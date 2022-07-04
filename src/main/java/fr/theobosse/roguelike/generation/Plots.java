package fr.theobosse.roguelike.generation;

import fr.theobosse.roguelike.game.Enemy;
import fr.theobosse.roguelike.tools.Configs;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class Plots {

    private static ArrayList<Plots> plots = new ArrayList<>();
    private final ConfigurationSection section;
    private final Location location;
    private final Shape shape;
    private final String id;

    public class Shape {

        private ConfigurationSection section;
        private final ShapeType type;
        private final int length;
        private final int width;

        private Shape(ConfigurationSection section) {
            this.section = section;
            this.type = ShapeType.getById(section.getString("type"));
            this.length = section.getInt("length");
            this.width = section.getInt("width");
        }

        private Shape(String type, int length, int width) {
            this.type = ShapeType.getById(type);
            this.length = length;
            this.width = width;
        }

        public ConfigurationSection getSection() {
            return section;
        }

        public ShapeType getType() {
            return type;
        }

        public int getLength() {
            return length;
        }

        public int getWidth() {
            return width;
        }
    }

    private enum ShapeType {
        L_SHAPE("L"),
        SQUARE("S"),
        RECT("R");

        private final String id;

        ShapeType(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }

        public static ShapeType getById(String id) {
            for (ShapeType s : ShapeType.values())
                if (s.getId().equals(id))
                    return s;
            return null;
        }
    }

    public Plots(String id, Location loc, String type, int length, int width) {
        loc = loc.getBlock().getLocation();
        YamlConfiguration config = Configs.getConfig("plots");
        config.set(id + ".world", loc.getWorld().getName());
        config.set(id + ".x", loc.getX());
        config.set(id + ".y", loc.getY() - 1);
        config.set(id + ".z", loc.getZ());

        config.set(id + ".shape.type", type);
        config.set(id + ".shape.length", length);
        config.set(id + ".shape.width", width);

        try {
            config.save(Configs.getFile("plots"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        this.section = config.getConfigurationSection(id);
        this.shape = new Shape(type, length, width);
        this.location = loc;
        this.id = id;

        if (shape.getType().equals(ShapeType.SQUARE)) {
            for (int i = 0; i < length; i++) {
                for (int j = 0; j < length; j++) {
                    if (i == 0 || j == 0 || i == (length - 1) || j == (length - 1))
                        loc.clone().add(i, -1, j).getBlock().setType(Material.PURPLE_TERRACOTTA);
                }
            }
        }
        plots.add(this);
    }

    public Plots(ConfigurationSection section) {
        this.shape = new Shape(Objects.requireNonNull(section.getConfigurationSection("shape")));
        this.id = section.getCurrentPath();
        this.section = section;

        this.location = new Location(
                Bukkit.getWorld(section.getString("world")),
                section.getInt("x"),
                section.getInt("y"),
                section.getInt("z")
        );
    }

    public static Plots getMob(String id) {
        for (Plots p : plots)
            if (p.getId().equals(id))
                return p;
        return null;
    }

    public static void load() {
        YamlConfiguration config = Configs.getConfig("plots");
        for (String id : config.getKeys(false))
            plots.add(new Plots(Objects.requireNonNull(config.getConfigurationSection(id))));
    }

    public ConfigurationSection getSection() {
        return section;
    }

    public Location getLocation() {
        return location;
    }

    public Shape getShape() {
        return shape;
    }

    public String getId() {
        return id;
    }
}
