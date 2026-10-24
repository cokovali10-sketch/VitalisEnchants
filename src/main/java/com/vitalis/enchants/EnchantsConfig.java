package com.vitalis.enchants;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

/** Numbers per enchant id. Missing section = compiled defaults (never breaks). */
public class EnchantsConfig {
    public static class Nums {
        public boolean enabled = true;
        public int maxLevel = 3;
        public double chanceBase;
        public double chancePerLevel;
        public double multBase = 1.0;
        public double multPerLevel;
        public double durationBase;
        public double durationPerLevel;
    }

    /** Every known enchant id; used as compiled fallback + config ensure-list. */
    public static final String[] IDS = {
        "storm", "vampire", "scholar", "frost", "venom", "berserk",
        "owl", "gills", "vigor", "spring", "feather",
        "drill", "haste", "autosmelt", "lumberjack", "hunter"
    };

    private final Map<String, Nums> map = new HashMap<>();

    public Nums get(String id) {
        return map.computeIfAbsent(id, k -> defaults(k));
    }

    private static Nums defaults(String id) {
        Nums n = new Nums();
        switch (id) {
            case "storm" -> { n.maxLevel = 3; n.chanceBase = 0.10; n.chancePerLevel = 0.10; }
            case "vampire" -> { n.maxLevel = 3; n.multBase = 1.10; n.multPerLevel = 0.10; }
            case "scholar" -> { n.maxLevel = 3; n.multBase = 1.5; n.multPerLevel = 0.5; }
            case "frost" -> {
                n.maxLevel = 3; n.chanceBase = 0.25; n.chancePerLevel = 0.10;
                n.durationBase = 40; n.durationPerLevel = 20;
            }
            case "venom" -> {
                n.maxLevel = 3; n.chanceBase = 0.20; n.chancePerLevel = 0.10;
                n.durationBase = 60; n.durationPerLevel = 20;
            }
            case "berserk" -> { n.maxLevel = 3; n.chanceBase = 1.0; n.multBase = 1.15; n.multPerLevel = 0.10; }
            case "owl" -> n.maxLevel = 1;
            case "gills" -> n.maxLevel = 1;
            case "feather" -> n.maxLevel = 1;
            case "autosmelt" -> n.maxLevel = 1;
            case "lumberjack" -> n.maxLevel = 1;
            case "vigor" -> n.maxLevel = 3;
            case "spring" -> { n.maxLevel = 3; n.multBase = 1.0; n.multPerLevel = 0.5; }
            case "haste" -> { n.maxLevel = 3; n.multBase = 1.0; n.multPerLevel = 0.5; }
            case "hunter" -> { n.maxLevel = 3; n.multBase = 1.20; n.multPerLevel = 0.10; }
            case "drill" -> n.maxLevel = 1;
            default -> n.enabled = false;
        }
        return n;
    }

    public void load(JavaPlugin plugin) {
        plugin.saveDefaultConfig();
        plugin.reloadConfig();
        FileConfiguration c = plugin.getConfig();
        map.clear();
        if (c.isConfigurationSection("enchants")) {
            for (String id : c.getConfigurationSection("enchants").getKeys(false)) {
                Nums n = defaults(id);
                String p = "enchants." + id + ".";
                n.enabled = c.getBoolean(p + "enabled", n.enabled);
                n.maxLevel = Math.max(1, c.getInt(p + "max-level", n.maxLevel));
                n.chanceBase = c.getDouble(p + "chance-base", n.chanceBase);
                n.chancePerLevel = c.getDouble(p + "chance-per-level", n.chancePerLevel);
                n.multBase = c.getDouble(p + "mult-base", n.multBase);
                n.multPerLevel = c.getDouble(p + "mult-per-level", n.multPerLevel);
                n.durationBase = c.getDouble(p + "duration-base", n.durationBase);
                n.durationPerLevel = c.getDouble(p + "duration-per-level", n.durationPerLevel);
                map.put(id, n);
            }
        }
        for (String id : IDS) {
            map.putIfAbsent(id, defaults(id));
        }
    }
}
