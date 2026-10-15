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

    public static boolean isHelmet(String mat) {
        return mat.endsWith("_HELMET");
    }

    public static boolean isMelee(String mat) {
        return mat.endsWith("_SWORD") || mat.endsWith("_AXE");
    }
}
