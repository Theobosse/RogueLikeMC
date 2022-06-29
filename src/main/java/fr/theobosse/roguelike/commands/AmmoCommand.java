package fr.theobosse.roguelike.commands;

import fr.theobosse.roguelike.game.Ammo;
import fr.theobosse.roguelike.game.Weapon;
import fr.theobosse.roguelike.tools.Configs;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class AmmoCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String cmd, @NotNull String[] args) {
        if (args.length >= 2) {
            // Check if number
            try {
                Double.parseDouble(args[0]);
            } catch(NumberFormatException e){
                commandSender.sendMessage(String.format("§cNombre invalide: '%s'", args[0]));
                return false;
            }

            int quantity = Integer.parseInt(args[0]);
            Player player = Bukkit.getPlayer(args[1]);

            if (player == null) {
                commandSender.sendMessage(String.format("§cJoueur invalide: '%s'", args[1]));
                return false;
            }

            ItemStack item = Ammo.getItem(quantity);
            player.getInventory().addItem(item);

        } else if (args.length >= 1) {
            // Check if number
            try {
                Double.parseDouble(args[0]);
            } catch(NumberFormatException e){
                commandSender.sendMessage(String.format("§cNombre invalide: '%s'", args[0]));
                return false;
            }

            if (!(commandSender instanceof Player)) {
                commandSender.sendMessage("§cVous ne pouvez pas donner d'item à la console !");
                return false;
            }

            int quantity = Integer.parseInt(args[0]);
            Player player = (Player) commandSender;

            ItemStack item = Ammo.getItem(quantity);
            player.getInventory().addItem(item);

        } else {
            if (!(commandSender instanceof Player)) {
                commandSender.sendMessage("§cVous ne pouvez pas donner d'item à la console !");
                return false;
            }

            Player player = (Player) commandSender;
            ItemStack item = Ammo.getItem(1);
            player.getInventory().addItem(item);
        }

        return true;
    }

}
