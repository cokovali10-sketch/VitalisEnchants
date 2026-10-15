package com.vitalis.enchants;

/** Pure math: level scaling + roman numerals. Fully unit-tested, no Bukkit. */
public final class EnchantMath {
    private EnchantMath() {}

    /** Chance grows per level, capped at 1.0. */
    public static double chance(double base, double perLevel, int level) {
        if (level <= 0) return 0;
        return Math.min(1.0, Math.max(0, base + perLevel * (level - 1)));
    }

    /** Multiplier grows per level (1.0 = no bonus). */
    public static double mult(double base, double perLevel, int level) {
        if (level <= 0) return 1.0;
        return Math.max(1.0, base + perLevel * (level - 1));
    }

    public static int clampLevel(int level, int cap) {
        return Math.max(1, Math.min(Math.max(1, cap), level));
    }

    private static final String[] ROMAN = {
        "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X"};

    public static String roman(int level) {
        if (level >= 1 && level <= ROMAN.length) return ROMAN[level - 1];
        return String.valueOf(level);
    }
}
