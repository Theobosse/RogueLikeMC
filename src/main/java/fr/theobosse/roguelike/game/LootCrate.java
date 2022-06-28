package fr.theobosse.roguelike.game;

import fr.theobosse.roguelike.RogueLike;
import fr.theobosse.roguelike.tools.Configs;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Barrel;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_18_R2.block.impl.CraftBarrel;
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

    public Barrel summon(Location loc){
        loc.getBlock().setType(Material.BARREL);
        ArmorStand as = (ArmorStand) loc.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
        as.setCustomName(name);
        as.setCustomNameVisible(true);
        as.setVisible(false);
        as.setGravity(false);
        UUID uuid = as.getUniqueId();

        Barrel b = (Barrel) loc.getBlock().getState();
        PersistentDataContainer data = b.getPersistentDataContainer();
        data.set(new NamespacedKey(RogueLike.instance, "id"), PersistentDataType.STRING, id);
        data.set(new NamespacedKey(RogueLike.instance, "asuuid"), PersistentDataType.STRING, uuid.toString());
        return b;
    }
}
