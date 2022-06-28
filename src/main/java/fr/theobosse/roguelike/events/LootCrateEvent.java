package fr.theobosse.roguelike.events;

import fr.theobosse.roguelike.RogueLike;
import fr.theobosse.roguelike.tools.Configs;
import fr.theobosse.roguelike.tools.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Barrel;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Random;
import java.util.UUID;

public class LootCrateEvent implements Listener {

    @EventHandler
    public void opened(PlayerInteractEvent event) {
        if (!event.getAction().equals(Action.LEFT_CLICK_BLOCK)) return;
        if (event.getClickedBlock() == null) return;
        if (!(event.getClickedBlock().getState() instanceof Barrel)) return;

        Barrel block = (Barrel) event.getClickedBlock().getState();
        PersistentDataContainer data = block.getPersistentDataContainer();

        System.out.println("KEYS:");
        for (NamespacedKey k : data.getKeys())
            System.out.println(k.getKey());

        NamespacedKey key = new NamespacedKey(RogueLike.instance, "id");
        Location loc = block.getLocation();

        if (data.has(key, PersistentDataType.STRING)) {
            ConfigurationSection section = Configs.getConfig("loots").getConfigurationSection(data.get(key, PersistentDataType.STRING));
            if (section.contains("drop")) {
                for (String item : section.getConfigurationSection("drop").getKeys(false)) {
                    if (section.contains("drop." + item + ".percent-drop")) {
                        int percent_drop = section.getInt("drop." + item + ".percent-drop");
                        Random rnd = new Random();
                        if (rnd.nextInt(100) < percent_drop)
                            block.getWorld().dropItem(loc, new ItemBuilder(section.getConfigurationSection("drop." + item)).getItem());
                    } else {
                        block.getWorld().dropItem(loc, new ItemBuilder(section.getConfigurationSection("drop." + item)).getItem());
                    }
                }
            }

            loc.getBlock().setType(Material.AIR);
            String asuuid = data.get(new NamespacedKey(RogueLike.instance, "as-uuid"), PersistentDataType.STRING);
            Bukkit.getEntity(UUID.fromString(asuuid)).remove();
        }
    }
}
