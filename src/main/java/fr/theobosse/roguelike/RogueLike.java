package fr.theobosse.roguelike;

import fr.theobosse.roguelike.commands.GenerationCommand;
import fr.theobosse.roguelike.commands.WeaponCommand;
import fr.theobosse.roguelike.game.Role;
import fr.theobosse.roguelike.game.Weapon;
import fr.theobosse.roguelike.tools.Configs;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class RogueLike extends JavaPlugin {

    public static RogueLike instance;

    @Override
    public void onEnable() {
        instance = this;

        // CONFIGS
        Configs.register("roles");
        Configs.register("weapons");
        Configs.load();

        Weapon.load();
        Role.load();

        // COMMANDS
        getCommand("gen").setExecutor(new GenerationCommand());
        getCommand("weapon").setExecutor(new WeaponCommand());
    }
}
