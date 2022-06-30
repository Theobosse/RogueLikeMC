package fr.theobosse.roguelike.events;

import fr.theobosse.roguelike.RogueLike;
import fr.theobosse.roguelike.game.Ammo;
import fr.theobosse.roguelike.game.Weapon;
import fr.theobosse.roguelike.tools.Configs;
import fr.theobosse.roguelike.tools.ItemBuilder;
import org.bukkit.*;
import org.bukkit.block.Barrel;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Objects;
import java.util.Random;
import java.util.UUID;
import java.util.logging.Logger;

public class LootCrateEvent implements Listener {

    @EventHandler
    public void opened(PlayerInteractEvent event) {
        if (!event.getAction().equals(Action.LEFT_CLICK_BLOCK)) return;
        if (event.getClickedBlock() == null) return;
        if (!(event.getClickedBlock().getState() instanceof Barrel)) return;

        Player player = event.getPlayer();

        Barrel block = (Barrel) event.getClickedBlock().getState();
        PersistentDataContainer data = block.getPersistentDataContainer();

        NamespacedKey key = new NamespacedKey(RogueLike.instance, "id");
        Location loc = block.getLocation();

        if (data.has(key, PersistentDataType.STRING)) {
            ConfigurationSection section = Configs.getConfig("loots").getConfigurationSection(data.get(key, PersistentDataType.STRING));

            String asuuid = data.get(new NamespacedKey(RogueLike.instance, "as-uuid"), PersistentDataType.STRING);
            Bukkit.getEntity(UUID.fromString(asuuid)).remove();
            loc.getBlock().setType(Material.AIR);

            loc.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, loc, 50, .5, .5, .5, 2);
            loc.getWorld().playSound(loc, Sound.ENTITY_FIREWORK_ROCKET_LARGE_BLAST, 10, 2);
            //loc.getWorld().playEffect(loc, Effect.FIREWORK_SHOOT, Tag.FIRE);

            if (section.contains("drop")) {
                // Si les pourcentage sont equilibré
                if (section.contains("one-item") && Objects.equals(section.getBoolean("one-item"), true)) {
                    int current_precentage = 0;
                    Random rnd = new Random();
                    int when_item_drop = rnd.nextInt(100);

                    for (String item : section.getConfigurationSection("drop").getKeys(false)) {
                        if (section.contains("drop." + item + ".percent-drop")) {
                            int percent_drop = section.getInt("drop." + item + ".percent-drop");
                            current_precentage += percent_drop;
                            if (when_item_drop < current_precentage) {
                                dropItem(loc, section, item);
                                return;
                            }
                        } else {
                            dropItem(loc, section, item);
                            return;
                        }
                    }
                } else {
                    for (String item : section.getConfigurationSection("drop").getKeys(false)) {
                        // Si doit être drop avec un poucentage de chance
                        if (section.contains("drop." + item + ".percent-drop")) {
                            int percent_drop = section.getInt("drop." + item + ".percent-drop");
                            Random rnd = new Random();
                            if (rnd.nextInt(100) < percent_drop) {
                                dropItem(loc, section, item);
                            }
                        } else {
                            dropItem(loc, section, item);
                        }
                    }
                }
            }
        }
    }

    private void dropItem(Location loc, ConfigurationSection section, String item) {
        ItemStack itemStack = getItem(section, "drop." + item);
        if(itemStack != null) {
            loc.getWorld().dropItem(loc, itemStack);
        }
    }

    public static ItemStack getItem(ConfigurationSection section, String itempath){
        if (!section.contains(itempath)) return null;
        if (Objects.equals(section.getString(itempath + ".type"), "weapon")) {
            if (section.contains(itempath + ".weapon-type")) {
                return Weapon.getWeapon(section.getString(itempath + ".weapon-type")).build();
            } else {
                System.out.println("§cErreur: Contactez un admin, le weapon-tpye n'est pas défini");
            }
        } else if (Objects.equals(section.getString(itempath + ".type"), "ammo")) {
            if (section.contains(itempath + ".ammo-quantity")) {
                return Ammo.getItem(section.getInt(itempath + ".ammo-quantity"));
            } else {
                System.out.println("§cErreur: Contactez un admin, le ammo-quantity n'est pas défini");
            }
        } else {
            return new ItemBuilder(section.getConfigurationSection(itempath)).getItem();
        }
        return null;
    }
}
