package eu.jackowl.blockylife.listeners;

import eu.jackowl.blockylife.BlockyLife;
import eu.jackowl.blockylife.managers.PlayerDataManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public record PlayerJoinListener(BlockyLife blockyLife) implements Listener {

    @EventHandler
    private void onJoin(@NotNull PlayerJoinEvent e) {
        final UUID playerUUID = e.getPlayer().getUniqueId();
        PlayerDataManager.createData(playerUUID, blockyLife);
        blockyLife.setPulse(playerUUID, Float.parseFloat((String) PlayerDataManager.getObject(playerUUID, "pulse", blockyLife)));
    }
}
