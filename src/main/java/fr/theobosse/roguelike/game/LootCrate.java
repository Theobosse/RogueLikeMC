package fr.theobosse.roguelike.game;

import fr.theobosse.roguelike.RogueLike;
import fr.theobosse.roguelike.tools.Configs;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.TileState;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class LootCrate {
    private final ConfigurationSection section;
    private final String id;
    private final String name;

    public static ArrayList<LootCrate> lootCreates = new ArrayList<>();

    public LootCrate(ConfigurationSection section){
        this.id = section.getCurrentPath();
        this.section = section;
        this.name = section.getString("name");
    }

    public static void load() {
        YamlConfiguration config = Configs.getConfig("loots");
        for (String id : config.getKeys(false))
            lootCreates.add(new LootCrate(Objects.requireNonNull(config.getConfigurationSection(id))));
    }

    public static LootCrate getLootCrate(String id) {
        for (LootCrate lc : lootCreates)
            if (lc.getId().equals(id)) return lc;
        return null;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ConfigurationSection getSection() {
        return section;
    }

    public TileState summon(Location loc){
        loc.getBlock().setType(Material.BARREL);

        TileState b = (TileState) loc.getBlock().getState();

        ArmorStand as = (ArmorStand) loc.getWorld().spawnEntity(b.getLocation().clone().add(0.5, 1, 0.5), EntityType.ARMOR_STAND);
        as.setVisible(false);
        as.setCustomNameVisible(true);
        as.setCustomName(name);
        as.setGravity(false);
        as.setMarker(true);

        UUID uuid = as.getUniqueId();


        PersistentDataContainer data = b.getPersistentDataContainer();
        data.set(new NamespacedKey(RogueLike.instance, "id"), PersistentDataType.STRING, id);
        data.set(new NamespacedKey(RogueLike.instance, "as-uuid"), PersistentDataType.STRING, uuid.toString());
        b.update();
        return b;
    }
}
