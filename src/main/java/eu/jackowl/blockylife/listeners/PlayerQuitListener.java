package eu.jackowl.blockylife.listeners;

import eu.jackowl.blockylife.BlockyLife;
import eu.jackowl.blockylife.managers.PlayerDataManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public record PlayerQuitListener(BlockyLife blockyLife) implements Listener {

    @EventHandler
    private void onQuit(@NotNull PlayerQuitEvent e) {
        UUID playerUUID = e.getPlayer().getUniqueId();
        PlayerDataManager.saveData(playerUUID, blockyLife);
        blockyLife.removePulse(playerUUID);
        blockyLife.getAfkList().remove(playerUUID);
    }
}
