package com.vitalis.enchants;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * Passive armor/tool enchants, refreshed every 5s (short, safe effects):
 * owl (night vision), gills (water breathing), vigor (regeneration),
 * spring (jump boost on boots), haste (mining haste while holding a pickaxe).
 */
public class ArmorTask implements Runnable {
    private static final int TICKS = 300;

    private final VitalisEnchants plugin;

    public ArmorTask(VitalisEnchants plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        for (Player p : plugin.getServer().getOnlinePlayers()) {
            try {
                passive(p, p.getInventory().getHelmet(), "owl", PotionEffectType.NIGHT_VISION, false);
                passive(p, p.getInventory().getHelmet(), "gills", PotionEffectType.WATER_BREATHING, false);
                passive(p, p.getInventory().getChestplate(), "vigor", PotionEffectType.REGENERATION, false);
                passive(p, p.getInventory().getBoots(), "spring", PotionEffectType.JUMP_BOOST, true);
                passive(p, p.getInventory().getItemInMainHand(), "haste", PotionEffectType.HASTE, true);
            } catch (Exception ignored) {}
        }
    }

    private void passive(Player p, ItemStack item, String id, PotionEffectType type, boolean ampByLevel) {
        if (item == null || item.getType().isAir()) return;
        EnchantDef def = plugin.registry().get(id);
        if (def == null || !def.fits(item.getType().name())) return;
        var n = plugin.cfg().get(id);
        if (!n.enabled) return;
        int lvl = plugin.api().level(item, id);
        if (lvl <= 0) return;
        int amp = ampByLevel ? Math.max(0, lvl - 1) : 0;
        p.addPotionEffect(new PotionEffect(type, TICKS, amp, true, false, false));
    }
}
