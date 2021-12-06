package eu.jackowl.blockylife.checkers;

import eu.jackowl.blockylife.BlockyLife;
import eu.jackowl.blockylife.managers.PlayerDataManager;
import eu.jackowl.blockylife.modules.PulseModule;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.Objects;
import java.util.UUID;

public record PulseChecker(BlockyLife blockyLife, BukkitScheduler bukkitScheduler, PulseModule pulseModule) {

    //private Pattern hexPattern = Pattern.compile("#[-fA-f0-9]{6}");

    public void runChecker() {
        bukkitScheduler.scheduleSyncRepeatingTask(blockyLife, () ->
        {
            if (!Bukkit.getServer().getOnlinePlayers().isEmpty()) {
                for (final Player p : Bukkit.getOnlinePlayers()) {
                    if (blockyLife.getWorldList().contains((p.getWorld().getName()))) {
                        if (!p.hasPermission("blockylife.bypass")) {
                            final UUID playerUUID = p.getUniqueId();
                            if (!blockyLife.getAfkList().contains(playerUUID)) {
                                //for(String configEntry : config.getStringList("path")) {
                                //}
                                bukkitScheduler.runTaskAsynchronously(blockyLife, () -> {
                                    final double playerPulse = blockyLife.getPulse(playerUUID);
                                    if (playerPulse > 220) {
                                        bukkitScheduler.runTask(blockyLife, () -> killPlayer(p));
                                        blockyLife.setPulse(playerUUID, 80);
                                        PlayerDataManager.saveData(playerUUID, blockyLife);
                                    } else if (playerPulse > 190.00 && playerPulse < 220.00) {
                                        pulseModule.sendBreath(p);
                                        p.sendTitle(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(blockyLife.getConfig().getString("Modules.Pulse.Translation.Titles.CalmTitle"))), ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(blockyLife.getConfig().getString("Modules.Pulse.Translation.Titles.CalmSubtitle"))), 10, 70, 20);
                                        //p.sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(blockyLife.getConfig().getString("Modules.Pulse.Translation.Messages.Slowdown"))));
                                    } else if (playerPulse > 100.00 && playerPulse < 190.00) {
                                        pulseModule.sendBreath(p);
                                    }
                                    //else if (playerPulse < 60.00 && playerPulse > 30.00) {
                                    //}
                                    else if (playerPulse < 30.00 && playerPulse > 20.00) {
                                        p.sendTitle(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(blockyLife.getConfig().getString("Modules.Pulse.Translation.Titles.MoveTitle"))), ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(blockyLife.getConfig().getString("Modules.Pulse.Translation.Titles.MoveSubtitle"))), 10, 70, 20);
                                        //p.sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(blockyLife.getConfig().getString("Modules.Pulse.Translation.Messages.Slowdown"))));
                                    } else if (playerPulse < 20.00) {
                                        bukkitScheduler.runTask(blockyLife, () -> killPlayer(p));
                                    }
                                });
                            }
                        }
                    }
                }
            }
        }, 20L, 20L);
    }

    private void killPlayer(Player p) {
        p.setHealth(0.0D);
    }
}
