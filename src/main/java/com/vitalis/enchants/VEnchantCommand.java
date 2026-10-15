package com.vitalis.enchants;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * /venchant give &lt;player&gt; &lt;id&gt; [level] | list | remove &lt;player&gt; &lt;id&gt;.
 * OP-only (vitalisenchants.admin). Applies to the item in the target's main hand.
 */
public class VEnchantCommand implements CommandExecutor, TabCompleter {
    private final VitalisEnchants plugin;

    public VEnchantCommand(VitalisEnchants plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender s, Command cmd, String label, String[] a) {
        if (!s.hasPermission("vitalisenchants.admin") && !s.isOp()) {
            s.sendMessage(Msg.color("&cНет доступа."));
            return true;
        }
        if (a.length == 0 || a[0].equalsIgnoreCase("list")) {
            s.sendMessage(Msg.color("&6Кастомные чары Heaven:"));
            for (EnchantDef d : plugin.registry().values()) {
                var n = plugin.cfg().get(d.id);
                s.sendMessage(Msg.color((n.enabled ? "&a" : "&c&m")
                        + d.id + " &7— " + d.nameRu + " (макс. " + n.maxLevel + ")"));
            }
            return true;
        }
        if (a[0].equalsIgnoreCase("give") && a.length >= 3) {
            Player t = Bukkit.getPlayerExact(a[1]);
            if (t == null) {
                s.sendMessage(Msg.color("&cИгрок не в сети."));
                return true;
            }
            EnchantDef def = plugin.registry().get(a[2].toLowerCase(Locale.ROOT));
            if (def == null) {
                s.sendMessage(Msg.color("&cНет такого чара. Список: /venchant list"));
                return true;
            }
            var n = plugin.cfg().get(def.id);
            if (!n.enabled) {
                s.sendMessage(Msg.color("&cЧар выключен в конфиге."));
                return true;
            }
            int lvl = 1;
            if (a.length >= 4) {
                try {
                    lvl = Integer.parseInt(a[3]);
                } catch (NumberFormatException e) {
                    s.sendMessage(Msg.color("&cУровень — число."));
                    return true;
                }
            }
            lvl = EnchantMath.clampLevel(lvl, Math.min(def.hardCap, n.maxLevel));
            ItemStack hand = t.getInventory().getItemInMainHand();
            if (hand.getType().isAir()) {
                s.sendMessage(Msg.color("&cУ игрока пустая рука — пусть возьмёт предмет."));
                return true;
            }
            if (!VitalisEnchants.fits(def, hand.getType().name())) {
                s.sendMessage(Msg.color("&cЧар «" + def.nameRu
                        + "» не подходит этому предмету."));
                return true;
            }
            if (!plugin.api().apply(hand, def, lvl)) {
                s.sendMessage(Msg.color("&cНе вышло наложить."));
                return true;
            }
            s.sendMessage(Msg.color("&aВыдано: &f" + def.nameRu + " "
                    + EnchantMath.roman(lvl) + " &7→ &f" + t.getName()));
            t.sendMessage(Msg.color("&6◆ На твой предмет легли чары: &f"
                    + def.nameRu + " " + EnchantMath.roman(lvl)));
            return true;
        }
        if (a[0].equalsIgnoreCase("remove") && a.length >= 3) {
            Player t = Bukkit.getPlayerExact(a[1]);
            if (t == null) {
                s.sendMessage(Msg.color("&cИгрок не в сети."));
                return true;
            }
            EnchantDef def = plugin.registry().get(a[2].toLowerCase(Locale.ROOT));
            if (def == null) {
                s.sendMessage(Msg.color("&cНет такого чара."));
                return true;
            }
            ItemStack hand = t.getInventory().getItemInMainHand();
            if (!plugin.api().apply(hand, def, 0)) {
                s.sendMessage(Msg.color("&cНечего снимать."));
                return true;
            }
            s.sendMessage(Msg.color("&aСнято: &f" + def.nameRu + " &7→ &f" + t.getName()));
            return true;
        }
        s.sendMessage(Msg.color("&e/venchant give <ник> <id> [уровень]"));
        s.sendMessage(Msg.color("&e/venchant remove <ник> <id> &7| &e/venchant list"));
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender s, Command cmd, String label, String[] a) {
        List<String> out = new ArrayList<>();
        if (a.length == 1) {
            for (String sub : new String[]{"give", "list", "remove"}) {
                if (sub.startsWith(a[0].toLowerCase(Locale.ROOT))) out.add(sub);
            }
        } else if (a.length == 2 && (a[0].equalsIgnoreCase("give") || a[0].equalsIgnoreCase("remove"))) {
            String q = a[1].toLowerCase(Locale.ROOT);
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (p.getName().toLowerCase(Locale.ROOT).startsWith(q)) out.add(p.getName());
            }
        } else if (a.length == 3 && (a[0].equalsIgnoreCase("give") || a[0].equalsIgnoreCase("remove"))) {
            String q = a[2].toLowerCase(Locale.ROOT);
            for (String id : plugin.registry().keySet()) {
                if (id.startsWith(q)) out.add(id);
            }
        }
        return out;
    }
}
