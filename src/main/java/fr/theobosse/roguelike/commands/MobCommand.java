package fr.theobosse.roguelike.commands;

import fr.theobosse.roguelike.game.Enemy;
import fr.theobosse.roguelike.tools.Configs;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class MobCommand implements CommandExecutor {

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

}
