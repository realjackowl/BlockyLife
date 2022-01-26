package eu.jackowl.blockylife.listeners;

import eu.jackowl.blockylife.BlockyLife;
import eu.jackowl.blockylife.managers.PlayerDataManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public record PlayerDeathListener(BlockyLife blockyLife) implements Listener {

    @EventHandler
    private void onDeath(@NotNull PlayerDeathEvent e) {
        final UUID playerUUID = e.getEntity().getUniqueId();
        blockyLife.setPulse(playerUUID, 80);
        PlayerDataManager.saveData(playerUUID, blockyLife);
        //'s heart couldn't make it
    }
}