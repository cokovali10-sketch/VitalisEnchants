package com.vitalis.enchants;

import java.util.Set;

/** One custom enchant: identity + which items it fits. Numbers live in config. */
public class EnchantDef {
    public final String id;
    public final String nameRu;
    public final int hardCap;
    public final Set<String> materials;

    public EnchantDef(String id, String nameRu, int hardCap, Set<String> materials) {
        this.id = id;
        this.nameRu = nameRu;
        this.hardCap = hardCap;
        this.materials = materials;
    }

    public static boolean isHelmet(String mat) { return mat.endsWith("_HELMET"); }
    public static boolean isChestplate(String mat) { return mat.endsWith("_CHESTPLATE") || mat.equals("ELYTRA"); }
    public static boolean isLeggings(String mat) { return mat.endsWith("_LEGGINGS"); }
    public static boolean isBoots(String mat) { return mat.endsWith("_BOOTS"); }
    public static boolean isArmor(String mat) {
        return isHelmet(mat) || isChestplate(mat) || isLeggings(mat) || isBoots(mat);
    }
    public static boolean isSword(String mat) { return mat.endsWith("_SWORD"); }
    public static boolean isAxe(String mat) { return mat.endsWith("_AXE"); }
    public static boolean isPickaxe(String mat) { return mat.endsWith("_PICKAXE"); }
    public static boolean isShovel(String mat) { return mat.endsWith("_SHOVEL"); }
    public static boolean isHoe(String mat) { return mat.endsWith("_HOE"); }
    public static boolean isMelee(String mat) { return isSword(mat) || isAxe(mat); }
    public static boolean isTool(String mat) {
        return isPickaxe(mat) || isShovel(mat) || isAxe(mat) || isHoe(mat);
    }
    public static boolean isBow(String mat) { return mat.equals("BOW") || mat.equals("CROSSBOW"); }

    /**
     * Suffix-based group check (version-proof, no Bukkit Material lookups).
     * Groups: SWORDS AXES MELEE HELMETS CHESTPLATES LEGGINGS BOOTS ARMOR
     * PICKAXES SHOVELS TOOLS BOWS, or an exact material name.
     */
    public boolean fits(String mat) {
        if (mat == null) return false;
        for (String g : materials) {
            switch (g) {
                case "SWORDS" -> { if (isSword(mat)) return true; }
                case "AXES" -> { if (isAxe(mat)) return true; }
                case "MELEE" -> { if (isMelee(mat)) return true; }
                case "HELMETS" -> { if (isHelmet(mat)) return true; }
                case "CHESTPLATES" -> { if (isChestplate(mat)) return true; }
                case "LEGGINGS" -> { if (isLeggings(mat)) return true; }
                case "BOOTS" -> { if (isBoots(mat)) return true; }
                case "ARMOR" -> { if (isArmor(mat)) return true; }
                case "PICKAXES" -> { if (isPickaxe(mat)) return true; }
                case "SHOVELS" -> { if (isShovel(mat)) return true; }
                case "TOOLS" -> { if (isTool(mat)) return true; }
                case "BOWS" -> { if (isBow(mat)) return true; }
                default -> { if (g.equals(mat)) return true; }
            }
        }
        return false;
    }
}
