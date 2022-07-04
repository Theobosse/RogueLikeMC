package fr.theobosse.roguelike.commands;

import fr.theobosse.roguelike.generation.Plots;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PlotCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String cmd, @NotNull String[] args) {
        if (!(sender instanceof Player)) return false;
        Player player = (Player) sender;

        if (args.length == 0) {
            player.sendMessage("§cVous devez indiquer l'id du plot et son type.");
            return false;
        }

        if (args.length == 1) {
            player.sendMessage("§cVous devez indiquer le type de 'plot' que vous souhaitez faire.");
            return false;
        }

        String id = args[0];
        String type = args[1];
        int length = 1;
        int width = 1;

        if (args.length >= 3)
            length = Integer.parseInt(args[2]);
        if (args.length >= 4)
            width = Integer.parseInt(args[3]);

        Plots plot = new Plots(id, player.getLocation(), type, length, width);
        return true;
    }
}
