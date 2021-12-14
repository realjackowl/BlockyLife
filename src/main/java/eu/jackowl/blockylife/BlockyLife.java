package eu.jackowl.blockylife;

import eu.jackowl.blockylife.checkers.AFKChecker;
import eu.jackowl.blockylife.checkers.PlayerActivityChecker;
import eu.jackowl.blockylife.checkers.PulseChecker;
import eu.jackowl.blockylife.display.ActionBarDisplay;
import eu.jackowl.blockylife.listeners.*;
import eu.jackowl.blockylife.managers.ConfigManager;
import eu.jackowl.blockylife.managers.PlayerDataManager;
import eu.jackowl.blockylife.managers.WorldManager;
import eu.jackowl.blockylife.modules.PulseModule;
import eu.jackowl.blockylife.modules.TimeModule;
import eu.jackowl.blockylife.placeholders.PlaceholderAPI;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BlockyLife extends JavaPlugin {

    private final BukkitScheduler bukkitScheduler = Bukkit.getScheduler();

    private final HashMap<UUID, Double> pulse = new HashMap<>();

    public Double getPulse(UUID playerUUID) {
        return pulse.get(playerUUID);
    }

    public void setPulse(UUID playerUUID, double pulse) {
        this.pulse.put(playerUUID, pulse);
    }

    public void removePulse(UUID playerUUID) {
        pulse.remove(playerUUID);
    }

    private final ArrayList<UUID> afkList = new ArrayList<>();

    public ArrayList<UUID> getAfkList() {
        return afkList;
    }

    public void addToAfkList(UUID playerUUID) {
        afkList.add(playerUUID);
    }

    private void clearAfkList() {
        afkList.clear();
    }

    private List<String> worldList = new ArrayList<>();

    public List<String> getWorldList() {
        return worldList;
    }

    public void setWorldList(List<String> worldList) {
        this.worldList = worldList;
    }

    public File dataFolder = this.getDataFolder();

    private final double leftClickModifier = getConfig().getDouble("Modules.Pulse.Settings.Modifiers.LEFT_CLICK");

    public Double getLeftClickModifier() {
        return leftClickModifier;
    }

    private final double jumpModifier = getConfig().getDouble("Modules.Pulse.Settings.Modifiers.JUMP");

    public Double getJumpModifier() {
        return jumpModifier;
    }

    private final double jumpSprintModifier = getConfig().getDouble("Modules.Pulse.Settings.Modifiers.JUMP_SPRINT");

    public Double getJumpSprintModifier() {
        return jumpSprintModifier;
    }

    private final double moveModifier = getConfig().getDouble("Modules.Pulse.Settings.Modifiers.MOVE");

    public Double getMoveModifier() {
        return moveModifier;
    }

    private final double moveLower80Modifier = getConfig().getDouble("Modules.Pulse.Settings.Modifiers.MOVE_LOWER_80");

    public Double getMoveLower80Modifier() {
        return moveLower80Modifier;
    }

    private final double sprintModifier = getConfig().getDouble("Modules.Pulse.Settings.Modifiers.SPRINT");

    public Double getSprintModifier() {
        return sprintModifier;
    }

    private final double stillHigher80Modifier = getConfig().getDouble("Modules.Pulse.Settings.Modifiers.STILL_HIGHER_80");

    public Double getStillHigher80Modifier() {
        return stillHigher80Modifier;
    }

    private final double stillLower80Modifier = getConfig().getDouble("Modules.Pulse.Settings.Modifiers.STILL_LOWER_80");

    public Double getStillLower80Modifier() {
        return stillLower80Modifier;
    }

    private final double stillLower60Modifier = getConfig().getDouble("Modules.Pulse.Settings.Modifiers.STILL_LOWER_60");

    public Double getStillLower60Modifier() {
        return stillLower60Modifier;
    }

    private void initModules() {
        if (getConfig().getBoolean("Modules.Pulse.Enabled")) {
            sendConsoleMessage("[BlockyLife] Loading Pulse module..");
            sendConsoleMessage("[BlockyLife] Loading display..");
            if (Objects.equals(getConfig().getString("Modules.Pulse.Settings.ValueDisplay"), "ActionBar")) {
                new ActionBarDisplay(this, bukkitScheduler).runDisplay();
            }
            sendConsoleMessage("[BlockyLife] Loading checkers..");
            new PulseChecker(this, bukkitScheduler, new PulseModule()).runChecker();
            new PlayerActivityChecker(this, bukkitScheduler).runChecker();
            new AFKChecker().runChecker(this, bukkitScheduler);
            sendConsoleMessage("[BlockyLife] Loading listeners..");
            registerListener(new PlayerChatListener(this), new PlayerDeathListener(this), new PlayerQuitListener(this), new PlayerMoveListener(this), new PlayerJoinListener(this), new PlayerKickListener(this), new PlayerInteractListener(this), new EntityDamageByEntityListener(this));
            sendConsoleMessage("[BlockyLife] Success!");
        }
        if (getConfig().getBoolean("Modules.Time.Enabled")) {
            sendConsoleMessage("[BlockyLife] Loading Time module...");
            new TimeModule(this, bukkitScheduler).startModule();
            sendConsoleMessage("[BlockyLife] Success!");
        }
    }

    private void initPlaceholders() {
        sendConsoleMessage("[BlockyLife] Loading placeholders...");
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new PlaceholderAPI(this).register();
        } else
            sendConsoleMessage("[BlockyLife] PlaceholderAPI is not installed");
    }

    private void initWorlds() {
        sendConsoleMessage("[BlockyLife] Loading worlds...");
        new WorldManager(this).loadWorlds();
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();
        new ConfigManager(this).updateConfig();
        reloadConfig();
        initWorlds();
        initModules();
        initPlaceholders();
    }

    @Override
    public void onDisable() {
        if (!Bukkit.getServer().getOnlinePlayers().isEmpty()) {
            for (final Player p : Bukkit.getOnlinePlayers()) {
                UUID playerUUID = p.getUniqueId();
                PlayerDataManager.saveData(playerUUID, this);
                this.removePulse(playerUUID);
            }
        }
        clearAfkList();
    }

    public void sendActionBarMessage(@NotNull Player player, String message) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
    }

    public void sendBossBarMessage(@NotNull Player player, String message) {
    }

    public void sendConsoleMessage(String message) {
        Bukkit.getConsoleSender().sendMessage(message);
    }

    private void registerCommand(String name, CommandExecutor commandExecutor) {
        Objects.requireNonNull(getCommand(name)).setExecutor(commandExecutor);
    }

    private void registerListener(Listener @NotNull ... listeners) {
        for (final Listener listener : listeners) {
            Bukkit.getPluginManager().registerEvents(listener, this);
        }
    }

    public static String translateMessage(String messageString) {
        Pattern messagePattern = Pattern.compile("#[a-fA-F0-9]{6}");
        Matcher messageMatcher = messagePattern.matcher(messageString);
        while (messageMatcher.find()) {
            String color = messageString.substring(messageMatcher.start(), messageMatcher.end());
            messageString = messageString.replace(color, ChatColor.of(color) + "");
            messageMatcher = messagePattern.matcher(messageString);
        }
        return ChatColor.translateAlternateColorCodes('&', messageString);
    }
}