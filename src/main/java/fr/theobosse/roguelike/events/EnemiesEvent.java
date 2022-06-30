package fr.theobosse.roguelike.events;

import fr.theobosse.roguelike.RogueLike;
import fr.theobosse.roguelike.tools.Configs;
import fr.theobosse.roguelike.tools.ItemBuilder;
import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Random;


public class EnemiesEvent implements Listener {

    @EventHandler
    public void onArrowHit(EntityDamageByEntityEvent event){
        if (!(event.getCause().equals(EntityDamageEvent.DamageCause.PROJECTILE))) return;

        Arrow arrow = (Arrow) event.getDamager();
        if (!(arrow.getShooter() instanceof Mob)) return;
        Mob mob = (Mob) arrow.getShooter();
        if (!(event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getEntity();

        PersistentDataContainer data = mob.getPersistentDataContainer();
        String id = data.get(new NamespacedKey(RogueLike.instance, "id"), PersistentDataType.STRING);

        ConfigurationSection section = Configs.getConfig("mobs").getConfigurationSection(id);


        if(section.contains("damage")) {
            Integer damage = section.getInt("damage");
            // Jsp si on eleve - event.getDamage(), genre on le heal du nombre de degat qu'il a reçu
            // Sinon il se prend L'arc de base et le damage
            player.damage(damage);

        }
        return;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void damaged(EntityDamageEvent event) {
        if (event.isCancelled()) return;
        double damage = event.getFinalDamage();
        Entity entity = event.getEntity();
        World world = entity.getWorld();

        NamespacedKey key = new NamespacedKey(RogueLike.instance, "life");
        PersistentDataContainer data = entity.getPersistentDataContainer();
        Location loc = entity.getLocation();

        if (data.has(key, PersistentDataType.DOUBLE)) {
            double life = data.get(key, PersistentDataType.DOUBLE);
            ConfigurationSection section = Configs.getConfig("mobs").getConfigurationSection(data.get(new NamespacedKey(RogueLike.instance, "id"), PersistentDataType.STRING));
            data.set(key, PersistentDataType.DOUBLE, life - damage);
            String name = section.getString("name");
            entity.setCustomName(name + " §c[" + Math.round(life - damage) + "]");
            event.setDamage(0);

            // Summon Armor Stand with damage quantity
            Random rnd = new Random();
            ArmorStand armorStand = (ArmorStand) world.spawnEntity(loc.clone().add(rnd.nextDouble() * 2 - 1, rnd.nextDouble() * 2 + 1, rnd.nextDouble() * 2 - 1), EntityType.ARMOR_STAND);
            armorStand.setCustomName("§c" + Math.round(damage));
            armorStand.setCustomNameVisible(true);
            armorStand.setGravity(false);
            armorStand.setVisible(false);
            armorStand.setMarker(true);

            Bukkit.getScheduler().runTaskLater(RogueLike.instance, armorStand::remove,30);

            // Kill
            if (life - damage <= 0) {
                entity.remove();

                ((ExperienceOrb) entity.getWorld().spawn(loc, EntityType.EXPERIENCE_ORB.getEntityClass())).setExperience(section.getInt("xp"));
                entity.getWorld().spawnParticle(Particle.SOUL, loc.add(0, 1, 0), 10, 0.5, 0.5, 0.5, 1);
                entity.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME, loc.add(0, 1, 0), 10, 0.5, 0.5, 0.5, 1);

                if (section.contains("drop")) {
                    for(String item : section.getConfigurationSection("drop").getKeys(false)){
                        if (section.contains("drop." + item + ".percent-drop")){
                            int percent_drop = section.getInt("drop." + item + ".percent-drop");
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
