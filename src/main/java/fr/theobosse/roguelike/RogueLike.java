package fr.theobosse.roguelike;

import fr.theobosse.roguelike.commands.GenerationCommand;
import fr.theobosse.roguelike.commands.MobCommand;
import fr.theobosse.roguelike.commands.WeaponCommand;
import fr.theobosse.roguelike.events.DurabilityEvent;
import fr.theobosse.roguelike.events.EnemiesEvent;
import fr.theobosse.roguelike.game.Enemy;
import fr.theobosse.roguelike.game.Role;
import fr.theobosse.roguelike.game.Weapon;
import fr.theobosse.roguelike.tools.Configs;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class RogueLike extends JavaPlugin {

    public static RogueLike instance;

    @Override
    public void onEnable() {
        instance = this;
        PluginManager pm = getServer().getPluginManager();

        // CONFIGS
        Configs.register("roles");
        Configs.register("weapons");
        Configs.register("mobs");

        // LOAD CLASSES
        Configs.load();
        Weapon.load();
        Enemy.load();
        Role.load();

        // COMMANDS
        getCommand("gen").setExecutor(new GenerationCommand());
        getCommand("weapon").setExecutor(new WeaponCommand());
        getCommand("mob").setExecutor(new MobCommand());

        // EVENTS
        pm.registerEvents(new DurabilityEvent(), this);
        pm.registerEvents(new EnemiesEvent(), this);
    }
}
