package eu.jackowl.blockylife.listeners;

import eu.jackowl.blockylife.BlockyLife;
import eu.jackowl.blockylife.managers.PlayerDataManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public record PlayerQuitListener(BlockyLife blockyLife) implements Listener {

    @EventHandler
    private void onQuit(@NotNull PlayerQuitEvent e) {
        Player p = e.getPlayer();
        UUID playerUUID = p.getUniqueId();
        if (blockyLife.getConfig().getBoolean("Modules.Pulse.Enabled")) {
            PlayerDataManager.saveData(playerUUID, blockyLife);
            blockyLife.removePulse(playerUUID);
            blockyLife.getAfkList().remove(playerUUID);
        }
        if (blockyLife.getConfig().getBoolean("Modules.Time.Enabled")) {
            blockyLife.getBossBar().removePlayer(p);
        }
    }
}
