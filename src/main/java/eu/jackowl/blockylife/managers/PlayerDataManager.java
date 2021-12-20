package eu.jackowl.blockylife.managers;

import eu.jackowl.blockylife.BlockyLife;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class PlayerDataManager {

    public static @Unmodifiable Object getObject(UUID playerUUID, String p, @NotNull BlockyLife blockyLife) {
        final File f = new File(new File(blockyLife.getDataFolder(), File.separator + "players"), File.separator + playerUUID + ".yml");
        final YamlConfiguration pd = YamlConfiguration.loadConfiguration(f);
        return pd.getString(p);
    }

    public static void setObject(UUID playerUUID, String p, Object v, @NotNull BlockyLife blockyLife) {
        final File f = new File(new File(blockyLife.getDataFolder(), File.separator + "players"), File.separator + playerUUID + ".yml");
        final YamlConfiguration pd = YamlConfiguration.loadConfiguration(f);
        pd.set(p, v);
        try {
            pd.save(f);
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    public static void createData(UUID playerUUID, @NotNull BlockyLife blockyLife) {
        final File f = new File(new File(blockyLife.getDataFolder(), File.separator + "players"), File.separator + playerUUID + ".yml");
        final YamlConfiguration pd = YamlConfiguration.loadConfiguration(f);
        if (!f.exists()) {
            try {
                pd.set("pulse", 80.00);
                pd.save(f);
            } catch (final IOException exception) {
                exception.printStackTrace();
            }
        }
    }

    public static void saveData(UUID playerUUID, @NotNull BlockyLife blockyLife) {
        final File f = new File(new File(blockyLife.getDataFolder(), File.separator + "players"), File.separator + playerUUID + ".yml");
        if (f.exists()) {
            PlayerDataManager.setObject(playerUUID, "pulse", Math.round(blockyLife.getPulse(playerUUID)), blockyLife);
        }
    }
}