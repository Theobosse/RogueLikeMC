package fr.theobosse.roguelike;

import fr.theobosse.roguelike.commands.AmmoCommand;
import fr.theobosse.roguelike.commands.GenerationCommand;
import fr.theobosse.roguelike.commands.LootCrateCommand;
import fr.theobosse.roguelike.commands.MobCommand;
import fr.theobosse.roguelike.commands.WeaponCommand;
import fr.theobosse.roguelike.events.AmmoEvent;
import fr.theobosse.roguelike.events.EnemiesEvent;
import fr.theobosse.roguelike.events.LootCrateEvent;
import fr.theobosse.roguelike.events.WeaponEvent;
import fr.theobosse.roguelike.game.Enemy;
import fr.theobosse.roguelike.game.LootCrate;
import fr.theobosse.roguelike.game.Role;
import fr.theobosse.roguelike.game.Weapon;
import fr.theobosse.roguelike.tools.Configs;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

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
        Configs.register("loots");
        Configs.load();

        // LOAD CLASSES
        LootCrate.load();
        Weapon.load();
        Enemy.load();
        Role.load();

        // COMMANDS
        getCommand("lootcrate").setExecutor(new LootCrateCommand());
        getCommand("gen").setExecutor(new GenerationCommand());
        getCommand("weapon").setExecutor(new WeaponCommand());
        getCommand("ammo").setExecutor(new AmmoCommand());
        getCommand("mob").setExecutor(new MobCommand());

        // Autocompletion
        getCommand("lootcrate").setTabCompleter(new LootCrateCommand());
        getCommand("weapon").setTabCompleter(new WeaponCommand());
        getCommand("mob").setTabCompleter(new MobCommand());

        // EVENTS
        pm.registerEvents(new LootCrateEvent(), this);
        pm.registerEvents(new EnemiesEvent(), this);
        pm.registerEvents(new AmmoEvent(), this);
        pm.registerEvents(new WeaponEvent(), this);
    }
}
