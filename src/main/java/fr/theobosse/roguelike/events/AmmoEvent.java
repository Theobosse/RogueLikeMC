package fr.theobosse.roguelike.events;

import fr.theobosse.roguelike.RogueLike;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class AmmoEvent implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void hitEvent(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) return;
        Player player = (Player) event.getDamager();
        ItemStack is = player.getItemInHand();
        ItemMeta im = is.getItemMeta();
        if (im == null) return;

        PersistentDataContainer container = im.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(RogueLike.instance, "ammo");
        NamespacedKey mKey = new NamespacedKey(RogueLike.instance, "maxAmmo");

        if (container.has(key, PersistentDataType.INTEGER)) {
            int ammo = container.get(key, PersistentDataType.INTEGER);
            int maxAmmo = container.get(mKey, PersistentDataType.INTEGER);

            if (ammo > 0) {
                container.set(key, PersistentDataType.INTEGER, ammo - 1);
                if (ammo == 1) {
                    im.setDisplayName(im.getDisplayName().split(" §6§l>> §e")[0] + " §6§l>> §4§lCASSÉ");
                    player.playSound(player, Sound.ENTITY_ITEM_BREAK, SoundCategory.PLAYERS, 100, 3);
                } else {
                    im.setDisplayName(im.getDisplayName().split(" §6§l>> §e")[0] + " §6§l>> §e" + (ammo - 1) + " §6/ §e" + maxAmmo);
                }
            } else {
                player.playSound(player, Sound.ENTITY_VILLAGER_NO, SoundCategory.PLAYERS, 10, 2);
                event.setCancelled(true);
            }
        }
        is.setItemMeta(im);
    }

    @EventHandler
    public void onInvClick(InventoryClickEvent event) {
        if (!event.getClick().isRightClick()) return;
        if (!(event.getWhoClicked() instanceof Player)) return;
        Player player = (Player) event.getWhoClicked();
        ItemStack wp = event.getCurrentItem();
        ItemStack ammo = event.getCursor();

        if (ammo == null || wp == null || ammo.getItemMeta() == null || wp.getItemMeta() == null) return;
        ItemMeta ammoMeta = ammo.getItemMeta();
        ItemMeta wpMeta = wp.getItemMeta();

        PersistentDataContainer ammoData = ammoMeta.getPersistentDataContainer();
        PersistentDataContainer wpData = wpMeta.getPersistentDataContainer();
        NamespacedKey ammoKey = new NamespacedKey(RogueLike.instance, "amount");
        NamespacedKey wpKey = new NamespacedKey(RogueLike.instance, "ammo");
        NamespacedKey mwpKey = new NamespacedKey(RogueLike.instance, "maxAmmo");

        if (ammoData.has(ammoKey, PersistentDataType.INTEGER) && wpData.has(wpKey, PersistentDataType.INTEGER) && wpData.has(mwpKey, PersistentDataType.INTEGER)) {
            event.setCancelled(true);
            int weaponAmmo = wpData.get(wpKey, PersistentDataType.INTEGER);
            int maxWeaponAmmo = wpData.get(mwpKey, PersistentDataType.INTEGER);
            int ammoAmount = ammoData.get(ammoKey, PersistentDataType.INTEGER);

            wpData.set(wpKey, PersistentDataType.INTEGER, weaponAmmo + 1);
            wpMeta.setDisplayName(wpMeta.getDisplayName().split(" §6§l>> ")[0] + " §6§l>> §e" + (weaponAmmo + 1) + " §6/ §e" + maxWeaponAmmo);
            ammo.setItemMeta(ammoMeta);
            wp.setItemMeta(wpMeta);

            player.playSound(player, Sound.ENTITY_PLAYER_LEVELUP, 6, 3);
        }
    }

}