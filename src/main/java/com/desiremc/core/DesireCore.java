package com.desiremc.core;

import com.desiremc.core.api.FileHandler;
import com.desiremc.core.api.LangHandler;
import com.desiremc.core.api.command.CustomCommandHandler;
import com.desiremc.core.api.newcommands.CommandHandler;
import com.desiremc.core.bungee.StatusManager;
import com.desiremc.core.commands.InfoCommand;
import com.desiremc.core.commands.PingCommand;
import com.desiremc.core.commands.RenameCommand;
import com.desiremc.core.commands.SuperSlimeCommand;
import com.desiremc.core.commands.TeamSpeakCommand;
import com.desiremc.core.commands.achievement.AchievementCommand;
import com.desiremc.core.commands.auth.AuthCommand;
import com.desiremc.core.commands.auth.LoginCommand;
import com.desiremc.core.commands.chat.ChatCommand;
import com.desiremc.core.commands.friends.FriendsCommand;
import com.desiremc.core.commands.punishment.BanCommand;
import com.desiremc.core.commands.punishment.BlacklistCommand;
import com.desiremc.core.commands.punishment.HistoryCommand;
import com.desiremc.core.commands.punishment.IpbanCommand;
import com.desiremc.core.commands.punishment.KickCommand;
import com.desiremc.core.commands.punishment.MuteCommand;
import com.desiremc.core.commands.punishment.RollbackCommand;
import com.desiremc.core.commands.punishment.TempBanCommand;
import com.desiremc.core.commands.punishment.TempMuteCommand;
import com.desiremc.core.commands.punishment.UnIpbanCommand;
import com.desiremc.core.commands.punishment.UnbanCommand;
import com.desiremc.core.commands.punishment.UnblacklistCommand;
import com.desiremc.core.commands.punishment.UnmuteCommand;
import com.desiremc.core.commands.punishment.WarnCommand;
import com.desiremc.core.commands.rank.RankCommand;
import com.desiremc.core.commands.report.ReportCommand;
import com.desiremc.core.commands.settings.SettingsCommand;
import com.desiremc.core.commands.spawn.SetSpawnCommand;
import com.desiremc.core.commands.spawn.SpawnCommand;
import com.desiremc.core.commands.staff.StaffAltsCommand;
import com.desiremc.core.commands.staff.StaffChatCommand;
import com.desiremc.core.commands.staff.StaffCommand;
import com.desiremc.core.commands.staff.StaffFreezeCommand;
import com.desiremc.core.commands.staff.StaffModeCommand;
import com.desiremc.core.commands.staff.StaffReportsCommand;
import com.desiremc.core.commands.staff.StaffRestoreCommand;
import com.desiremc.core.commands.ticket.TicketCommand;
import com.desiremc.core.commands.timings.TimingsCommand;
import com.desiremc.core.commands.tokens.TokensCommand;
import com.desiremc.core.connection.MongoWrapper;
import com.desiremc.core.gui.MenuAPI;
import com.desiremc.core.handler.CommandBlocker;
import com.desiremc.core.handler.SlowChatHandler;
import com.desiremc.core.listeners.AuthListener;
import com.desiremc.core.listeners.ConnectionListener;
import com.desiremc.core.listeners.GUIListener;
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
import com.desiremc.core.tablist.TabAPI;
import com.desiremc.core.tickets.TicketHandler;
import com.desiremc.core.utils.ItemDb;
import com.desiremc.core.utils.ReflectionUtils.NMSClasses;
import com.desiremc.core.utils.ReflectionUtils.NMSFields;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.SimplePluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.UUID;

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

    private static boolean useTimings;

    @Override
    public void onEnable()
    {
        instance = this;

        saveDefaultConfig();
        saveResource("lang.yml", false);
        saveResource("items.csv", false);

        config = new FileHandler(new File(getDataFolder(), "config.yml"), this);
        lang = new LangHandler(new File(getDataFolder(), "lang.yml"), this);

        useTimings = config.getBoolean("timings");
        ((SimplePluginManager) Bukkit.getPluginManager()).useTimings(useTimings);

        itemHandler = new ItemDb();

        SERVER = config.getString("SERVER");

        mongoWrapper = new MongoWrapper();

        PunishmentHandler.initialize();
        SessionHandler.initialize();
        NMSClasses.initialize();
        NMSFields.initialize();
        ScoreboardRegistry.initialize();
        EntryRegistry.initialize();
        MenuAPI.initialize();
        ListenerManager.initialize();
        CustomCommandHandler.initialize();
        CommandHandler.initialize();
        StaffHandler.initialize();
        TicketHandler.initialize();
        StatusManager.startPingTask();
        ReportHandler.initialize();
        GadgetHandler.initialize();
        TabAPI.initialize();

        mongoWrapper.getDatastore().ensureIndexes();

        registerCommands();
        registerListeners();

        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

        for (Player p : Bukkit.getOnlinePlayers())
        {
            Bukkit.getPluginManager().callEvent(new PlayerJoinEvent(p, ""));
        }
    }

    @Override
    public void onDisable()
    {
        for (Player p : Bukkit.getOnlinePlayers())
        {
            StaffHandler.getInstance().disableStaffMode(p);
        }
    }

    public static ClassLoader getLoader()
    {
        return instance.getClassLoader();
    }

    private void registerCommands()
    {
        CustomCommandHandler customCommandHandler = CustomCommandHandler.getInstance();
        customCommandHandler.registerCommand(new SettingsCommand());
        customCommandHandler.registerCommand(new ReportCommand());
        customCommandHandler.registerCommand(new RankCommand());
        customCommandHandler.registerCommand(new BlacklistCommand());
        customCommandHandler.registerCommand(new UnblacklistCommand());
        customCommandHandler.registerCommand(new StaffCommand());
        customCommandHandler.registerCommand(new TempBanCommand());
        customCommandHandler.registerCommand(new BanCommand());
        customCommandHandler.registerCommand(new UnbanCommand());
        customCommandHandler.registerCommand(new WarnCommand());
        customCommandHandler.registerCommand(new AchievementCommand());
        customCommandHandler.registerCommand(new LoginCommand());
        customCommandHandler.registerCommand(new AuthCommand());
        customCommandHandler.registerCommand(new TimingsCommand());
        customCommandHandler.registerCommand(new StaffChatCommand("sc"));
        customCommandHandler.registerCommand(new StaffFreezeCommand());
        customCommandHandler.registerCommand(new StaffModeCommand("mod", new String[] {"staff", "v"}));
        customCommandHandler.registerCommand(new StaffRestoreCommand("inv"));
        customCommandHandler.registerCommand(new StaffReportsCommand("reports"));
        customCommandHandler.registerCommand(new StaffAltsCommand("alts"));
        customCommandHandler.registerCommand(new IpbanCommand());
        customCommandHandler.registerCommand(new UnIpbanCommand());
        customCommandHandler.registerCommand(new RollbackCommand());
        customCommandHandler.registerCommand(new SuperSlimeCommand());

        CommandHandler commandHandler = CommandHandler.getInstance();
        commandHandler.registerCommand(new KickCommand());
        commandHandler.registerCommand(new InfoCommand());
        commandHandler.registerCommand(new TicketCommand());
        commandHandler.registerCommand(new PingCommand());
        commandHandler.registerCommand(new RenameCommand());
        commandHandler.registerCommand(new TeamSpeakCommand());
        commandHandler.registerCommand(new TokensCommand());
        commandHandler.registerCommand(new MuteCommand());
        commandHandler.registerCommand(new UnmuteCommand());
        commandHandler.registerCommand(new TempMuteCommand());
        commandHandler.registerCommand(new ChatCommand());
        commandHandler.registerCommand(new FriendsCommand());
        commandHandler.registerCommand(new HistoryCommand());
        commandHandler.registerCommand(new SpawnCommand());
        commandHandler.registerCommand(new SetSpawnCommand());
    }

    private void registerListeners()
    {
        ListenerManager listenerManager = ListenerManager.getInstace();
        listenerManager.addListener(new ConnectionListener());
        listenerManager.addListener(new PlayerListener());
        listenerManager.addListener(new AuthListener());
        //listenerManager.addListener(new TabList());
        listenerManager.addListener(new StaffListener());
        listenerManager.addListener(new GUIListener());

        listenerManager.addListener(new SlowChatHandler());
        listenerManager.addListener(new CommandBlocker());
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

    public static WorldEditPlugin getWorldEdit()
    {
        Plugin p = Bukkit.getPluginManager().getPlugin("WorldEdit");
        if (p == null)
        {
            System.out.println("This could would crash if that were to happen.");
            return null;
        }
        return (WorldEditPlugin) p;
    }

    public static boolean toggleTimings()
    {
        useTimings = !useTimings;
        config.setBoolean("timings", useTimings);
        return useTimings;
    }

}
