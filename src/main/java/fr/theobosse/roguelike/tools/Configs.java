package fr.theobosse.roguelike.tools;

import fr.theobosse.roguelike.RogueLike;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Configs {

    private static final List<String> configsName = new ArrayList<>();
    private static final HashMap<String, File> configsFile = new HashMap<>();
    private static final HashMap<String, YamlConfiguration> configs = new HashMap<>();

    public static void load() {
        configsFile.clear();
        configs.clear();
        for (String config : configsName) {
            File file = new File(RogueLike.instance.getDataFolder()+ "/" + config + ".yml");
            if (!file.isFile()) RogueLike.instance.saveResource(config + ".yml", true);

            configsFile.put(config, file);
            configs.put(config, YamlConfiguration.loadConfiguration(file));
        }
    }

    public static void register(String config) {
        configsName.add(config);
    }

    public static void register(List<String> configs) {
        configsName.addAll(configs);
    }

    public static void remove(String config) {
        configsName.remove(config);
    }

    public static YamlConfiguration getConfig(String config) {
        return configs.get(config);
    }

    public static File getFile(String config) {
        return configsFile.get(config);
    }

    public static void save(String config) {
        try {
            configs.get(config).save(configsFile.get(config));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
