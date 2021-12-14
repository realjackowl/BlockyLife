package eu.jackowl.blockylife.placeholders;

import eu.jackowl.blockylife.BlockyLife;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

public class PlaceholderAPI extends PlaceholderExpansion {

    private final BlockyLife plugin;

    public PlaceholderAPI(BlockyLife plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getAuthor() {
        return "jackowl";
    }

    @Override
    public @NotNull String getIdentifier() {
        return "blockylife";
    }

    @Override
    public @NotNull String getVersion() {
        return "0.1.0";
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {
        if (params.equalsIgnoreCase("pulse") && plugin.getConfig().getBoolean("Modules.Pulse.Enabled")) {
            return Long.toString(Math.round(plugin.getPulse(player.getUniqueId())));
        }
        return null;
    }
}
