package fr.theobosse.roguelike.events;

import fr.theobosse.roguelike.RogueLike;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class AmmoReload implements Listener {

    @EventHandler
    public void onInvClick(InventoryClickEvent event) {
        if (!event.getClick().isRightClick()) return;
        if (!(event.getWhoClicked() instanceof Player)) return;
        Player player = (Player) event.getWhoClicked();
        ItemStack ammo = event.getCursor();
        ItemStack wp = event.getCurrentItem();
        if (ammo == null || wp == null || ammo.getItemMeta() == null || wp.getItemMeta() == null) return;

        PersistentDataContainer ammoData = ammo.getItemMeta().getPersistentDataContainer();
        PersistentDataContainer wpData = wp.getItemMeta().getPersistentDataContainer();
        NamespacedKey ammoKey = new NamespacedKey(RogueLike.instance, "amount");
        NamespacedKey wpKey = new NamespacedKey(RogueLike.instance, "ammo");
        NamespacedKey mwpKey = new NamespacedKey(RogueLike.instance, "maxAmmo");

        if (ammoData.has(ammoKey, PersistentDataType.INTEGER) && wpData.has(wpKey, PersistentDataType.INTEGER) && wpData.has(mwpKey, PersistentDataType.INTEGER)) {
            int weaponAmmo = wpData.get(wpKey, PersistentDataType.INTEGER);
            int maxWeaponAmmo = wpData.get(mwpKey, PersistentDataType.INTEGER);
            int ammoAmount = ammoData.get(ammoKey, PersistentDataType.INTEGER);
            int newAmmo = Math.min(maxWeaponAmmo, weaponAmmo + ammoAmount);

            wp.getItemMeta().setDisplayName(wp.getItemMeta().getDisplayName().split(" §6§l>> §e")[0] + " §6§l>> §e" + newAmmo + " §6/ §e" + maxWeaponAmmo);
            player.playSound(player, Sound.ENTITY_PLAYER_LEVELUP, 6, 3);
        }
    }

}
