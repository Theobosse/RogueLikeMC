package fr.theobosse.roguelike.game;

import fr.theobosse.roguelike.RogueLike;
import fr.theobosse.roguelike.tools.Configs;
import fr.theobosse.roguelike.tools.ItemBuilder;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
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
    private int ammo;
    private int maxAmmo;
    private String name;
    private String description;
    private int damage;
    private String itemClass;
    private double speed;
    private double attackSpeed;
    private ArrayList<String> lore;

    // Projectiles
    private String projectile;
    private double projectileSpeed;
    private double cooldown;

    private int isFire;
    private int fireLen;

    public Weapon(ConfigurationSection section) {
        ItemBuilder ib = new ItemBuilder(Objects.requireNonNull(section.getConfigurationSection("item")));
        this.section = section;
        this.id = section.getCurrentPath();
        this.name = ib.getName();
        this.description = section.getString("description");
        this.itemClass = section.getString("class");
        this.damage = section.getInt("damage");
        this.speed = section.getDouble("speed");
        this.attackSpeed = section.getDouble("attack-speed");
        this.ammo = section.getInt("ammo");
        this.maxAmmo = section.getInt("ammo");
        this.cooldown = section.getInt("cooldown");

        // Fire
        this.isFire = section.getInt("is-fire");
        this.fireLen = section.getInt("fire-len");

        // Projectiles
        this.projectile = section.getString("projectile");
        this.projectileSpeed = section.getDouble("projectile-speed");
        this.ammo = section.getInt("ammo");
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
        setName(ib.getName());
        ib.setName(ib.getName() + " ??6??l>> ??e" + ammo);
        ib.setUnbreakable(true);
        lore = new ArrayList<>();
        lore.add("??a");

        if (damage != 0) {
            lore.add("??c??lDEGATS : ??c" + damage);
            ib.setDamage(damage);
        } if (speed != 0) {
            lore.add("??b??lVITESSE : ??b" + speed);
            ib.setSpeed(speed * 0.2);
        } if (attackSpeed != 0) {
            lore.add("??6??lVITESSE D'ATTAQUE : ??6" + attackSpeed);
            ib.setAttackSpeed(attackSpeed);
        }

        ib.getFlags().add(ItemFlag.HIDE_ATTRIBUTES);
        ib.getFlags().add(ItemFlag.HIDE_UNBREAKABLE);
        ib.getLore().addAll(lore);
        ItemStack item = ib.getItem();

        ItemMeta im = item.getItemMeta();
        PersistentDataContainer container = im.getPersistentDataContainer();
        container.set(new NamespacedKey(RogueLike.instance, "id"), PersistentDataType.STRING, id);
        container.set(new NamespacedKey(RogueLike.instance, "ammo"), PersistentDataType.INTEGER, ammo);
        item.setItemMeta(im);
        return item;
    }

    public String getItemClass() {
        return itemClass;
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

    public int getAmmo() {
        return ammo;
    }

    public String getId() {
        return id;
    }

    public String getProjectile() { 
        return projectile; 
    }

    public double getCooldown() {
        return cooldown;
    }

    public double getProjectileSpeed() {
        return projectileSpeed;
    }

    /// Setters

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

    public void setAmmo(int ammo) {
        this.ammo = ammo;
    }

    public void setProjectile(String proj) {
        this.projectile = proj;
    }

    public void setCooldown(int cooldown) {
        this.cooldown = cooldown;
    }

    public void setProjectileSpeed(double projectileSpeed) {
        this.projectileSpeed = projectileSpeed;
    }

    public int getFireLen() {
        return fireLen;
    }

    public int getIsFire() {
        return isFire;
    }
}
