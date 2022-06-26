package fr.theobosse.roguelike.game;

import fr.theobosse.roguelike.tools.Configs;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Role {

    private static final List<Role> roles = new ArrayList<>();

    private final ConfigurationSection section;
    private final String name;
    private final String description;
    private final int health;
    private final double speed;
    private final String itemClass;

    public Role(ConfigurationSection section) {
        this.section = section;
        this.name = section.getString("name");
        this.description = section.getString("description");
        this.health = section.getInt("health");
        this.speed = section.getDouble("speed");
        this.itemClass = section.getString("class");
    }

    public ConfigurationSection getSection() {
        return section;
    }

    public double getSpeed() {
        return speed;
    }

    public int getHealth() {
        return health;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public String getItemClass() {
        return itemClass;
    }

    public static void load() {
        ConfigurationSection section = Configs.getConfig("roles");
        for (String key : section.getKeys(false)) {
            roles.add(new Role(Objects.requireNonNull(section.getConfigurationSection(key))));
        }
    }

    public static List<Role> getRoles() {
        return roles;
    }

    public static Role getRole(String name) {
        for (Role role : roles) {
            if (role.getName().equals(name)) {
                return role;
            }
        }
        return null;
    }
}
