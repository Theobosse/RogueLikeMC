package fr.theobosse.roguelike.commands;

import fr.theobosse.roguelike.game.Enemy;
import fr.theobosse.roguelike.tools.Configs;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MobCommand implements CommandExecutor, @Nullable TabCompleter {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) return false;
        Player p = (Player) sender;

        if (args.length == 1) {
            String id = args[0];
            ConfigurationSection section = Configs.getConfig("mobs").getConfigurationSection(id);
            Enemy e = new Enemy(section);
            e.spawn(p.getLocation());
        } else {
            p.sendMessage("§cVous devez entrer un argument !");
        }

        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            YamlConfiguration config = Configs.getConfig("mobs");
            ArrayList<String> mobs = new ArrayList<>(config.getKeys(false));
            ArrayList<String> completions = new ArrayList<>();

            StringUtil.copyPartialMatches(args[0], mobs, completions);
            Collections.sort(completions);
            return completions;
        }
        return null;
    }
}
