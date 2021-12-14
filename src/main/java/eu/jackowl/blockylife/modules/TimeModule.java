package eu.jackowl.blockylife.modules;

import eu.jackowl.blockylife.BlockyLife;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.Collection;
import java.util.Objects;

public record TimeModule(BlockyLife blockyLife, BukkitScheduler bukkitScheduler, BossBar bossBar) {

    public void startModule() {
        if (Objects.equals(blockyLife.getConfig().getString("Modules.Time.Settings.SynchronizeTime"), "true")) {
            syncTime();
        }
        runDisplay();
    }

    private void syncTime() {
        final World mainWorld = Bukkit.getServer().getWorld(Objects.requireNonNull(blockyLife.getConfig().getString("Modules.Time.Settings.MainWorld")));
        bukkitScheduler.scheduleSyncRepeatingTask(blockyLife, () -> {
            for (String w : blockyLife.getWorldList()) {
                Objects.requireNonNull(Bukkit.getServer().getWorld(w)).setTime(Objects.requireNonNull(mainWorld).getTime());
            }
        }, 20L, 20L);
    }

    private void runDisplay() {
        final World mainWorld = Bukkit.getServer().getWorld(Objects.requireNonNull(blockyLife.getConfig().getString("Modules.Time.Settings.MainWorld")));
        if (Objects.equals(blockyLife.getConfig().getString("Modules.Time.Settings.TimeFormat"), "24H")) {
            switch (Objects.requireNonNull(blockyLife.getConfig().getString("Modules.Time.Settings.TimeDisplay"))) {
                case "ActionBar":
                    bukkitScheduler.runTaskTimerAsynchronously(blockyLife, () -> {
                        final Collection<? extends Player> onlinePlayers = Bukkit.getOnlinePlayers();
                        if (!onlinePlayers.isEmpty()) {
                            final long gameTime = Objects.requireNonNull(mainWorld).getTime();
                            final String timeHours = String.format("%02d", (gameTime / 1000L + 6L) % 24L);
                            final String timeMinutes = String.format("%02d", gameTime % 1000L * 60L / 1000L);
                            for (final Player p : onlinePlayers) {
                                blockyLife.sendActionBarMessage(p, BlockyLife.translateMessage(Objects.requireNonNull(blockyLife.getConfig().getString("Modules.Time.Settings.ValueFormat")).replace("{0}", timeHours).replace("{1}", timeMinutes)));
                            }
                        }
                    }, 0L, 0L);
                case "BossBar":
                    bukkitScheduler.runTaskTimerAsynchronously(blockyLife, () -> {
                        final Collection<? extends Player> onlinePlayers = Bukkit.getOnlinePlayers();
                        if (!onlinePlayers.isEmpty()) {
                            if (!bossBar.isVisible()) {
                                bossBar.setVisible(true);
                            }
                            final long gameTime = Objects.requireNonNull(mainWorld).getTime();
                            final String timeHours = String.format("%02d", (gameTime / 1000L + 6L) % 24L);
                            final String timeMinutes = String.format("%02d", gameTime % 1000L * 60L / 1000L);
                            for (final Player p : onlinePlayers) {
                                if (!bossBar.getPlayers().contains(p)) {
                                    bossBar.addPlayer(p);
                                }
                                bossBar.setTitle(BlockyLife.translateMessage(Objects.requireNonNull(blockyLife.getConfig().getString("Modules.Time.Settings.ValueFormat")).replace("{0}", timeHours).replace("{1}", timeMinutes)));
                            }
                        } else {
                            if (bossBar.isVisible()) {
                                bossBar.setVisible(false);
                            }
                        }
                    }, 0L, 0L);
                default:
                    //blockyLife.sendConsoleMessage();
                    break;
            }
        } else {
            switch (Objects.requireNonNull(blockyLife.getConfig().getString("Modules.Time.Settings.TimeDisplay"))) {
                case "ActionBar":
                    bukkitScheduler.runTaskTimerAsynchronously(blockyLife, () -> {
                        final Collection<? extends Player> onlinePlayers = Bukkit.getOnlinePlayers();
                        if (!onlinePlayers.isEmpty()) {
                            boolean isPM = false;
                            final long gameTime = Objects.requireNonNull(mainWorld).getTime();
                            long currentHour = (gameTime / 1000L + 6L) % 24L;
                            if (currentHour > 12) {
                                currentHour -= 12L;
                                isPM = true;
                            }
                            final String timeHours = Long.toString(currentHour);
                            final String timeMinutes = String.format("%02d", gameTime % 1000L * 60L / 1000L);
                            for (final Player p : onlinePlayers) {
                                blockyLife.sendActionBarMessage(p, BlockyLife.translateMessage(Objects.requireNonNull(blockyLife.getConfig().getString("Modules.Time.Settings.ValueFormat")).replace("{0}", timeHours).replace("{1}", timeMinutes).replace("{2}", isPM ? "PM" : "AM")));
                            }
                        }
                    }, 0L, 0L);
                case "BossBar":
                    final BossBar bossBar = Bukkit.createBossBar("Time", BarColor.BLUE, BarStyle.SOLID);
                    bukkitScheduler.runTaskTimerAsynchronously(blockyLife, () -> {
                        final Collection<? extends Player> onlinePlayers = Bukkit.getOnlinePlayers();
                        if (!onlinePlayers.isEmpty()) {
                            if (!bossBar.isVisible()) {
                                bossBar.setVisible(true);
                            }
                            boolean isPM = false;
                            final long gameTime = Objects.requireNonNull(mainWorld).getTime();
                            long currentHour = (gameTime / 1000L + 6L) % 24L;
                            if (currentHour > 12) {
                                currentHour -= 12L;
                                isPM = true;
                            }
                            final String timeHours = Long.toString(currentHour);
                            final String timeMinutes = String.format("%02d", gameTime % 1000L * 60L / 1000L);
                            for (final Player p : onlinePlayers) {
                                if (!bossBar.getPlayers().contains(p)) {
                                    bossBar.addPlayer(p);
                                }
                                bossBar.setTitle(BlockyLife.translateMessage(Objects.requireNonNull(blockyLife.getConfig().getString("Modules.Time.Settings.ValueFormat")).replace("{0}", timeHours).replace("{1}", timeMinutes).replace("{2}", isPM ? "PM" : "AM")));
                            }
                        } else {
                            if (bossBar.isVisible()) {
                                bossBar.setVisible(false);
                            }
                        }
                    }, 0L, 0L);
                default:
                    //blockyLife.sendConsoleMessage();
                    break;
            }
        }
    }
}