package fr.theobosse.roguelike.game;

import fr.theobosse.roguelike.RogueLike;
import fr.theobosse.roguelike.tools.Configs;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.Objects;

public class Enemy {

    public static ArrayList<Enemy> enemies = new ArrayList<>();

    private final double life;
    private final int damage;
    private final double speed;
    private final String name;
    private final int xp;
    private final String id;
    private final String entityType;


    public Enemy(ConfigurationSection section) {
        this.id = section.getCurrentPath();
        this.life = section.getInt("hp");
        this.damage = section.getInt("damage");
        this.speed = section.getDouble("speed");
        this.name = section.getString("name");
        this.xp = section.getInt("xp");
        this.entityType = section.getString("type");
    }

    public static Enemy getMob(String id) {
        for (Enemy e : enemies)
            if (e.id.equals(id))
                return e;
        return null;
    }

    public static void load() {
        YamlConfiguration config = Configs.getConfig("mobs");
        for (String id : config.getKeys(false))
            enemies.add(new Enemy(Objects.requireNonNull(config.getConfigurationSection(id))));
    }

    public Entity spawn(Location loc) {
        Mob e = (Mob) loc.getWorld().spawnEntity(loc, EntityType.fromName(entityType));
        e.setCustomName(name);
        PersistentDataContainer data = e.getPersistentDataContainer();
        data.set(new NamespacedKey(RogueLike.instance, "life"), PersistentDataType.DOUBLE, life);
        data.set(new NamespacedKey(RogueLike.instance, "id"), PersistentDataType.STRING, id);

        e.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(damage);
        e.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(speed * 0.2);
        return e;
    }
}
