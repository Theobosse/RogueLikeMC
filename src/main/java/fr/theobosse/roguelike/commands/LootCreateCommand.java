package fr.theobosse.roguelike.commands;

import fr.theobosse.roguelike.game.LootCreate;
import fr.theobosse.roguelike.tools.Configs;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class LootCreateCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String cmd, @NotNull String[] args) {
        if (!(sender instanceof Player)) return false;
        Player p = (Player) sender;
        if (args.length == 1) {
            String id = args[0];
            ConfigurationSection section = Configs.getConfig("loots").getConfigurationSection(id);
            LootCreate e = new LootCreate(section);
            e.summon(p.getLocation());
        } else {
            p.sendMessage("§cVous devez entrer un argument !");
        }

        return false;
    }
}
