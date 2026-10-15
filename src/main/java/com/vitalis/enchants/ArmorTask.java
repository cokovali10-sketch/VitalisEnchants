package com.vitalis.enchants;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/** owl ("Сова"): night vision while a charmed helmet is worn. */
public class ArmorTask implements Runnable {
    private final VitalisEnchants plugin;

    public ArmorTask(VitalisEnchants plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        var n = plugin.cfg().get("owl");
        if (!n.enabled) return;
        EnchantDef def = plugin.registry().get("owl");
        if (def == null) return;
        for (Player p : plugin.getServer().getOnlinePlayers()) {
            try {
                ItemStack helm = p.getInventory().getHelmet();
                if (helm == null || helm.getType().isAir()) continue;
                if (plugin.api().level(helm, "owl") <= 0) continue;
                p.addPotionEffect(new PotionEffect(
                        PotionEffectType.NIGHT_VISION, 300, 0, true, false, false));
            } catch (Exception ignored) {}
        }
    }
}
