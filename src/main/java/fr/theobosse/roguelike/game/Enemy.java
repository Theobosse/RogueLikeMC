package fr.theobosse.roguelike.game;

import fr.theobosse.roguelike.RogueLike;
import fr.theobosse.roguelike.events.LootCrateEvent;
import fr.theobosse.roguelike.tools.Configs;
import fr.theobosse.roguelike.tools.ItemBuilder;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Zombie;
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
    private final ConfigurationSection section;


    public Enemy(ConfigurationSection section) {
        this.id = section.getCurrentPath();
        this.life = section.getInt("hp");
        this.damage = section.getInt("damage");
        this.speed = section.getDouble("speed");
        this.name = section.getString("name");
        this.xp = section.getInt("xp");
        this.entityType = section.getString("type");
        this.section = section;
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
        e.setCustomName(name + " §c[" + Math.round(life) + "]");
        PersistentDataContainer data = e.getPersistentDataContainer();
        data.set(new NamespacedKey(RogueLike.instance, "life"), PersistentDataType.DOUBLE, life);
        data.set(new NamespacedKey(RogueLike.instance, "id"), PersistentDataType.STRING, id);

        e.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(damage);
        e.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(speed * 0.2);

        if (section.contains("equipment.helmet"))
            e.getEquipment().setHelmet(LootCrateEvent.getItem(section, "equipment.helmet"));
        if (section.contains("equipment.chestplate"))
            e.getEquipment().setChestplate(LootCrateEvent.getItem(section, "equipment.chestplate"));
        if (section.contains("equipment.leggings"))
            e.getEquipment().setLeggings(LootCrateEvent.getItem(section, "equipment.leggings"));
        if (section.contains("equipment.boots"))
            e.getEquipment().setBoots(LootCrateEvent.getItem(section, "equipment.boots"));
        if (section.contains("equipment.main"))
            e.getEquipment().setItemInMainHand(LootCrateEvent.getItem(section, "equipment.main"));
        if (section.contains("equipment.off"))
            e.getEquipment().setItemInOffHand(LootCrateEvent.getItem(section, "equipment.off"));


        return e;
    }

    public ConfigurationSection getSection() {
        return section;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public double getSpeed() {
        return speed;
    }

    public int getDamage() {
        return damage;
    }

    public double getLife() {
        return life;
    }

    public int getXp() {
        return xp;
    }

    public String getEntityType() {
        return entityType;
    }

}
