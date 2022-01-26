package eu.jackowl.blockylife.checkers;

import eu.jackowl.blockylife.BlockyLife;
import eu.jackowl.blockylife.modules.PulseModule;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public class PulseChecker {

    public PulseChecker(@NotNull BlockyLife blockyLife, BukkitScheduler bukkitScheduler, PulseModule pulseModule) {
        this.pulseModule = pulseModule;
        this.blockyLife = blockyLife;
        this.bukkitScheduler = bukkitScheduler;
        this.calmTitle = BlockyLife.translateMessage(blockyLife.configFile.getString("Modules.Pulse.Translation.Titles.CalmTitle"));
        this.calmSubtitle = BlockyLife.translateMessage(blockyLife.configFile.getString("Modules.Pulse.Translation.Titles.CalmSubtitle"));
        this.moveTitle = BlockyLife.translateMessage(blockyLife.configFile.getString("Modules.Pulse.Translation.Titles.MoveTitle"));
        this.moveSubtitle = BlockyLife.translateMessage(blockyLife.configFile.getString("Modules.Pulse.Translation.Titles.MoveSubtitle"));
        this.rangeList = Objects.requireNonNull(blockyLife.configFile.getConfigurationSection("Modules.Pulse.Settings.Ranges")).getKeys(false);
        this.rangeCount = rangeList.size();
        for (String pulseRange : rangeList) {
            if (Integer.parseInt(pulseRange) == 1) {
                actionList.add(blockyLife.configFile.getString("Modules.Pulse.Settings.Ranges." + pulseRange + ".Actions"));
                maximumList.add(0);
                minimumList.add(blockyLife.configFile.getInt("Modules.Pulse.Settings.Ranges." + pulseRange + ".Minimum"));
            } else if (Integer.parseInt(pulseRange) == rangeCount) {
                actionList.add(blockyLife.configFile.getString("Modules.Pulse.Settings.Ranges." + pulseRange + ".Actions"));
                maximumList.add(blockyLife.configFile.getInt("Modules.Pulse.Settings.Ranges." + pulseRange + ".Maximum"));
                minimumList.add(0);
            } else {
                actionList.add(blockyLife.configFile.getString("Modules.Pulse.Settings.Ranges." + pulseRange + ".Actions"));
                maximumList.add(blockyLife.configFile.getInt("Modules.Pulse.Settings.Ranges." + pulseRange + ".Maximum"));
                minimumList.add(blockyLife.configFile.getInt("Modules.Pulse.Settings.Ranges." + pulseRange + ".Minimum"));
            }
        }
    }

    private final PulseModule pulseModule;
    private final BlockyLife blockyLife;
    private final BukkitScheduler bukkitScheduler;
    private final String calmTitle;
    private final String calmSubtitle;
    private final String moveTitle;
    private final String moveSubtitle;
    private final int rangeCount;
    private final Set<String> rangeList;
    private final ArrayList<String> actionList = new ArrayList<>();
    private final ArrayList<Integer> maximumList = new ArrayList<>();
    private final ArrayList<Integer> minimumList = new ArrayList<>();

    public void runChecker() {
        bukkitScheduler.scheduleSyncRepeatingTask(blockyLife, () ->
        {
            if (!Bukkit.getServer().getOnlinePlayers().isEmpty()) {
                for (final Player p : Bukkit.getOnlinePlayers()) {
                    if (blockyLife.getWorldList().contains((p.getWorld().getName()))) {
                        if (!p.hasPermission("blockylife.bypass")) {
                            final UUID playerUUID = p.getUniqueId();
                            if (!blockyLife.getAfkList().contains(playerUUID)) {
                                final double playerPulse = blockyLife.getPulse(playerUUID);
                                for (String pulseRange : rangeList) {
                                    if (Integer.parseInt(pulseRange) == 1) {
                                        if (playerPulse > minimumList.get(Integer.parseInt(pulseRange) - 1))
                                            takeActions(actionList.get(Integer.parseInt(pulseRange) - 1), p, playerUUID);
                                    } else if (Integer.parseInt(pulseRange) == rangeCount) {
                                        if (playerPulse < maximumList.get(Integer.parseInt(pulseRange) - 1))
                                            takeActions(actionList.get(Integer.parseInt(pulseRange) - 1), p, playerUUID);
                                    } else {
                                        if (playerPulse > minimumList.get(Integer.parseInt(pulseRange) -1) && playerPulse < maximumList.get(Integer.parseInt(pulseRange) - 1))
                                            takeActions(actionList.get(Integer.parseInt(pulseRange) - 1), p, playerUUID);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }, 20L, 20L);
    }

    private void takeActions(@NotNull String actionConfig, Player p, UUID playerUUID) {
        final String[] actionList = actionConfig.replaceAll(" ", "").split(",");
        for (String definedAction : actionList) {
            switch (definedAction) {
                case "BREATH" -> pulseModule.sendBreath(p);
                case "CALM_TITLE" -> p.sendTitle(calmTitle, calmSubtitle, 10, 70, 20);
                case "KILL" -> {
                    p.setHealth(0.0D);
                    blockyLife.setPulse(playerUUID, 80);
                }
                case "MOVE_TITLE" -> p.sendTitle(moveTitle, moveSubtitle, 10, 70, 20);
            }
        }
    }
}