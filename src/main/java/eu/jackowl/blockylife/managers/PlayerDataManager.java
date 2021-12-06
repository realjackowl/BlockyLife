package eu.jackowl.blockylife.managers;

import eu.jackowl.blockylife.BlockyLife;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.Unmodifiable;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

public class PlayerDataManager {

    private static final File pd = new File(Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("BlockyLife")).getDataFolder(), File.separator + "players");

    public static @Unmodifiable Object getObject(UUID playerUUID, String p) {
        final File f = new File(pd, File.separator + playerUUID + ".yml");
        final YamlConfiguration pd = YamlConfiguration.loadConfiguration(f);
        return pd.getString(p);
    }

    public static void setObject(UUID playerUUID, String p, Object v) {
        final File f = new File(pd, File.separator + playerUUID + ".yml");
        final YamlConfiguration pd = YamlConfiguration.loadConfiguration(f);
        pd.set(p, v);
        try {
            pd.save(f);
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    public static void createData(UUID playerUUID) {
        final File f = new File(pd, File.separator + playerUUID + ".yml");
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

    public static void saveData(UUID playerUUID, BlockyLife blockyLife) {
        final File f = new File(pd, File.separator + playerUUID + ".yml");
        if (f.exists()) {
            PlayerDataManager.setObject(playerUUID, "pulse", Math.round(blockyLife.getPulse(playerUUID)));
        }
    }
}