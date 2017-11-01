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
import com.desiremc.core.bungee.StatusManager;
import com.desiremc.core.commands.AchievementCommand;
import com.desiremc.core.commands.BanCommand;
import com.desiremc.core.commands.InfoCommand;
import com.desiremc.core.commands.TempBanCommand;
import com.desiremc.core.commands.UnbanCommand;
import com.desiremc.core.commands.WarnCommand;
import com.desiremc.core.commands.alerts.AlertsCommand;
import com.desiremc.core.commands.auth.AuthLoginCommand;
import com.desiremc.core.commands.auth.AuthResetKeyCommand;
import com.desiremc.core.commands.chat.ChatCommand;
import com.desiremc.core.commands.friends.FriendsCommand;
import com.desiremc.core.commands.rank.RankCommand;
import com.desiremc.core.commands.report.ReportCommand;
import com.desiremc.core.commands.staff.StaffCommand;
import com.desiremc.core.commands.ticket.TicketCommand;
import com.desiremc.core.connection.MongoWrapper;
import com.desiremc.core.gui.MenuAPI;
import com.desiremc.core.listeners.AuthListener;
import com.desiremc.core.listeners.ConnectionListener;
import com.desiremc.core.listeners.InventoryListener;
import com.desiremc.core.listeners.ListenerManager;
import com.desiremc.core.listeners.PlayerListener;
import com.desiremc.core.listeners.StaffListener;
import com.desiremc.core.punishment.PunishmentHandler;
import com.desiremc.core.report.ReportHandler;
import com.desiremc.core.scoreboard.EntryRegistry;
import com.desiremc.core.scoreboard.ScoreboardRegistry;
import com.desiremc.core.session.SessionHandler;
import com.desiremc.core.staff.GadgetHandler;
import com.desiremc.core.staff.StaffHandler;
import com.desiremc.core.tablist.TabList;
import com.desiremc.core.tickets.TicketHandler;
import com.desiremc.core.utils.ItemDb;
import com.desiremc.core.utils.ReflectionUtils.NMSClasses;
import com.desiremc.core.utils.ReflectionUtils.NMSFields;

public class DesireCore extends JavaPlugin
{

    public static final boolean DEBUG = false;

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

        NMSClasses.initialize();
        NMSFields.initialize();
        PunishmentHandler.initialize();
        ScoreboardRegistry.initialize();
        EntryRegistry.initialize();
        MenuAPI.initialize();
        ListenerManager.initialize();
        CustomCommandHandler.initialize();
        SessionHandler.initialize();
        StaffHandler.initialize();
        TicketHandler.initialize();
        StatusManager.startPingTask();
        ReportHandler.initialize();
        GadgetHandler.initialize();

        mongoWrapper.getDatastore().ensureIndexes();

        registerCommands();
        registerListeners();

        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

        for (Player p : Bukkit.getOnlinePlayers())
        {
            Bukkit.getPluginManager().callEvent(new PlayerJoinEvent(p, ""));
        }
    }

    public static ClassLoader getLoader()
    {
        return instance.getClassLoader();
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
        customCommandHandler.registerCommand(new ChatCommand());
        customCommandHandler.registerCommand(new TempBanCommand());
        customCommandHandler.registerCommand(new BanCommand());
        customCommandHandler.registerCommand(new UnbanCommand());
        customCommandHandler.registerCommand(new WarnCommand());
        customCommandHandler.registerCommand(new AchievementCommand());
        customCommandHandler.registerCommand(new AuthLoginCommand());
        customCommandHandler.registerCommand(new TicketCommand());
        customCommandHandler.registerCommand(new AuthResetKeyCommand());
    }

    private void registerListeners()
    {
        ListenerManager listenerManager = ListenerManager.getInstace();
        listenerManager.addListener(new ConnectionListener());
        listenerManager.addListener(new PlayerListener());
        listenerManager.addListener(new InventoryListener());
        listenerManager.addListener(new AuthListener());
        listenerManager.addListener(new TabList());
        listenerManager.addListener(new StaffListener());
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
