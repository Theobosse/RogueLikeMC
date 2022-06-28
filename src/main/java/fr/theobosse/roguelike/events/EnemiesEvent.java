package fr.theobosse.roguelike.events;

import fr.theobosse.roguelike.RogueLike;
import fr.theobosse.roguelike.tools.Configs;
import fr.theobosse.roguelike.tools.ItemBuilder;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Random;


public class EnemiesEvent implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void damaged(EntityDamageEvent event) {
        double damage = event.getFinalDamage();
        Entity entity = event.getEntity();
        World world = entity.getWorld()
        NamespacedKey key = new NamespacedKey(RogueLike.instance, "life");
        PersistentDataContainer data = entity.getPersistentDataContainer();
        Location loc = entity.getLocation();

        if (data.has(key)) {
            double life = data.get(key, PersistentDataType.DOUBLE);
            ConfigurationSection section = Configs.getConfig("mobs").getConfigurationSection(data.get(new NamespacedKey(RogueLike.instance, "id"), PersistentDataType.STRING));
            data.set(key, PersistentDataType.DOUBLE, life - damage);
            String name = section.getString("name");
            entity.setCustomName(name + " §c[" + Math.round(life - damage) + "]");
            event.setDamage(0);

            // Summon Armor Stand with damage quantity
            Entity armorStand;
            armorStand = world.spawnEntity(loc, EntityType.ARMOR_STAND);
            armorStand.setCustomName(String.valueOf(damage));

            // Kill
            if (life - damage <= 0) {
                entity.remove();

                ((ExperienceOrb) entity.getWorld().spawn(loc, EntityType.EXPERIENCE_ORB.getEntityClass())).setExperience(section.getInt("xp"));
                entity.getWorld().spawnParticle(Particle.SOUL, loc.add(0, 1, 0), 10, 0.5, 0.5, 0.5, 1, 0, true);
                entity.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME, loc.add(0, 1, 0), 10, 0.5, 0.5, 0.5, 1, 0, true);
                Random rnd = new Random();
                ArmorStand armowo = (ArmorStand) loc.getWorld().spawnEntity(loc.add(rnd.nextInt(3)-1,rnd.nextInt(2),rnd.nextInt(3)-1), EntityType.ARMOR_STAND);
                armowo.setCustomName(String.valueOf(damage));
                armowo.setVisible(false);
                armowo.setCustomNameVisible(true);

                if (section.contains("drop")){
                    for(String item : section.getConfigurationSection("drop").getKeys(false)){
                        if (section.contains("drop." + item + ".percent-drop")){
                            int percent_drop = section.getInt("drop." + item + ".percent-drop");
                            Random rnd = new Random();
                            if (rnd.nextInt(100) < percent_drop)
                                entity.getWorld().dropItem(loc, new ItemBuilder(section.getConfigurationSection("drop." + item)).getItem());
                        }else {
                            entity.getWorld().dropItem(loc, new ItemBuilder(section.getConfigurationSection("drop." + item)).getItem());
                        }
                    }
                }
            }
        }

    }

}
