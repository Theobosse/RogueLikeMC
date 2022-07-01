package fr.theobosse.roguelike.tools;

import fr.theobosse.roguelike.RogueLike;
import fr.theobosse.roguelike.game.Enemy;
import fr.theobosse.roguelike.game.LootCrate;
import fr.theobosse.roguelike.game.Weapon;
import org.bukkit.NamespacedKey;
import org.bukkit.block.TileState;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class DataManager {

    private PersistentDataContainer container;
    private ItemStack is;
    private ItemMeta im;

    public DataManager(ItemStack is) {
        this.is = is;
        this.im = is.getItemMeta();

        if (im == null) return;
        this.container = im.getPersistentDataContainer();
    }

    public DataManager(TileState tile) {
        this.container = tile.getPersistentDataContainer();
    }

    public DataManager(Entity entity) {
        this.container = entity.getPersistentDataContainer();
    }

    public <T, Z> void set(String data, PersistentDataType<T, Z> type, Z value) {
        if (container == null) return;
        if (contains(data, type)) {
            container.set(new NamespacedKey(RogueLike.instance, data), type, value);
            if (is != null) is.setItemMeta(im);
        }
    }

    public <T, Z> boolean contains(String data, PersistentDataType<T, Z> type) {
        if (container == null) return false;
        return container.has(new NamespacedKey(RogueLike.instance, data), type);
    }

    public <T, Z> Z get(String data, PersistentDataType<T, Z> type) {
        if (container == null) return null;
        if (contains(data, type))
            return container.get(new NamespacedKey(RogueLike.instance, data), type);
        return null;
    }

    public <T, Z> void add(String data, double value) {
        if (contains(data, PersistentDataType.INTEGER))
            set(data, PersistentDataType.INTEGER, get(data, PersistentDataType.INTEGER) + (int) value);
        else if (contains(data, PersistentDataType.FLOAT))
            set(data, PersistentDataType.FLOAT, get(data, PersistentDataType.FLOAT) + (float) value);
        else set(data, PersistentDataType.DOUBLE, get(data, PersistentDataType.DOUBLE) + value);
    }

    public <T, Z> void sub(String data, double value) {
        if (contains(data, PersistentDataType.INTEGER))
            set(data, PersistentDataType.INTEGER, get(data, PersistentDataType.INTEGER) - (int) value);
        else if (contains(data, PersistentDataType.FLOAT))
            set(data, PersistentDataType.FLOAT, get(data, PersistentDataType.FLOAT) - (float) value);
        else set(data, PersistentDataType.DOUBLE, get(data, PersistentDataType.DOUBLE) - value);
    }

    public Weapon getWeapon() {
        return Weapon.getWeapon(get("id", PersistentDataType.STRING));
    }

    public Enemy getEnemy() {
        return Enemy.getMob(get("id", PersistentDataType.STRING));
    }

    public LootCrate getLootCrate() {
        return LootCrate.getLootCrate(get("id", PersistentDataType.STRING));
    }

}
