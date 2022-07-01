package fr.theobosse.roguelike.events;

import fr.theobosse.roguelike.RogueLike;
import fr.theobosse.roguelike.game.Ammo;
import fr.theobosse.roguelike.game.Weapon;
import fr.theobosse.roguelike.tools.Configs;
import fr.theobosse.roguelike.tools.ItemBuilder;
import org.bukkit.*;
import org.bukkit.block.Barrel;
import org.bukkit.block.Container;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;

import java.util.Objects;
import java.util.Random;
import java.util.UUID;

public class WeaponEvent implements Listener {

    @EventHandler
    public void shoot(PlayerInteractEvent event) {
        if (!event.getAction().equals(Action.RIGHT_CLICK_AIR) && !event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;

        Player player = event.getPlayer();
        World world = player.getWorld();
        Location loc = player.getEyeLocation();

        ItemStack itemStack = player.getItemInHand();
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) return;

        NamespacedKey key = new NamespacedKey(RogueLike.instance, "name");
        PersistentDataContainer container = itemMeta.getPersistentDataContainer();
        String id = container.get(key, PersistentDataType.STRING);
        Weapon weapon = Weapon.getWeapon(id);

        // check for cooldown
        if (player.getCooldown(itemStack.getType()) > 0) return;

        if (weapon == null) return;
        if (weapon.getProjectile() == null) return;

        double projSpeed = weapon.getProjectileSpeed();

        // Create & spawn arrow
        Projectile p = (Projectile) world.spawnEntity(loc, EntityType.fromName(weapon.getProjectile()));

        Vector dirVec = player.getLocation().getDirection().normalize().multiply(projSpeed);
        p.setVelocity(dirVec);

        // Reset cooldown
        player.setCooldown(itemStack.getType(), (int) weapon.getCooldown());
        weapon.updateAmmoCount(itemStack);
    }
}
