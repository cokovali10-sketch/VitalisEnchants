package com.vitalis.enchants;

import net.kyori.adventure.text.Component;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

/**
 * PDC storage + lore lines. The PDC tag is the truth; lore is decoration
 * rebuilt on every change (stale lines of the same enchant are dropped).
 */
public class EnchantApi {
    private final JavaPlugin plugin;

    public EnchantApi(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public NamespacedKey key(String id) {
        return new NamespacedKey(plugin, "venchant_" + id);
    }

    public int level(ItemStack item, String id) {
        if (item == null || !item.hasItemMeta()) return 0;
        ItemMeta m = item.getItemMeta();
        Integer v = m.getPersistentDataContainer().get(key(id), PersistentDataType.INTEGER);
        return v == null ? 0 : Math.max(0, v);
    }

    /** Applies level (<=0 removes). Returns false when item unsuitable. */
    public boolean apply(ItemStack item, EnchantDef def, int level) {
        if (item == null || item.getType().isAir() || !item.hasItemMeta()) return false;
        ItemMeta m = item.getItemMeta();
        if (level <= 0) {
            m.getPersistentDataContainer().remove(key(def.id));
        } else {
            m.getPersistentDataContainer().set(key(def.id), PersistentDataType.INTEGER, level);
        }
        rebuildLore(m, def);
        item.setItemMeta(m);
        return true;
    }

    private void rebuildLore(ItemMeta m, EnchantDef changed) {
        List<Component> lore = m.hasLore() ? new ArrayList<>(m.lore()) : new ArrayList<>();
        String needle = changed.nameRu;
        lore.removeIf(c -> plain(c).contains(needle));
        int lvl = 0;
        Integer v = m.getPersistentDataContainer().get(key(changed.id), PersistentDataType.INTEGER);
        if (v != null) lvl = Math.max(0, v);
        if (lvl > 0) {
            lore.add(Msg.mm("<gradient:#B388FF:#7C4DFF>◆ " + changed.nameRu
                    + " " + EnchantMath.roman(lvl) + "</gradient>"));
        }
        m.lore(lore.isEmpty() ? null : lore);
    }

    static String plain(Component c) {
        try {
            return net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
                    .plainText().serialize(c);
        } catch (Exception e) {
            return "";
        }
    }
}
