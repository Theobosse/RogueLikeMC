package fr.theobosse.roguelike.commands;

import fr.theobosse.roguelike.generation.Map;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class GenerationCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) return false;
        Player player = (Player) sender;
        player.sendMessage("§eGeneration...");
        if (args.length >= 5) {
            int width = Integer.parseInt(args[0]);
            int length = Integer.parseInt(args[1]);
            int tileSize = Integer.parseInt(args[2]);
            int corridorLength = Integer.parseInt(args[3]);
            int height = Integer.parseInt(args[4]);
            Map map = new Map(width, length, tileSize, corridorLength, height);
            map.generate(player.getLocation().subtract(0, 1, 0));
        } else {
            Map map = new Map(4, 4, 8, 2, 2);
            map.generate(player.getLocation().subtract(0, 1, 0));
        }
        player.sendMessage("§aGeneration done !");
        return true;
    }
}
