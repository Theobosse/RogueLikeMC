package fr.theobosse.roguelike;

import fr.theobosse.roguelike.commands.GenerationCommand;
import fr.theobosse.roguelike.commands.LootCrateCommand;
import fr.theobosse.roguelike.commands.MobCommand;
import fr.theobosse.roguelike.commands.WeaponCommand;
import fr.theobosse.roguelike.events.DurabilityEvent;
import fr.theobosse.roguelike.events.EnemiesEvent;
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
        getCommand("gen").setExecutor(new GenerationCommand());
        getCommand("weapon").setExecutor(new WeaponCommand());
        getCommand("mob").setExecutor(new MobCommand());
        getCommand("lootcrate").setExecutor(new LootCrateCommand());

        // Autocompletion
        getCommand("weapon").setTabCompleter(new WeaponCommand());
        getCommand("mob").setTabCompleter(new MobCommand());
        getCommand("lootcrate").setTabCompleter(new LootCrateCommand());

        // EVENTS
        pm.registerEvents(new DurabilityEvent(), this);
        pm.registerEvents(new EnemiesEvent(), this);
    }
}
