package com.vitalis.enchants;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

/** feather ("Пёрышко"): no fall damage while enchanted boots are worn. */
public class FallListener implements Listener {
    private final VitalisEnchants plugin;

    public FallListener(VitalisEnchants plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onFall(EntityDamageEvent e) {
        if (e.getCause() != EntityDamageEvent.DamageCause.FALL) return;
        if (!(e.getEntity() instanceof Player p)) return;
        ItemStack boots = p.getInventory().getBoots();
        if (boots == null || boots.getType().isAir()) return;
        EnchantDef def = plugin.registry().get("feather");
        if (def == null || !def.fits(boots.getType().name())) return;
        if (!plugin.cfg().get("feather").enabled) return;
        if (plugin.api().level(boots, "feather") <= 0) return;
        e.setCancelled(true);
    }
}
