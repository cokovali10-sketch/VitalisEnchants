package com.vitalis.enchants;

import org.bukkit.Material;

import java.util.EnumMap;
import java.util.Map;

/** Pure mapping of blocks that "autosmelt" turns into their furnace result. */
public final class SmeltTable {
    private static final Map<Material, Material> MAP = new EnumMap<>(Material.class);

    private SmeltTable() {}

    private static void put(Material in, Material out) {
        MAP.put(in, out);
    }

    static {
        put(Material.IRON_ORE, Material.IRON_INGOT);
        put(Material.DEEPSLATE_IRON_ORE, Material.IRON_INGOT);
        put(Material.RAW_IRON, Material.IRON_INGOT);
        put(Material.RAW_IRON_BLOCK, Material.IRON_BLOCK);
        put(Material.GOLD_ORE, Material.GOLD_INGOT);
        put(Material.DEEPSLATE_GOLD_ORE, Material.GOLD_INGOT);
        put(Material.NETHER_GOLD_ORE, Material.GOLD_INGOT);
        put(Material.RAW_GOLD, Material.GOLD_INGOT);
        put(Material.RAW_GOLD_BLOCK, Material.GOLD_BLOCK);
        put(Material.COPPER_ORE, Material.COPPER_INGOT);
        put(Material.DEEPSLATE_COPPER_ORE, Material.COPPER_INGOT);
        put(Material.RAW_COPPER, Material.COPPER_INGOT);
        put(Material.RAW_COPPER_BLOCK, Material.COPPER_BLOCK);
        put(Material.ANCIENT_DEBRIS, Material.NETHERITE_SCRAP);
        put(Material.SAND, Material.GLASS);
        put(Material.RED_SAND, Material.GLASS);
        put(Material.COBBLESTONE, Material.STONE);
        put(Material.COBBLED_DEEPSLATE, Material.DEEPSLATE);
        put(Material.CLAY, Material.TERRACOTTA);
        put(Material.CLAY_BALL, Material.BRICK);
        put(Material.NETHERRACK, Material.NETHER_BRICK);
        put(Material.WET_SPONGE, Material.SPONGE);
        put(Material.SEA_PICKLE, Material.LIME_DYE);
        put(Material.KELP, Material.DRIED_KELP);
    }

    public static boolean smeltable(Material m) {
        return MAP.containsKey(m);
    }

    public static Material result(Material m) {
        return MAP.get(m);
    }
}
