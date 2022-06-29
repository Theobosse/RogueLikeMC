package fr.theobosse.roguelike.game;

import fr.theobosse.roguelike.RogueLike;
import fr.theobosse.roguelike.tools.Configs;
import fr.theobosse.roguelike.tools.ItemBuilder;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Zombie;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;


public class Ammo {
    private final String id;
    private final ConfigurationSection section;
    private int value;

    public Ammo(ConfigurationSection section) {
        this.section = section;
        this.id = section.getCurrentPath();
        this.value = 10;
    }
    public static ItemStack getItem(int amount) {
        ItemStack is = new ItemStack(Material.ARROW);
        ItemMeta im = is.getItemMeta();

        // CUSTOMIZE AMMO
        im.setDisplayName("§5Munitions");
        im.setLore(Arrays.asList(" §e>> §6Quantité§e: §a§l" + amount));

        PersistentDataContainer data = im.getPersistentDataContainer();
        data.set(new NamespacedKey(RogueLike.instance, "amount"), PersistentDataType.INTEGER, amount);

        is.setItemMeta(im);
        return is;
    }
}
