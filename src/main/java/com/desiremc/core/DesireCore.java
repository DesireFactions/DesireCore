package com.desiremc.core;

import java.io.File;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.desiremc.core.api.FileHandler;
import com.desiremc.core.api.LangHandler;
import com.desiremc.core.api.command.CustomCommandHandler;
import com.desiremc.core.commands.AchievementCommand;
import com.desiremc.core.commands.BanCommand;
import com.desiremc.core.commands.InfoCommand;
import com.desiremc.core.commands.TempBanCommand;
import com.desiremc.core.commands.UnbanCommand;
import com.desiremc.core.commands.WarnCommand;
import com.desiremc.core.commands.alerts.AlertsCommand;
import com.desiremc.core.commands.friends.FriendsCommand;
import com.desiremc.core.commands.rank.RankCommand;
import com.desiremc.core.commands.report.ReportCommand;
import com.desiremc.core.commands.staff.StaffCommand;
import com.desiremc.core.connection.MongoWrapper;
import com.desiremc.core.gui.MenuAPI;
import com.desiremc.core.listeners.ConnectionListener;
import com.desiremc.core.listeners.InventoryListener;
import com.desiremc.core.listeners.ListenerManager;
import com.desiremc.core.listeners.PlayerListener;
import com.desiremc.core.punishment.PunishmentHandler;
import com.desiremc.core.scoreboard.EntryRegistry;
import com.desiremc.core.scoreboard.ScoreboardRegistry;
import com.desiremc.core.session.SessionHandler;
import com.desiremc.core.session.StaffHandler;
import com.desiremc.core.utils.ItemDb;

public class DesireCore extends JavaPlugin
{
    private static final UUID CONSOLE = UUID.fromString("00000000-0000-0000-0000-000000000000");

    private static String SERVER;

    private static DesireCore instance;

    private static MongoWrapper mongoWrapper;

    private static LangHandler lang;
    private static FileHandler config;
    private static ItemDb itemHandler;

    @Override
    public void onEnable()
    {
        instance = this;

        saveDefaultConfig();
        saveResource("lang.yml", false);
        saveResource("items.csv", false);

        config = new FileHandler(new File(getDataFolder(), "config.yml"), this);
        lang = new LangHandler(new File(getDataFolder(), "lang.yml"), this);
        itemHandler = new ItemDb();

        SERVER = config.getString("SERVER");

        mongoWrapper = new MongoWrapper();

        PunishmentHandler.initialize();
        ScoreboardRegistry.initialize();
        EntryRegistry.initialize();
        MenuAPI.initialize();
        ListenerManager.initialize();
        CustomCommandHandler.initialize();
        SessionHandler.initialize();
        StaffHandler.initialize();
        
        registerCommands();
        registerListeners();

        for (Player p : Bukkit.getOnlinePlayers())
        {
            Bukkit.getPluginManager().callEvent(new PlayerJoinEvent(p, ""));
        }

    }

    private void registerCommands()
    {
        CustomCommandHandler customCommandHandler = CustomCommandHandler.getInstance();
        customCommandHandler.registerCommand(new AlertsCommand());
        customCommandHandler.registerCommand(new FriendsCommand());
        customCommandHandler.registerCommand(new ReportCommand());
        customCommandHandler.registerCommand(new InfoCommand());
        customCommandHandler.registerCommand(new RankCommand());
        customCommandHandler.registerCommand(new StaffCommand());
        customCommandHandler.registerCommand(new TempBanCommand());
        customCommandHandler.registerCommand(new BanCommand());
        customCommandHandler.registerCommand(new UnbanCommand());
        customCommandHandler.registerCommand(new WarnCommand());
        customCommandHandler.registerCommand(new AchievementCommand());
    }

    private void registerListeners()
    {
        ListenerManager listenerManager = ListenerManager.getInstace();
        listenerManager.addListener(new ConnectionListener());
        listenerManager.addListener(new PlayerListener());
        listenerManager.addListener(new InventoryListener());
    }

    public MongoWrapper getMongoWrapper()
    {
        return mongoWrapper;
    }

    public static LangHandler getLangHandler()
    {
        return lang;
    }

    public static FileHandler getConfigHandler()
    {
        return config;
    }

    public static ItemDb getItemHandler()
    {
        return itemHandler;
    }

    public static UUID getConsoleUUID()
    {
        return CONSOLE;
    }

    public static DesireCore getInstance()
    {
        return instance;
    }

    public static String getCurrentServer()
    {
        return SERVER;
    }

}
