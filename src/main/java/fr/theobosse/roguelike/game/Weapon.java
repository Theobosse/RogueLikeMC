package fr.theobosse.roguelike.game;

import fr.theobosse.roguelike.RogueLike;
import fr.theobosse.roguelike.tools.Configs;
import fr.theobosse.roguelike.tools.ItemBuilder;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Weapon {

    public static final List<Weapon> weapons = new ArrayList<>();

    private final ConfigurationSection section;
    private final String id;
    private int durability;
    private String name;
    private String description;
    private int damage;
    private String itemClass;
    private double speed;
    private double attackSpeed;
    private ArrayList<String> lore;

    public Weapon(ConfigurationSection section) {
        this.section = section;
        this.id = section.getCurrentPath();
        this.name = section.getString("name");
        this.description = section.getString("description");
        this.itemClass = section.getString("class");
        this.damage = section.getInt("damage");
        this.speed = section.getDouble("speed");
        this.attackSpeed = section.getDouble("attack-speed");
        this.durability = section.getInt("durability");
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


    public ItemStack build() {
        ItemBuilder ib = new ItemBuilder(Objects.requireNonNull(section.getConfigurationSection("item")));
        ib.setName(ib.getName() + " §6§l>> §e" + durability);
        ib.setUnbreakable(true);
        lore = new ArrayList<>();
        lore.add("§a");

        if (damage != 0) {
            lore.add("§c§lDEGATS : §c" + damage);
            ib.setDamage(damage);
        } if (speed != 0) {
            lore.add("§b§lVITESSE : §b" + speed);
            ib.setSpeed(speed * 0.2);
        } if (attackSpeed != 0) {
            lore.add("§6§lVITESSE D'ATTAQUE : §6" + attackSpeed);
            ib.setAttackSpeed(attackSpeed);
        }

        ib.getFlags().add(ItemFlag.HIDE_ATTRIBUTES);
        ib.getFlags().add(ItemFlag.HIDE_UNBREAKABLE);
        ib.getLore().addAll(lore);
        ItemStack item = ib.getItem();

        ItemMeta im = item.getItemMeta();
        PersistentDataContainer container = im.getPersistentDataContainer();
        container.set(new NamespacedKey(RogueLike.instance, "name"), PersistentDataType.STRING, id);
        container.set(new NamespacedKey(RogueLike.instance, "durability"), PersistentDataType.INTEGER, durability);
        item.setItemMeta(im);
        return item;
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

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public ArrayList<String> getLore() {
        return lore;
    }

    public double getAttackSpeed() {
        return attackSpeed;
    }

    public double getSpeed() {
        return speed;
    }

    public int getDurability() {
        return durability;
    }

    public String getId() {
        return id;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public void setAttackSpeed(double attackSpeed) {
        this.attackSpeed = attackSpeed;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setItemClass(String itemClass) {
        this.itemClass = itemClass;
    }

    public void setLore(ArrayList<String> lore) {
        this.lore = lore;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDurability(int durability) {
        this.durability = durability;
    }
}
