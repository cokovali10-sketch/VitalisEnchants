package com.vitalis.enchants;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;

/**
 * autosmelt ("Плавка", pickaxes): drops the furnace result of an ore/block.
 * lumberjack ("Дровосек", axes): fells the log column of the same wood.
 * Both skip silk-touch tools and cost durability like vanilla.
 */
public class ToolListener implements Listener {
    private static final int MAX_LOGS = 64;

    private final VitalisEnchants plugin;

    public ToolListener(VitalisEnchants plugin) {
        this.plugin = plugin;
    }

    public static boolean isLog(Material m) {
        String n = m.name();
        return n.endsWith("_LOG") || n.endsWith("_WOOD")
                || n.endsWith("_STEM") || n.endsWith("_HYPHAE");
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBreak(BlockBreakEvent e) {
        Player p = e.getPlayer();
        ItemStack tool = p.getInventory().getItemInMainHand();
        if (tool.getType().isAir()) return;
        String mat = tool.getType().name();
        boolean silk = tool.containsEnchantment(Enchantment.SILK_TOUCH);

        EnchantDef smelt = plugin.registry().get("autosmelt");
        if (!silk && smelt != null && smelt.fits(mat)
                && plugin.cfg().get("autosmelt").enabled
                && plugin.api().level(tool, "autosmelt") > 0) {
            Material block = e.getBlock().getType();
            if (SmeltTable.smeltable(block)) {
                Material out = SmeltTable.result(block);
                if (out != null) {
                    try {
                        e.setDropItems(false);
                        e.getBlock().getWorld().dropItemNaturally(
                                e.getBlock().getLocation().add(0.5, 0.5, 0.5), new ItemStack(out));
                    } catch (Exception ignored) {}
                }
            }
        }

        EnchantDef lj = plugin.registry().get("lumberjack");
        if (lj != null && lj.fits(mat)
                && plugin.cfg().get("lumberjack").enabled
                && plugin.api().level(tool, "lumberjack") > 0
                && isLog(e.getBlock().getType())) {
            fell(e.getBlock(), tool, p);
        }
    }

    private void fell(Block base, ItemStack tool, Player p) {
        Material want = base.getType();
        int broken = 0;
        Block b = base.getRelative(0, 1, 0);
        while (broken < MAX_LOGS && b.getType() == want) {
            try {
                if (b.breakNaturally(tool)) broken++;
            } catch (Exception ignored) { break; }
            b = b.getRelative(0, 1, 0);
        }
        if (broken > 0) damage(tool, broken, p);
    }

    private void damage(ItemStack tool, int amount, Player p) {
        try {
            if (!(tool.getItemMeta() instanceof Damageable dm)) return;
            int max = tool.getType().getMaxDurability();
            int next = dm.getDamage() + amount;
            if (next >= max) {
                tool.setAmount(0);
                p.playSound(p.getLocation(), org.bukkit.Sound.ENTITY_ITEM_BREAK, 1f, 1f);
            } else {
                dm.setDamage(next);
                tool.setItemMeta(dm);
            }
        } catch (Exception ignored) {}
    }
}
