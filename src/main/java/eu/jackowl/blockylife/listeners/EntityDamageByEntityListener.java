package eu.jackowl.blockylife.listeners;

import eu.jackowl.blockylife.BlockyLife;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.jetbrains.annotations.NotNull;

public record EntityDamageByEntityListener(BlockyLife blockyLife) implements Listener {

    @EventHandler
    public void onEntityDamageByEntity(@NotNull EntityDamageByEntityEvent e) {
        if (blockyLife.getAfkList().contains(e.getEntity().getUniqueId())) e.setCancelled(true);
    }
}