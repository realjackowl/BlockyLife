package eu.jackowl.blockylife.listeners;

import eu.jackowl.blockylife.BlockyLife;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UUID;

public record PlayerMoveListener(BlockyLife blockyLife) implements Listener {

    @EventHandler
    private void onMove(@NotNull PlayerMoveEvent e) {
        final Player p = e.getPlayer();
        if (blockyLife.getWorldList().contains((p.getWorld().getName()))) {
            if (!p.hasPermission("blockylife.bypass")) {
                final UUID playerUUID = p.getUniqueId();
                blockyLife.getAfkList().remove(playerUUID);
                if (!p.isInsideVehicle()) {
                    if (p.isSprinting()) {
                        blockyLife.setPulse(playerUUID, blockyLife.getPulse(playerUUID) + blockyLife.getSprintModifier());
                        final Location from = e.getFrom();
                        final Location to = e.getTo();
                        if (from.getBlockY() < Objects.requireNonNull(to).getBlockY() && !p.isSwimming() && !p.isFlying()) {
                            blockyLife.setPulse(playerUUID, blockyLife.getPulse(playerUUID) + blockyLife.getJumpSprintModifier());
                        }
                    } else {
                        final Location from = e.getFrom();
                        final Location to = e.getTo();
                        if (from.getBlockY() < Objects.requireNonNull(to).getBlockY() && !p.isSwimming() && !p.isFlying()) {
                            blockyLife.setPulse(playerUUID, blockyLife.getPulse(playerUUID) + blockyLife.getJumpModifier());
                        }
                        if (blockyLife.getPulse(playerUUID) > 80) {
                            blockyLife.setPulse(playerUUID, blockyLife.getPulse(playerUUID) - blockyLife.getMoveModifier());
                        } else if (blockyLife.getPulse(playerUUID) < 80) {
                            blockyLife.setPulse(playerUUID, blockyLife.getPulse(playerUUID) - blockyLife.getMoveLower80Modifier());
                        }
                    }
                }
            }
        }
    }
}
