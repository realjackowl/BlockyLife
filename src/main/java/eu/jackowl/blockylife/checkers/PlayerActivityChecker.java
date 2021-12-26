package eu.jackowl.blockylife.checkers;

import eu.jackowl.blockylife.BlockyLife;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public record PlayerActivityChecker(BlockyLife blockyLife, @NotNull BukkitScheduler bukkitScheduler) {

    public void runChecker() {
        bukkitScheduler.scheduleSyncRepeatingTask(blockyLife, () -> {
            if (!Bukkit.getServer().getOnlinePlayers().isEmpty()) {
                for (final Player player : Bukkit.getOnlinePlayers()) {
                    if (blockyLife.getWorldList().contains((player.getWorld().getName()))) {
                        if (!player.hasPermission("blockylife.bypass")) {
                            final UUID playerUUID = player.getUniqueId();
                            final double playerPulse = blockyLife.getPulse(playerUUID);
                            if (!blockyLife.getAfkList().contains(playerUUID)) {
                                if (playerPulse > 80) {
                                    blockyLife.setPulse(playerUUID, playerPulse - blockyLife.getStillHigher80Modifier());
                                } else if (playerPulse < 80 && playerPulse > 60) {
                                    blockyLife.setPulse(playerUUID, playerPulse - blockyLife.getStillLower80Modifier());
                                } else if (playerPulse < 60) {
                                    blockyLife.setPulse(playerUUID, playerPulse - blockyLife.getStillLower60Modifier());
                                }
                            }
                        }
                    }
                }
            }
        }, 20L, 20L);
    }
}