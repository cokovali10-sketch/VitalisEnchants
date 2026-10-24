package com.vitalis.enchants;

import org.bukkit.Material;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SmeltTableTest {

    @Test
    void mapsOresToFurnaceResult() {
        assertEquals(Material.IRON_INGOT, SmeltTable.result(Material.IRON_ORE));
        assertEquals(Material.IRON_INGOT, SmeltTable.result(Material.DEEPSLATE_IRON_ORE));
        assertEquals(Material.GOLD_INGOT, SmeltTable.result(Material.DEEPSLATE_GOLD_ORE));
        assertEquals(Material.NETHERITE_SCRAP, SmeltTable.result(Material.ANCIENT_DEBRIS));
        assertEquals(Material.GLASS, SmeltTable.result(Material.SAND));
        assertEquals(Material.STONE, SmeltTable.result(Material.COBBLESTONE));
    }

    @Test
    void unmappedBlocksStayVanilla() {
        assertTrue(SmeltTable.smeltable(Material.COPPER_ORE));
        assertFalse(SmeltTable.smeltable(Material.DIAMOND_ORE));
        assertNull(SmeltTable.result(Material.DIAMOND_ORE));
    }
}
