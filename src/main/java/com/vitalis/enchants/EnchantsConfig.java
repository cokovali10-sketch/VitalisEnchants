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
    }

    private final Map<String, Nums> map = new HashMap<>();

    public Nums get(String id) {
        return map.computeIfAbsent(id, k -> defaults(k));
    }

    private static Nums defaults(String id) {
        Nums n = new Nums();
        switch (id) {
            case "storm" -> { n.maxLevel = 3; n.chanceBase = 0.10; n.chancePerLevel = 0.10; }
            case "vampire" -> { n.maxLevel = 3; n.chanceBase = 1.0; n.chancePerLevel = 0; n.multBase = 0.10; n.multPerLevel = 0.10; }
            case "scholar" -> { n.maxLevel = 3; n.multBase = 1.5; n.multPerLevel = 0.5; }
            case "owl" -> { n.maxLevel = 1; }
            case "drill" -> { n.maxLevel = 1; }
            default -> { n.enabled = false; }
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
                map.put(id, n);
            }
        }
        for (String id : new String[]{"storm", "vampire", "scholar", "owl", "drill"}) {
            map.putIfAbsent(id, defaults(id));
        }
    }
}
