package eu.jackowl.blockylife.managers;

import com.tchristofferson.configupdater.ConfigUpdater;
import eu.jackowl.blockylife.BlockyLife;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public record ConfigManager(BlockyLife blockyLife) {

    public void updateConfig() {
        File configFile = new File(blockyLife.dataFolder, "config.yml");
        try {
            ConfigUpdater.update(blockyLife, "config.yml", configFile, Arrays.asList("AFKCheck", "Modules.Pulse", "Modules.Time.Enabled", "Modules.Time.Settings.MainWorld", "Modules.Time.Settings.SynchronizeTime", "Modules.Time.Settings.TimeDisplay", "Modules.Time.Settings.TimeFormat", "Modules.Time.Settings.ValueFormat"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
