package fr.theobosse.roguelike.events;

import fr.theobosse.roguelike.RogueLike;
import fr.theobosse.roguelike.tools.Configs;
import fr.theobosse.roguelike.tools.ItemBuilder;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.block.Barrel;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.checkerframework.checker.units.qual.N;

import java.util.Random;
import java.util.UUID;

public class LootCreateEvent {
    @EventHandler
    public void opened(PlayerInteractEvent event){
        Barrel block = (Barrel) event.getClickedBlock();
        PersistentDataContainer data = block.getPersistentDataContainer();
        Location loc = block.getLocation();
        ConfigurationSection section = Configs.getConfig("loots").getConfigurationSection(data.get(new NamespacedKey(RogueLike.instance, "id"), PersistentDataType.STRING));
        if (section.contains("drop")){
            for(String item : section.getConfigurationSection("drop").getKeys(false)){
                if (section.contains("drop." + item + ".percent-drop")){
                    int percent_drop = section.getInt("drop." + item + ".percent-drop");
                    Random rnd = new Random();
                    if (rnd.nextInt(100) < percent_drop)
                        block.getWorld().dropItem(loc, new ItemBuilder(section.getConfigurationSection("drop." + item)).getItem());
                }else {
                    block.getWorld().dropItem(loc, new ItemBuilder(section.getConfigurationSection("drop." + item)).getItem());
                }
            }
        }

        loc.getBlock().setType(Material.AIR);

        String asuuid = data.get(new NamespacedKey(RogueLike.instance, "asuuid"), PersistentDataType.STRING);
        loc.getWorld().getEntity(UUID.fromString(asuuid)).remove();
    }
}
