package com.desiremc.core.session;

import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.IdGetter;
import org.mongodb.morphia.annotations.Indexed;
import org.mongodb.morphia.annotations.Property;
import org.mongodb.morphia.annotations.Transient;

import com.desiremc.core.DesireCore;
import com.desiremc.core.Messageable;
import com.desiremc.core.fanciful.FancyMessage;
import com.desiremc.core.punishment.Punishment;
import com.desiremc.core.punishment.Punishment.Type;
import com.desiremc.core.punishment.PunishmentHandler;
import com.desiremc.core.utils.PlayerUtils;
import com.desiremc.core.utils.StringUtils;

@Entity(value = "sessions", noClassnameStored = true)
public class Session implements Messageable
{

    @Id
    private UUID uuid;

    @Indexed
    private String name;

    private List<String> nameList;

    private Rank rank;

    @Indexed
    private String ip;

    private List<String> ipList;

    @Transient
    private boolean updatedIp;

    @Property("first_login")
    private long firstLogin;

    @Property("last_login")
    private long lastLogin;

    @Property("total_played")
    private long totalPlayed;

    @Property("auth_key")
    private String authKey;

    @Property("has_authorized")
    private boolean hasAuthorized;

    @Property("has_authorized_ip")
    private boolean hasAuthorizedIP;

    private List<Achievement> achievements;

    private List<UUID> ignoring;

    private List<UUID> friends;

    @Property("incoming_friend_requests")
    private List<UUID> incomingFriendRequests;

    @Property("outgoing_friend_requests")
    private List<UUID> outgoingFriendRequests;

    private int tokens;

    private Set<SessionSetting> settings;

    @Transient
    private List<Punishment> activePunishments;

    @Transient
    private Player player;

    @Transient
    private boolean online;

    private SessionType sessionType;

    public Session()
    {
        activePunishments = new LinkedList<>();
        friends = new LinkedList<>();
        incomingFriendRequests = new LinkedList<>();
        outgoingFriendRequests = new LinkedList<>();
        achievements = new LinkedList<>();
        settings = EnumSet.noneOf(SessionSetting.class);
        ipList = new LinkedList<>();
        nameList = new LinkedList<>();
        ignoring = new LinkedList<>();
    }

    /**
     * @return {@code true} if this {@link Session} represents a {@link Player}.
     */
    public boolean isPlayer()
    {
        return sessionType == SessionType.PLAYER;
    }

    /**
     * @return {@code true} if this {@link Session} represents the {@link ConsoleCommandSender}.
     */
    public boolean isConsole()
    {
        return sessionType == SessionType.CONSOLE;
    }

    /**
     * Gets the {@link Player} associated with this Session. If this is the console's session, it will throw an
     * {@link IllegalStateException}.
     *
     * @return the {@link Player} of this session.
     */
    public Player getPlayer()
    {
        if (!isPlayer())
        {
            throw new IllegalStateException("Can't retrieve Player of Console.");
        }
        if (!isOnline())
        {
            throw new IllegalStateException("Can't retrieve Player of offline Session.");
        }

        if (player == null)
        {
            player = PlayerUtils.getPlayer(uuid);
        }

        if (isOnline() && !player.isOnline())
        {
            throw new IllegalStateException("Trying to get player before the player has logged in.");
        }

        return player;
    }

    /**
     * Checks if the player is online.
     *
     * @return {@code true} if the player is online. {@code false} otherwise.
     */
    public boolean isOnline()
    {
        return online;
    }

    /**
     * Sets whether this player is online or offline.
     *
     * @param online the online state.
     */
    public void setOnline(boolean online)
    {
        this.online = online;
        this.player = null;
    }

    /**
     * Get the CommandSender associated with this Session. Using this instead of getPlayer makes it safe to always use
     * this, even when dealing with the console sending commands.
     *
     * @return the {@link CommandSender} of this session.
     */
    public CommandSender getSender()
    {
        if (isConsole())
        {
            return Bukkit.getConsoleSender();
        }
        else
        {
            return getPlayer();
        }
    }

    /**
     * Convenience method for {@link CommandSender#sendMessage(String)}. The message will not be sent if the message is
     * null or if the session represents a {@link Player} and they are offline.
     *
     * @param message the message to send to the sender.
     */
    public void sendMessage(String message)
    {
        if (message == null)
        {
            return;
        }
        if (isPlayer() && !isOnline())
        {
            return;
        }
        getSender().sendMessage(message);
    }

    @IdGetter
    public UUID getUniqueId()
    {
        return uuid;
    }

    protected void assignDefaults(UUID uuid, String name, String ip)
    {
        this.uuid = uuid;
        this.name = name;
        this.rank = Rank.GUEST;
        this.sessionType = SessionType.PLAYER;
        this.firstLogin = System.currentTimeMillis();
        this.lastLogin = System.currentTimeMillis();
        this.totalPlayed = 0;
        this.assignDefaultSettings();
        this.ip = ip;
    }

    protected void applyValues(Session session)
    {
        name = session.name;
        nameList = session.nameList;
        rank = session.rank;
        ip = session.ip;
        ipList = session.ipList;
        firstLogin = session.firstLogin;
        lastLogin = session.lastLogin;
        totalPlayed = session.totalPlayed;
        authKey = session.authKey;
        hasAuthorized = session.hasAuthorized;
        hasAuthorizedIP = session.hasAuthorizedIP;
        achievements = session.achievements;
        ignoring = session.ignoring;
        friends = session.friends;
        incomingFriendRequests = session.incomingFriendRequests;
        outgoingFriendRequests = session.outgoingFriendRequests;
        tokens = session.tokens;
        settings = session.settings;
        sessionType = session.sessionType;
    }

    /**
     * Set all the default settings. This will clear the old settings. Do not use on an existing session.
     */
    protected void assignDefaultSettings()
    {
        if (settings == null || settings.size() != 0)
        {
            this.settings = EnumSet.noneOf(SessionSetting.class);
        }
        List<SessionSetting> settings = SessionSetting.enabledValues();
        settings.removeIf(setting -> !setting.getDefaultValue());
        this.settings.addAll(settings);
    }

    protected void assignConsole()
    {
        this.uuid = DesireCore.getConsoleUUID();
        this.name = "CONSOLE";
        this.rank = Rank.OWNER;
        this.sessionType = SessionType.CONSOLE;
    }

    protected void setUniqueId(UUID uuid)
    {
        this.uuid = uuid;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
        save();
    }

    public Rank getRank()
    {
        return rank;
    }

    public void setRank(Rank rank)
    {
        this.rank = rank;
    }

    public List<Punishment> getActivePunishments()
    {
        return activePunishments;
    }

    public void setActivePunishments(List<Punishment> activePunishments)
    {
        this.activePunishments = activePunishments;
    }

    public Punishment isBanned()
    {
        return PunishmentHandler.getInstance().getPunishment(uuid, Type.BAN);
    }

    public Punishment isIPBanned()
    {
        return PunishmentHandler.getInstance().getPunishment(uuid, Type.IP_BAN);
    }

    public Punishment isMuted()
    {
        return PunishmentHandler.getInstance().getPunishment(uuid, Type.MUTE);
    }

    public List<UUID> getFriends()
    {
        return friends;
    }

    public List<UUID> getIncomingFriendRequests()
    {
        return incomingFriendRequests;
    }

    public List<UUID> getOutgoingFriendRequests()
    {
        return outgoingFriendRequests;
    }

    public boolean hasIncomingFriendRequest(UUID uuid)
    {
        return incomingFriendRequests.contains(uuid);
    }

    public boolean hasOutgoingFriendRequest(UUID uuid)
    {
        return outgoingFriendRequests.contains(uuid);
    }

    public long getFirstLogin()
    {
        return firstLogin;
    }

    public long getLastLogin()
    {
        return lastLogin;
    }

    protected void setLastLogin(long lastLogin)
    {
        this.lastLogin = lastLogin;
    }

    public long getTotalPlayed()
    {
        return totalPlayed;
    }

    protected void setTotalPlayed(long totalPlayed)
    {
        this.totalPlayed = totalPlayed;
    }

    public String getIp()
    {
        return ip;
    }

    public void setIp(String ip)
    {
        this.ip = ip;
        this.updatedIp = true;
        save();
    }

    public boolean hasNewIp()
    {
        return updatedIp;
    }

    public List<Achievement> getAchievements()
    {
        return achievements;
    }

    /**
     * @param achievement the achievement
     * @return {@code true} if the player has the achievement.
     */
    public boolean hasAchievement(Achievement achievement)
    {
        return achievements.contains(achievement);
    }

    /**
     * Give a player an achievement as well as reward them with the tokens.
     *
     * @param achievement the achievement.
     * @param inform whether to inform the player or not.
     */
    public void awardAchievement(Achievement achievement, boolean inform)
    {
        if (hasAchievement(achievement))
        {
            return;
        }

        getAchievements().add(achievement);
        save();

        if (inform)
        {
            DesireCore.getLangHandler().sendRenderMessage(this, "achievement.award.header", false, true);
            DesireCore.getLangHandler().sendRenderMessage(this, "achievement.award.title", false, true);
            DesireCore.getLangHandler().sendRenderMessage(this, "achievement.award.name", false, true, "{name}", achievement.getName());
            DesireCore.getLangHandler().sendRenderMessage(this, "achievement.award.desc", false, true, "{desc}", achievement.getDescription());
            if (achievement.getReward() > 0)
            {
                DesireCore.getLangHandler().sendRenderMessage(this, "achievement.award.reward", false, true, "{reward}", achievement.getReward());
            }
            DesireCore.getLangHandler().sendRenderMessage(this, "achievement.award.header", false, true);
        }

        if (achievement.getReward() > 0)
        {
            addTokens(achievement.getReward(), false);
        }

        FancyMessage message = new FancyMessage(DesireCore.getLangHandler().getPrefix())
                .then(" " + player.getName() + " has earned the achievement ")
                .color(ChatColor.WHITE)
                .then(achievement.getName())
                .tooltip(achievement.getName(), achievement.getDescription(), "Tokens: " + achievement.getReward())
                .color(ChatColor.LIGHT_PURPLE);

        message.send(Bukkit.getOnlinePlayers());
    }

    public int getTokens()
    {
        return tokens;
    }

    public void addTokens(int tokens, boolean notify)
    {
        this.tokens += tokens;
        save();
        if (notify)
        {
            DesireCore.getLangHandler().sendRenderMessage(getPlayer(), "tokens.add", true, false, "{tokens}", tokens + "");
        }
    }

    public void takeTokens(int tokens, boolean notify)
    {
        this.tokens -= Math.min(tokens, this.tokens);
        save();
        if (notify)
        {
            DesireCore.getLangHandler().sendRenderMessage(getPlayer(), "tokens.taken", true, false, "{tokens}", tokens + "");
        }
    }

    public void setTokens(int tokens)
    {
        this.tokens = tokens;
        save();
    }

    public Set<SessionSetting> getSettings()
    {
        return settings;
    }

    public void setSetting(SessionSetting setting, boolean status)
    {
        if (status)
        {
            settings.add(setting);
        }
        else
        {
            settings.remove(setting);
        }
    }

    /**
     * @param setting the setting to check.
     * @return the new value of the setting.
     */
    public boolean toggleSetting(SessionSetting setting)
    {
        if (settings.contains(setting))
        {
            this.settings.remove(setting);
            return false;
        }
        else
        {
            this.settings.add(setting);
            return true;
        }
    }

    /**
     * @param setting the setting to check.
     * @return {@code true} if the setting is enabled.
     */
    public boolean getSetting(SessionSetting setting)
    {
        return settings.contains(setting);
    }

    public void setAuthKey(String key)
    {
        this.authKey = key;
        save();
    }

    public String getAuthkey()
    {
        return this.authKey;
    }

    public boolean hasAuthKey()
    {
        return !StringUtils.isNullOrEmpty(authKey);
    }

    public List<String> getIpList()
    {
        return ipList;
    }

    public List<String> getNameList()
    {
        return nameList;
    }

    public void setHasAuthorized(boolean hasAuthorized)
    {
        this.hasAuthorized = hasAuthorized;
        save();
    }

    public boolean hasAuthorized()
    {
        return hasAuthorized;
    }

    public void setHasAuthorizedIP(boolean hasAuthorizedIP)
    {
        this.hasAuthorizedIP = hasAuthorizedIP;
        save();
    }

    public boolean hasAuthorizedIP()
    {
        return hasAuthorizedIP;
    }

    public boolean isIgnoring(UUID uuid)
    {
        return ignoring.contains(uuid);
    }

    public void ignore(UUID uuid)
    {
        ignoring.add(uuid);
        save();
    }

    public void unignore(UUID uuid)
    {
        ignoring.remove(uuid);
        save();
    }

    /**
     * Saves this record to the database asynchronously as to prevent the thread from freezing. Even though it is
     * asynchronous, it should be used sparingly and not on a timer.
     */
    public void save()
    {
        Bukkit.getScheduler().runTaskAsynchronously(DesireCore.getInstance(), new Runnable()
        {
            @Override
            public void run()
            {
                SessionHandler.getInstance().save(Session.this);
            }
        });
    }

}
