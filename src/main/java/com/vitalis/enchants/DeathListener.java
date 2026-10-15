package com.vitalis.enchants;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

/** scholar: multiply XP orbs from mob kills. */
public class DeathListener implements Listener {
    private final VitalisEnchants plugin;

    public DeathListener(VitalisEnchants plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onKill(EntityDeathEvent e) {
        Player killer = e.getEntity().getKiller();
        if (killer == null) return;
        ItemStack hand = killer.getInventory().getItemInMainHand();
        if (hand.getType().isAir()) return;
        EnchantDef def = plugin.registry().get("scholar");
        if (def == null || !VitalisEnchants.fits(def, hand.getType().name())) return;
        var n = plugin.cfg().get("scholar");
        if (!n.enabled) return;
        int lvl = plugin.api().level(hand, "scholar");
        if (lvl <= 0) return;
        try {
            int xp = e.getDroppedExp();
            if (xp > 0) e.setDroppedExp((int) Math.round(
                    xp * EnchantMath.mult(n.multBase, n.multPerLevel, lvl)));
        } catch (Exception ignored) {}
    }
}
