package fr.theobosse.roguelike.commands;

import fr.theobosse.roguelike.game.Weapon;
import fr.theobosse.roguelike.tools.Configs;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class WeaponCommand implements CommandExecutor {

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

            p.getInventory().addItem(wp.getItem());
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

            ((Player) commandSender).getInventory().addItem(wp.getItem());
        }
        return true;
    }

}
