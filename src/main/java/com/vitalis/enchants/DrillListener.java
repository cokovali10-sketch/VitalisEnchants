package com.vitalis.enchants;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;

import java.util.ArrayList;
import java.util.List;

/**
 * drill ("Бур"): pickaxe breaks the 3x3 plane of the SAME material
 * around the mined block (8 neighbours max). Tile entities, liquids
 * and unbreakable blocks are never touched. Fortune on the tool applies
 * via breakNaturally. Extra blocks cost durability.
 */
public class DrillListener implements Listener {
    private final VitalisEnchants plugin;

    public DrillListener(VitalisEnchants plugin) {
        this.plugin = plugin;
    }

    /** Plane offsets for a 3x3 perpendicular to the mined face. Pure, tested. */
    public static List<int[]> plane(BlockFace face, boolean vertical) {
        List<int[]> out = new ArrayList<>(8);
        if (face == BlockFace.UP || face == BlockFace.DOWN || !vertical) {
            for (int dx = -1; dx <= 1; dx++) {
                for (int dz = -1; dz <= 1; dz++) {
                    if (dx == 0 && dz == 0) continue;
                    out.add(new int[]{dx, 0, dz});
                }
            }
        } else if (face == BlockFace.NORTH || face == BlockFace.SOUTH) {
            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = -1; dy <= 1; dy++) {
                    if (dx == 0 && dy == 0) continue;
                    out.add(new int[]{dx, dy, 0});
                }
            }
        } else {
            for (int dz = -1; dz <= 1; dz++) {
                for (int dy = -1; dy <= 1; dy++) {
                    if (dz == 0 && dy == 0) continue;
                    out.add(new int[]{0, dy, dz});
                }
            }
        }
        return out;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBreak(BlockBreakEvent e) {
        Player p = e.getPlayer();
        ItemStack tool = p.getInventory().getItemInMainHand();
        if (tool.getType().isAir() || !tool.getType().name().endsWith("_PICKAXE")) return;
        EnchantDef def = plugin.registry().get("drill");
        if (def == null) return;
        var n = plugin.cfg().get("drill");
        if (!n.enabled) return;
        if (plugin.api().level(tool, "drill") <= 0) return;

        Block center = e.getBlock();
        Material want = center.getType();
        if (want.isAir() || want == Material.BEDROCK || want == Material.BARRIER) return;
        if (want.getHardness() < 0) return;

        BlockFace face = BlockFace.DOWN;
        boolean vertical = false;
        try {
            var ray = p.rayTraceBlocks(6.0);
            if (ray != null && ray.getHitBlockFace() != null) {
                face = ray.getHitBlockFace();
                vertical = true;
            }
        } catch (Exception ignored) {
        }

        int broken = 0;
        for (int[] o : plane(face, vertical)) {
            try {
                Block b = center.getRelative(o[0], o[1], o[2]);
                if (b.getType() != want) continue;
                if (b.getState() instanceof org.bukkit.block.TileState) continue;
                if (b.breakNaturally(tool)) broken++;
            } catch (Exception ignored) {}
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
                try {
                    p.playSound(p.getLocation(), org.bukkit.Sound.ENTITY_ITEM_BREAK, 1f, 1f);
                } catch (Exception ignored) {}
            } else {
                dm.setDamage(next);
                tool.setItemMeta(dm);
            }
        } catch (Exception ignored) {}
    }
}
