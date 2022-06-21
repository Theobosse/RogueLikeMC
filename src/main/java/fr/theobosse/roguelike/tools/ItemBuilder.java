package fr.theobosse.roguelike.tools;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.*;

public class ItemBuilder {
    private HashMap<Enchantment, Integer> enchants;
    private final ConfigurationSection section;
    private List<ItemFlag> flags;
    private boolean unbreakable;
    private List<String> lore;
    private Material type;
    private String name;
    private int amount;
    private short dura;
    private int damage;
    private double attackSpeed;
    private double speed;

    public ItemBuilder(ConfigurationSection section) {
        assert section != null;
        this.section = section;
        lore = new ArrayList<>();
        flags = new ArrayList<>();
        enchants = new HashMap<>();
        damage = -1;
        attackSpeed = 0;
        speed = 0;
        amount = 1;
        dura = 0;

        if (section.contains("type"))
            type = Material.getMaterial(Objects.requireNonNull(section.getString("type")));

        if (section.contains("name"))
            name = section.getString("name");

        if (section.contains("amount"))
            amount = section.getInt("amount");

        if (section.contains("lore"))
            lore.addAll(section.getStringList("lore"));

        if (section.contains("flags"))
            for (String f : section.getStringList("flags")) {
                flags.add(ItemFlag.valueOf(f));
            }

        if (section.contains("enchantments")) {
            for (String enchant : Objects.requireNonNull(section.getConfigurationSection("enchantments")).getKeys(false)) {
                int level = section.getInt("enchantments." + enchant);
                enchants.put(Enchantment.getByName(enchant), level);
            }
        }

        if (section.contains("unbreakable"))
            unbreakable = section.getBoolean("unbreakable");

        if (section.contains("durability"))
            dura = (short) section.getInt("durability");
    }

    public ItemStack getItem() {
        ItemStack item;
        if (type != null)
            item = new ItemStack(type);
        else item = new ItemStack(Material.BARRIER);
        item.setDurability(dura);
        item.setAmount(amount);
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setUnbreakable(unbreakable);
        meta.setDisplayName(name);
        meta.setLore(lore);

        if (damage != -1) {
            AttributeModifier modifier = new AttributeModifier(UUID.randomUUID(), "Damage", damage, AttributeModifier.Operation.ADD_NUMBER);
            meta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, modifier);
        }

        if (attackSpeed > 0) {
            AttributeModifier modifier = new AttributeModifier(UUID.randomUUID(), "Attack Speed", attackSpeed, AttributeModifier.Operation.ADD_NUMBER);
            meta.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, modifier);
        }

        if (speed > 0) {
            AttributeModifier modifier = new AttributeModifier(UUID.randomUUID(), "Speed", speed, AttributeModifier.Operation.ADD_NUMBER);
            meta.addAttributeModifier(Attribute.GENERIC_MOVEMENT_SPEED, modifier);
        }

        for (Map.Entry<Enchantment, Integer> entry : enchants.entrySet()) {
            Enchantment enchantment = entry.getKey();
            Integer level = entry.getValue();
            meta.addEnchant(enchantment, level, true);
        }

        for (ItemFlag flag : flags)
            meta.addItemFlags(flag);

        if (section.contains("Skull")) {
            if (type == Material.PLAYER_HEAD) {
                ((SkullMeta) meta).setOwner(section.getString("Skull"));
            }
        }

        if (section.contains("Color")) {
            if (type == Material.LEATHER_BOOTS || type == Material.LEATHER_CHESTPLATE || type == Material.LEATHER_HELMET || type == Material.LEATHER_LEGGINGS) {
                if (section.contains("Color.R") && section.contains("Color.G") && section.contains("Color.B")) {
                    ((LeatherArmorMeta) meta).setColor(Color.fromBGR(section.getInt("Color.B"), section.getInt("Color.G"), section.getInt("Color.R")));
                }
            }
        }

        item.setItemMeta(meta);
        return item;
    }

    public String getName() {
        return name;
    }

    public Material getType() {
        return type;
    }

    public int getAmount() {
        return amount;
    }

    public short getDurability() {
        return dura;
    }

    public HashMap<Enchantment, Integer> getEnchants() {
        return enchants;
    }

    public List<ItemFlag> getFlags() {
        return flags;
    }

    public List<String> getLore() {
        return lore;
    }

    public double getAttackSpeed() {
        return attackSpeed;
    }

    public int getDamage() {
        return damage;
    }

    public double getSpeed() {
        return speed;
    }

    public boolean isUnbreakable() {
        return unbreakable;
    }



    public void setType(Material type) {
        this.type = type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setQuantity(int quantity) {
        this.amount = quantity;
    }

    public void setDurability(short dura) {
        this.dura = dura;
    }

    public void setLore(List<String> lore) {
        this.lore = lore;
    }

    public void setEnchants(HashMap<Enchantment, Integer> enchants) {
        this.enchants = enchants;
    }

    public void setFlags(List<ItemFlag> flags) {
        this.flags = flags;
    }

    public void setAttackSpeed(double attackSpeed) {
        this.attackSpeed = attackSpeed;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public void setUnbreakable(boolean unbreakable) {
        this.unbreakable = unbreakable;
    }
}
