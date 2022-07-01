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

    public static ItemStack getItem(int amount) {
        ItemStack is = new ItemStack(Material.ARROW, Math.min(amount, 65));
        ItemMeta im = is.getItemMeta();

        // CUSTOMIZE AMMO
        im.setDisplayName("§5Munitions §e(§a§l" + amount + "§e)");
        im.setLore(Arrays.asList(" §e>> §6Clique droit sur l'arme que vous souhaitez recharger"));

        PersistentDataContainer data = im.getPersistentDataContainer();
        data.set(new NamespacedKey(RogueLike.instance, "amount"), PersistentDataType.INTEGER, amount);

        is.setItemMeta(im);
        return is;
    }
}
