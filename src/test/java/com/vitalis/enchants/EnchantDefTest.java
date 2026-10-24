package com.vitalis.enchants;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class EnchantDefTest {

    private static EnchantDef def(String... groups) {
        return new EnchantDef("t", "T", 3, Set.of(groups));
    }

    @Test
    void meleeGroup() {
        EnchantDef d = def("MELEE");
        assertTrue(d.fits("DIAMOND_SWORD"));
        assertTrue(d.fits("NETHERITE_AXE"));
        assertFalse(d.fits("BOW"));
        assertFalse(d.fits("DIAMOND_PICKAXE"));
    }

    @Test
    void armorGroups() {
        assertTrue(def("HELMETS").fits("DIAMOND_HELMET"));
        assertTrue(def("BOOTS").fits("GOLDEN_BOOTS"));
        assertTrue(def("CHESTPLATES").fits("ELYTRA"));
        assertTrue(def("ARMOR").fits("IRON_LEGGINGS"));
        assertFalse(def("ARMOR").fits("IRON_SWORD"));
    }

    @Test
    void toolsAndBow() {
        assertTrue(def("PICKAXES").fits("STONE_PICKAXE"));
        assertTrue(def("AXES").fits("WOODEN_AXE"));
        assertTrue(def("BOWS").fits("BOW"));
        assertTrue(def("BOWS").fits("CROSSBOW"));
        assertFalse(def("BOWS").fits("ARROW"));
    }

    @Test
    void exactMaterial() {
        assertTrue(def("BOW").fits("BOW"));
        assertFalse(def("BOW").fits("CROSSBOW"));
    }

    @Test
    void nullSafe() {
        assertFalse(def("MELEE").fits(null));
    }
}
