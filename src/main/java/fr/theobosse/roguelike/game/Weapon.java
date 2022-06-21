package fr.theobosse.roguelike.game;

import fr.theobosse.roguelike.tools.ItemBuilder;
import org.bukkit.configuration.ConfigurationSection;
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

    public Weapon(ConfigurationSection section) {
        this.section = section;
        this.name = section.getString("name");
        this.description = section.getString("description");
        this.damage = section.getInt("damage");
        this.itemClass = section.getString("class");
        this.item = new ItemBuilder(Objects.requireNonNull(section.getConfigurationSection("item"))).getItem();
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
