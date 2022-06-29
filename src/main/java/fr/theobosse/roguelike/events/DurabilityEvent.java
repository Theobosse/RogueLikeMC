package fr.theobosse.roguelike.events;

import fr.theobosse.roguelike.RogueLike;
import fr.theobosse.roguelike.game.User;
import fr.theobosse.roguelike.game.Weapon;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class DurabilityEvent implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
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

}
