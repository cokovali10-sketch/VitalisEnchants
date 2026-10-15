package com.vitalis.enchants;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

/** storm (lightning, no fire) + vampire (lifesteal) on melee hits. */
public class CombatListener implements Listener {
    private final VitalisEnchants plugin;

    public CombatListener(VitalisEnchants plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onHit(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player p)) return;
        if (!(e.getEntity() instanceof LivingEntity target)) return;
        ItemStack hand = p.getInventory().getItemInMainHand();
        if (hand.getType().isAir()) return;
        String mat = hand.getType().name();

        EnchantDef storm = plugin.registry().get("storm");
        if (storm != null && VitalisEnchants.fits(storm, mat)) {
            var n = plugin.cfg().get("storm");
            if (n.enabled) {
                int lvl = plugin.api().level(hand, "storm");
                if (lvl > 0 && Math.random() < EnchantMath.chance(n.chanceBase, n.chancePerLevel, lvl)) {
                    try {
                        target.getWorld().strikeLightningEffect(target.getLocation());
                    } catch (Exception ignored) {}
                }
            }
        }

        EnchantDef vamp = plugin.registry().get("vampire");
        if (vamp != null && VitalisEnchants.fits(vamp, mat)) {
            var n = plugin.cfg().get("vampire");
            if (n.enabled) {
                int lvl = plugin.api().level(hand, "vampire");
                if (lvl > 0) {
                    double heal = e.getFinalDamage()
                            * (EnchantMath.mult(n.multBase, n.multPerLevel, lvl) - 1.0);
                    if (heal > 0) {
                        try {
                            double max = p.getMaxHealth();
                            p.setHealth(Math.min(max, p.getHealth() + heal));
                        } catch (Exception ignored) {}
                    }
                }
            }
        }
    }
}
