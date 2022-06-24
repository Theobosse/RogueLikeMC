package fr.theobosse.roguelike.game;

import fr.theobosse.roguelike.tools.Configs;
import fr.theobosse.roguelike.tools.ItemBuilder;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Weapon {

    public static final List<Weapon> weapons = new ArrayList<>();

    private final ConfigurationSection section;
    private final String name;
    private final String description;
    private final int damage;
    private final ItemStack item;
    private final String itemClass;
    private final double speed;
    private final double attackSpeed;
    private final ArrayList<String> lore;
    private final String id;

    public Weapon(ConfigurationSection section) {
        this.section = section;
        this.id = section.getCurrentPath();
        this.name = section.getString("name");
        this.description = section.getString("description");
        this.itemClass = section.getString("class");
        this.damage = section.getInt("damage");
        this.speed = section.getDouble("speed");
        this.attackSpeed = section.getDouble("attack-speed");

        ItemBuilder ib = new ItemBuilder(Objects.requireNonNull(section.getConfigurationSection("item")));

        this.lore = new ArrayList<>(section.getStringList("lore"));
        this.lore.add("§a");

        if (damage != 0) {
            this.lore.add("§c§lDEGATS : §c" + damage);
            ib.setDamage(damage);
        } if (speed != 0) {
            this.lore.add("§b§lVITESSE : §b" + speed);
            ib.setSpeed(speed);
        } if (attackSpeed != 0) {
            this.lore.add("§-§lVITESSE D'ATTAQUE : §6" + attackSpeed);
            ib.setAttackSpeed(attackSpeed);
        }

        this.item = ib.setLore(lore).getItem();
    }

    public static Weapon getWeapon(String id) {
        for (Weapon wp : weapons)
            if (wp.id.equals(id))
                return wp;
        return null;
    }

    public static void load() {
        YamlConfiguration config = Configs.getConfig("weapons");
        for (String id : config.getKeys(false))
            weapons.add(new Weapon(Objects.requireNonNull(config.getConfigurationSection(id))));
    }


    public String getItemClass() {
        return itemClass;
    }

    public int getDamage() {
        return damage;
    }

    public ConfigurationSection getSection() {
        return section;
    }

    public ItemStack getItem() {
        return item;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }
}
