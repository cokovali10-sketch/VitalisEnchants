package com.vitalis.enchants;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * VitalisEnchants v1.0.0 - Paper/Purpur 1.21.11.
 * Custom enchantments, OP-only distribution via /venchant.
 * Storage: PersistentDataContainer + lore line (PDC is the truth).
 */
public class VitalisEnchants extends JavaPlugin {
    private final EnchantsConfig cfg = new EnchantsConfig();
    private EnchantApi api;
    private final Map<String, EnchantDef> registry = new LinkedHashMap<>();

    @Override
    public void onEnable() {
        cfg.load(this);
        api = new EnchantApi(this);
        Set<String> melee = Set.of("MELEE");
        Set<String> helmets = Set.of("HELMETS");
        Set<String> chests = Set.of("CHESTPLATES");
        Set<String> boots = Set.of("BOOTS");
        Set<String> picks = Set.of("PICKAXES");
        Set<String> axes = Set.of("AXES");
        Set<String> bows = Set.of("BOWS");

        registry.put("storm", new EnchantDef("storm", "Гроза", 3, melee));
        registry.put("vampire", new EnchantDef("vampire", "Вампир", 3, melee));
        registry.put("scholar", new EnchantDef("scholar", "Учёный", 3, melee));
        registry.put("frost", new EnchantDef("frost", "Мороз", 3, melee));
        registry.put("venom", new EnchantDef("venom", "Яд", 3, melee));
        registry.put("berserk", new EnchantDef("berserk", "Ярость", 3, melee));

        registry.put("owl", new EnchantDef("owl", "Сова", 1, helmets));
        registry.put("gills", new EnchantDef("gills", "Жабры", 1, helmets));
        registry.put("vigor", new EnchantDef("vigor", "Стойкость", 3, chests));
        registry.put("spring", new EnchantDef("spring", "Пружина", 3, boots));
        registry.put("feather", new EnchantDef("feather", "Пёрышко", 1, boots));

        registry.put("drill", new EnchantDef("drill", "Бур", 1, picks));
        registry.put("haste", new EnchantDef("haste", "Спешка", 3, picks));
        registry.put("autosmelt", new EnchantDef("autosmelt", "Плавка", 1, picks));
        registry.put("lumberjack", new EnchantDef("lumberjack", "Дровосек", 1, axes));

        registry.put("hunter", new EnchantDef("hunter", "Охотник", 3, bows));

        VEnchantCommand cmd = new VEnchantCommand(this);
        try {
            var pc = getCommand("venchant");
            if (pc != null) {
                pc.setExecutor(cmd);
                pc.setTabCompleter(cmd);
            } else {
                getLogger().warning("/venchant not in plugin.yml, command disabled.");
            }
        } catch (Exception e) {
            getLogger().warning("Command register failed: " + e.getMessage());
        }

        getServer().getPluginManager().registerEvents(new CombatListener(this), this);
        getServer().getPluginManager().registerEvents(new DeathListener(this), this);
        getServer().getPluginManager().registerEvents(new DrillListener(this), this);
        getServer().getPluginManager().registerEvents(new FallListener(this), this);
        getServer().getPluginManager().registerEvents(new ToolListener(this), this);
        getServer().getScheduler().runTaskTimer(this, new ArmorTask(this), 100L, 100L);

        getLogger().info("VitalisEnchants enabled: " + registry.size() + " enchantments.");
    }

    public EnchantsConfig cfg() { return cfg; }
    public EnchantApi api() { return api; }
    public Map<String, EnchantDef> registry() { return registry; }

    /** Material group check, delegated to the pure EnchantDef logic. */
    public static boolean fits(EnchantDef def, String mat) {
        return def != null && def.fits(mat);
    }
}
