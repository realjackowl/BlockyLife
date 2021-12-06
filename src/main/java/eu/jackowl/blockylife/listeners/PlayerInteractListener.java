package eu.jackowl.blockylife.listeners;

import eu.jackowl.blockylife.BlockyLife;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public record PlayerInteractListener(BlockyLife blockyLife) implements Listener {
    @EventHandler
    private void onInteract(@NotNull PlayerInteractEvent e) {
        final UUID playerUUID = e.getPlayer().getUniqueId();
        blockyLife.getAfkList().remove(playerUUID);
        if (e.getAction().equals(Action.LEFT_CLICK_AIR) || e.getAction().equals(Action.LEFT_CLICK_BLOCK)) blockyLife.setPulse(playerUUID, blockyLife.getPulse(playerUUID) + 1.5);
    }
}
