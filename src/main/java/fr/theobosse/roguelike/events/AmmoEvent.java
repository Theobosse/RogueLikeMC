package fr.theobosse.roguelike.events;

import fr.theobosse.roguelike.game.Weapon;
import fr.theobosse.roguelike.tools.DataManager;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class AmmoEvent implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void hitEvent(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) return;
        Player player = (Player) event.getDamager();
        Entity entity = event.getEntity();
        ItemStack is = player.getItemInHand();

        DataManager data = new DataManager(is);
        DataManager entData = new DataManager(entity);

        if (!data.contains("ammo", PersistentDataType.INTEGER)) return;

        int ammo = data.get("ammo", PersistentDataType.INTEGER);
        if (ammo > 0) {
            data.sub("ammo", 1);
            if (ammo == 1)
                player.playSound(player, Sound.ENTITY_ITEM_BREAK, SoundCategory.PLAYERS, 100, 3);
            updateWeaponDisplay(is);
        } else {
            player.playSound(player, Sound.ENTITY_VILLAGER_NO, SoundCategory.PLAYERS, 10, 2);
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInvClick(InventoryClickEvent event) {
        if (!event.getClick().isRightClick()) return;
        if (!(event.getWhoClicked() instanceof Player)) return;
        Player player = (Player) event.getWhoClicked();
        ItemStack wp = event.getCurrentItem();
        ItemStack ammo = event.getCursor();

        if (ammo == null || wp == null || ammo.getItemMeta() == null || wp.getItemMeta() == null) return;
        DataManager ammoData = new DataManager(ammo);
        DataManager wpData = new DataManager(wp);
        Weapon weapon = wpData.getWeapon();

        if (ammoData.contains("amount", PersistentDataType.INTEGER) && wpData.contains("ammo", PersistentDataType.INTEGER) && weapon != null) {
            event.setCancelled(true);
            int weaponAmmo = wpData.get("ammo", PersistentDataType.INTEGER);
            int ammoAmount = ammoData.get("amount", PersistentDataType.INTEGER);
            int maxWeaponAmmo = weapon.getAmmo();

            if (ammoAmount > 0 && weaponAmmo != maxWeaponAmmo) {
                int newAmmo = event.getClick().isShiftClick() ? Math.min(ammoAmount, maxWeaponAmmo - weaponAmmo) : 1;
                wpData.add("ammo", newAmmo);
                ammoData.sub("amount", newAmmo);
                updateWeaponDisplay(wp);
                updateAmmoDisplay(ammo);

                player.playSound(player, Sound.ENTITY_PLAYER_LEVELUP, 6, 3);
            } else {
                player.playSound(player.getLocation(), Sound.BLOCK_PISTON_EXTEND, 7, 2);
            }
        }
    }

    @EventHandler
    public void onCollect(EntityPickupItemEvent event) {
        // Called when a player collects ammo
        if (!(event.getEntity() instanceof Player)) return;

        Player player = (Player) event.getEntity();
        Item item = event.getItem();

        DataManager data = new DataManager(item.getItemStack());
        if (!data.contains("amount", PersistentDataType.INTEGER)) return;
        int amount = data.get("amount", PersistentDataType.INTEGER);

        ItemStack is = player.getInventory().getItem(8);
        if (is == null) return;
        DataManager invData = new DataManager(is);
        ItemStack invItemStack = player.getInventory().getItem(8);
        if (invItemStack == null){
            player.sendMessage("§cWTF? what pas d'item en 8");
            return;
        }

        if (!invData.contains("amount", PersistentDataType.INTEGER)){
            player.sendMessage("§cERREUR: Case 8 ne contient pas de \"amount\"! oulàlà!");
            return;
        }

        invData.add("amount", amount * item.getItemStack().getAmount());
        updateAmmoDisplay(is);
        event.setCancelled(true);
        item.remove();
    }

    public void updateWeaponDisplay(ItemStack is) {
        DataManager data = new DataManager(is);
        Weapon weapon = data.getWeapon();
        ItemMeta im = is.getItemMeta();
        assert im != null;

        int ammo = data.get("ammo", PersistentDataType.INTEGER);
        String name = weapon.getName();
        int maxAmmo = weapon.getAmmo();

        if (ammo > 0)
            im.setDisplayName(name + " §6§l>> §e" + ammo + " §6/ §e" + maxAmmo);
        else
            im.setDisplayName(name + " §6§l>> §4§lCASSÉ");
        is.setItemMeta(im);
    }

    public void updateAmmoDisplay(ItemStack is) {
        DataManager data = new DataManager(is);
        ItemMeta im = is.getItemMeta();
        assert im != null;

        int amount = data.get("amount", PersistentDataType.INTEGER);
        im.setDisplayName("§5Munitions §e(§a§l" + (amount) + "§e)");
        is.setItemMeta(im);
    }
}
