package eu.jackowl.blockylife.listeners;

import eu.jackowl.blockylife.BlockyLife;
import eu.jackowl.blockylife.managers.PlayerDataManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public record PlayerKickListener(BlockyLife blockyLife) implements Listener {
    @EventHandler
    private void onKick(@NotNull PlayerKickEvent e) {
        final UUID playerUUID = e.getPlayer().getUniqueId();
        PlayerDataManager.saveData(playerUUID, blockyLife);
        blockyLife.removePulse(playerUUID);
        blockyLife.getAfkList().remove(playerUUID);
    }
}
