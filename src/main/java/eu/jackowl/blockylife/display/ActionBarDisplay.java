package eu.jackowl.blockylife.display;

import eu.jackowl.blockylife.BlockyLife;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.UUID;

public record ActionBarDisplay(BlockyLife blockyLife,
                               BukkitScheduler bukkitScheduler) {

    public void runDisplay() {
        bukkitScheduler.scheduleSyncRepeatingTask(blockyLife, () -> {
            if (!Bukkit.getServer().getOnlinePlayers().isEmpty()) {
                for (final Player p : Bukkit.getOnlinePlayers()) {
                    if (blockyLife.getWorldList().contains((p.getWorld().getName()))) {
                        final UUID playerUUID = p.getUniqueId();
                        if (!blockyLife.getAfkList().contains(playerUUID)) {
                            if (!p.hasPermission("blockylife.bypass")) {
                                blockyLife.sendActionBarMessage(p, ChatColor.translateAlternateColorCodes('&', "&c♥ " + Math.round(blockyLife.getPulse(playerUUID))));
                                bukkitScheduler.runTaskLaterAsynchronously(blockyLife, () -> {
                                    if (p.isOnline())
                                        blockyLife.sendActionBarMessage(p, ChatColor.translateAlternateColorCodes('&', "&4♥&c " + Math.round(blockyLife.getPulse(playerUUID))));
                                }, 10L);
                            }
                        }
                    }
                }
            }
        }, 20L, 20L);
    }
}