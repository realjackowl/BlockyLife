package eu.jackowl.blockylife.managers;

import eu.jackowl.blockylife.BlockyLife;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public record WorldManager(BlockyLife blockyLife) {
    public void loadWorlds() {
        List<String> worldList = blockyLife.getWorldList();
        final File file = new File(blockyLife.dataFolder, File.separator + "worlds.yml");
        final FileConfiguration yaml = YamlConfiguration.loadConfiguration(file);
        if (!file.exists()) {
            try {
                List<String> excludeFolders = Arrays.asList("bundler", "cache", "crash-reports", "libraries", "logs", "plugins", "versions");
                for (File serverFile : Objects.requireNonNull(Bukkit.getServer().getWorldContainer().listFiles())) {
                    if (serverFile.isDirectory() && !excludeFolders.contains(serverFile.getName())) {
                        worldList.add(serverFile.getName());
                    }
                }
                blockyLife.setWorldList(worldList);
                yaml.set("worlds", worldList);
                yaml.save(file);
            } catch (final IOException exception) {
                exception.printStackTrace();
            }
        } else {
            blockyLife.setWorldList(yaml.getStringList("worlds"));
        }
    }
}