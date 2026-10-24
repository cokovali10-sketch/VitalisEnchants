package com.vitalis.enchants;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * Melee &amp; ranged weapon enchants.
 * storm (lightning, no fire), vampire (lifesteal), frost (slow), venom (poison),
 * berserk (bonus damage at low health), hunter (bonus arrow damage).
 */
public class CombatListener implements Listener {
    private final VitalisEnchants plugin;

    public CombatListener(VitalisEnchants plugin) {
        this.plugin = plugin;
    }

    /** berserk modifies damage, so it runs before MONITOR. */
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onBerserk(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player p)) return;
        if (!(e.getEntity() instanceof LivingEntity)) return;
        ItemStack hand = p.getInventory().getItemInMainHand();
        if (hand.getType().isAir()) return;
        EnchantDef def = plugin.registry().get("berserk");
        if (def == null || !def.fits(hand.getType().name())) return;
        var n = plugin.cfg().get("berserk");
        if (!n.enabled) return;
        int lvl = plugin.api().level(hand, "berserk");
        if (lvl <= 0) return;
        try {
            if (p.getHealth() <= p.getMaxHealth() * 0.5
                    && Math.random() < EnchantMath.chance(n.chanceBase, n.chancePerLevel, lvl)) {
                e.setDamage(e.getDamage() * EnchantMath.mult(n.multBase, n.multPerLevel, lvl));
            }
        } catch (Exception ignored) {}
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onHit(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player p)) return;
        if (!(e.getEntity() instanceof LivingEntity target)) return;
        ItemStack hand = p.getInventory().getItemInMainHand();
        if (hand.getType().isAir()) return;
        String mat = hand.getType().name();

        storm(p, hand, mat, target);
        vampire(p, hand, mat, e);
        frost(hand, mat, target);
        venom(hand, mat, target);
    }

    private void storm(Player p, ItemStack hand, String mat, LivingEntity target) {
        EnchantDef def = plugin.registry().get("storm");
        if (def == null || !def.fits(mat)) return;
        var n = plugin.cfg().get("storm");
        if (!n.enabled) return;
        int lvl = plugin.api().level(hand, "storm");
        if (lvl > 0 && Math.random() < EnchantMath.chance(n.chanceBase, n.chancePerLevel, lvl)) {
            try {
                target.getWorld().strikeLightningEffect(target.getLocation());
            } catch (Exception ignored) {}
        }
    }

    private void vampire(Player p, ItemStack hand, String mat, EntityDamageByEntityEvent e) {
        EnchantDef def = plugin.registry().get("vampire");
        if (def == null || !def.fits(mat)) return;
        var n = plugin.cfg().get("vampire");
        if (!n.enabled) return;
        int lvl = plugin.api().level(hand, "vampire");
        if (lvl <= 0) return;
        double heal = e.getFinalDamage() * (EnchantMath.mult(n.multBase, n.multPerLevel, lvl) - 1.0);
        if (heal > 0) {
            try {
                p.setHealth(Math.min(p.getMaxHealth(), p.getHealth() + heal));
            } catch (Exception ignored) {}
        }
    }

    private void frost(ItemStack hand, String mat, LivingEntity target) {
        EnchantDef def = plugin.registry().get("frost");
        if (def == null || !def.fits(mat)) return;
        var n = plugin.cfg().get("frost");
        if (!n.enabled) return;
        int lvl = plugin.api().level(hand, "frost");
        if (lvl > 0 && Math.random() < EnchantMath.chance(n.chanceBase, n.chancePerLevel, lvl)) {
            int ticks = EnchantMath.duration(n.durationBase, n.durationPerLevel, lvl);
            try {
                target.addPotionEffect(new PotionEffect(
                        PotionEffectType.SLOWNESS, ticks, 0, true, false, false));
            } catch (Exception ignored) {}
        }
    }

    private void venom(ItemStack hand, String mat, LivingEntity target) {
        EnchantDef def = plugin.registry().get("venom");
        if (def == null || !def.fits(mat)) return;
        var n = plugin.cfg().get("venom");
        if (!n.enabled) return;
        int lvl = plugin.api().level(hand, "venom");
        if (lvl > 0 && Math.random() < EnchantMath.chance(n.chanceBase, n.chancePerLevel, lvl)) {
            int ticks = EnchantMath.duration(n.durationBase, n.durationPerLevel, lvl);
            try {
                target.addPotionEffect(new PotionEffect(
                        PotionEffectType.POISON, ticks, 0, true, false, false));
            } catch (Exception ignored) {}
        }
    }

    /** hunter: bonus damage from arrows shot by a player holding an enchanted bow. */
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onArrow(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Projectile proj)) return;
        if (!(proj.getShooter() instanceof Player p)) return;
        if (!(e.getEntity() instanceof LivingEntity)) return;
        ItemStack bow = p.getInventory().getItemInMainHand();
        if (bow.getType().isAir()) return;
        EnchantDef def = plugin.registry().get("hunter");
        if (def == null || !def.fits(bow.getType().name())) return;
        var n = plugin.cfg().get("hunter");
        if (!n.enabled) return;
        int lvl = plugin.api().level(bow, "hunter");
        if (lvl <= 0) return;
        try {
            e.setDamage(e.getDamage() * EnchantMath.mult(n.multBase, n.multPerLevel, lvl));
        } catch (Exception ignored) {}
    }
}
