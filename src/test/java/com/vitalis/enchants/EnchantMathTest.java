package com.vitalis.enchants;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EnchantMathTest {

    @Test
    void chanceScalesAndCaps() {
        assertEquals(0.10, EnchantMath.chance(0.10, 0.10, 1), 1e-9);
        assertEquals(0.30, EnchantMath.chance(0.10, 0.10, 3), 1e-9);
        assertEquals(1.0, EnchantMath.chance(0.90, 0.50, 5), 1e-9);
        assertEquals(0, EnchantMath.chance(0.5, 0.1, 0), 1e-9);
    }

    @Test
    void multScales() {
        assertEquals(1.5, EnchantMath.mult(1.5, 0.5, 1), 1e-9);
        assertEquals(2.5, EnchantMath.mult(1.5, 0.5, 3), 1e-9);
        assertEquals(1.0, EnchantMath.mult(1.5, 0.5, 0), 1e-9);
    }

    @Test
    void vampireHealFractions() {
        // heal = damage * (mult - 1): must stay positive on every level
        assertEquals(0.10, EnchantMath.mult(1.10, 0.10, 1) - 1.0, 1e-9);
        assertEquals(0.20, EnchantMath.mult(1.10, 0.10, 2) - 1.0, 1e-9);
        assertEquals(0.30, EnchantMath.mult(1.10, 0.10, 3) - 1.0, 1e-9);
    }

    @Test
    void clampAndRoman() {
        assertEquals(1, EnchantMath.clampLevel(-5, 3));
        assertEquals(3, EnchantMath.clampLevel(99, 3));
        assertEquals("I", EnchantMath.roman(1));
        assertEquals("III", EnchantMath.roman(3));
        assertEquals("X", EnchantMath.roman(10));
    }
}
