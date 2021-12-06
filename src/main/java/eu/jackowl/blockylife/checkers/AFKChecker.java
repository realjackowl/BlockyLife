package eu.jackowl.blockylife.checkers;

import eu.jackowl.blockylife.BlockyLife;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.UUID;

public class AFKChecker {

    private final HashMap<UUID, Location> playerLocation = new HashMap<>();

    public void runChecker(BlockyLife blockyLife, @NotNull BukkitScheduler bukkitScheduler) {
        bukkitScheduler.scheduleSyncRepeatingTask(blockyLife, () -> {
            if (!Bukkit.getServer().getOnlinePlayers().isEmpty()) {
                for (final Player player : Bukkit.getOnlinePlayers()) {
                    if (!player.hasPermission("blockylife.bypass")) {
                        if (blockyLife.getWorldList().contains((player.getWorld().getName()))) {
                            final UUID playerUUID = player.getUniqueId();
                            if (player.getLocation().equals(playerLocation.get(playerUUID)) && !blockyLife.getAfkList().contains(playerUUID)) {
                                blockyLife.addToAfkList(playerUUID);
                            }
                            playerLocation.put(playerUUID, player.getLocation());
                        }
                    }
                }
            }
        }, blockyLife.getConfig().getInt("AFKCheck"), blockyLife.getConfig().getInt("AFKCheck"));
    }
}
