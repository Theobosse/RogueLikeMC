package fr.theobosse.roguelike.commands;

import fr.theobosse.roguelike.game.Weapon;
import fr.theobosse.roguelike.tools.Configs;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WeaponCommand implements CommandExecutor, @Nullable TabCompleter {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String cmd, @NotNull String[] args) {
        YamlConfiguration config = Configs.getConfig("weapons");
        if (args.length >= 2) {
            Weapon wp = Weapon.getWeapon(args[0]);
            Player p = Bukkit.getPlayer(args[1]);

            if (wp == null) {
                commandSender.sendMessage("§cL'arme entrée n'a pas été trouvée !");
                return false;
            }

            if (p == null) {
                commandSender.sendMessage("§cLe joueur entré est invalide !");
                return false;
            }

            p.getInventory().addItem(wp.build());
        } else if (args.length >= 1) {
            Weapon wp = Weapon.getWeapon(args[0]);

            if (wp == null) {
                commandSender.sendMessage("§cL'arme entrée n'a pas été trouvée !");
                return false;
            }

            if (!(commandSender instanceof Player)) {
                commandSender.sendMessage("§cVous ne pouvez pas donner d'item à la console !");
                return false;
            }

            ((Player) commandSender).getInventory().addItem(wp.build());
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            YamlConfiguration config = Configs.getConfig("weapons");
            ArrayList<String> weapons = new ArrayList<>(config.getKeys(false));
            ArrayList<String> completions = new ArrayList<>();

            StringUtil.copyPartialMatches(args[0], weapons, completions);
            Collections.sort(completions);
            return completions;
        }
        return null;
    }
}
