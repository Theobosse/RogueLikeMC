package fr.theobosse.roguelike.game;

import fr.theobosse.roguelike.RogueLike;
import fr.theobosse.roguelike.tools.Configs;
import net.minecraft.world.level.block.entity.TileEntity;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_18_R2.block.CraftBarrel;
import org.bukkit.craftbukkit.v1_18_R2.block.CraftBlockState;
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

    public TileState summon(Location loc){
        loc.getBlock().setType(Material.BARREL);
        ArmorStand as = (ArmorStand) loc.getWorld().spawnEntity(loc.clone().add(0, 1, 0), EntityType.ARMOR_STAND);
        as.setCustomNameVisible(true);
        as.setCustomName(name);
        as.setVisible(false);
        as.setGravity(false);
        as.setMarker(true);

        UUID uuid = as.getUniqueId();

        TileState b = (TileState) loc.getBlock().getState();
        PersistentDataContainer data = b.getPersistentDataContainer();
        data.set(new NamespacedKey(RogueLike.instance, "id"), PersistentDataType.STRING, id);
        data.set(new NamespacedKey(RogueLike.instance, "as-uuid"), PersistentDataType.STRING, uuid.toString());
        return b;
    }
}
