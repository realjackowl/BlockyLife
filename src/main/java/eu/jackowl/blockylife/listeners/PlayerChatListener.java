package eu.jackowl.blockylife.listeners;

import eu.jackowl.blockylife.BlockyLife;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.jetbrains.annotations.NotNull;

public record PlayerChatListener(BlockyLife blockyLife) implements Listener {

    @EventHandler
    private void onChat(@NotNull AsyncPlayerChatEvent e) {
        blockyLife.getAfkList().remove(e.getPlayer().getUniqueId());
    }
}
