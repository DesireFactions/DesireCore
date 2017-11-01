package com.desiremc.core.session;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.IdGetter;
import org.mongodb.morphia.annotations.Indexed;
import org.mongodb.morphia.annotations.Property;
import org.mongodb.morphia.annotations.Reference;
import org.mongodb.morphia.annotations.Transient;

import com.desiremc.core.DesireCore;
import com.desiremc.core.fanciful.FancyMessage;
import com.desiremc.core.punishment.Punishment;
import com.desiremc.core.punishment.Punishment.Type;
import com.desiremc.core.utils.PlayerUtils;
import com.desiremc.core.utils.StringUtils;

@Entity(value = "sessions", noClassnameStored = true)
public class Session
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

    private List<Achievement> achievements;

    @Reference
    private List<Session> friends;

    @Embedded("incoming_friend_requests")
    private List<Session> incomingFriendRequests;

    @Embedded("outgoing_friend_requests")
    private List<Session> outgoingFriendRequests;

    private int tokens;

    @Embedded
    private SessionSettings settings;

    @Transient
    private List<Punishment> activePunishments;

    @Transient
    private Player player;

    public Session()
    {
        activePunishments = new LinkedList<>();
        friends = new LinkedList<>();
        incomingFriendRequests = new LinkedList<>();
        outgoingFriendRequests = new LinkedList<>();
        achievements = new LinkedList<>();
        settings = new SessionSettings();
        ipList = new LinkedList<>();
        nameList = new LinkedList<>();
    }

    public Player getPlayer()
    {
        if (player == null)
        {
            player = PlayerUtils.getPlayer(uuid);
        }
        if (player == null || !player.isOnline())
        {
            throw new IllegalStateException("Player is offline.");
        }
        return player;
    }

    public OfflinePlayer getOfflinePlayer()
    {
        if (player != null)
        {
            return player;
        }
        OfflinePlayer op = Bukkit.getOfflinePlayer(getUniqueId());
        if (op == null)
        {
            return null;
        }
        if (op.isOnline())
        {
            player = (Player) op;
        }
        return op;
    }

    public void setPlayer(Player player)
    {
        this.player = player;
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
        this.firstLogin = System.currentTimeMillis();
        this.lastLogin = System.currentTimeMillis();
        this.totalPlayed = 0;
        this.ip = ip;
    }

    protected void assignConsole()
    {
        this.uuid = DesireCore.getConsoleUUID();
        this.name = "CONSOLE";
        this.rank = Rank.OWNER;
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
        save();
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
        for (Punishment p : activePunishments)
        {
            if (p.getType() == Type.BAN)
            {
                if (!p.isRepealed())
                {
                    return p;
                }
            }
        }
        return null;
    }

    public Punishment isMuted()
    {
        for (Punishment p : activePunishments)
        {
            if (p.getType() == Type.MUTE)
            {
                return p;
            }
        }
        return null;
    }

    public List<Session> getFriends()
    {
        return friends;
    }

    public List<Session> getIncomingFriendRequests()
    {
        return incomingFriendRequests;
    }

    public List<Session> getOutgoingFriendRequests()
    {
        return outgoingFriendRequests;
    }

    public boolean hasIncomingFriendRequest(Session session)
    {
        return incomingFriendRequests.contains(session);
    }

    public boolean hasOutgoingFriendRequest(Session session)
    {
        return outgoingFriendRequests.contains(session);
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

    public boolean hasAchievement(Achievement achievement)
    {
        for (Achievement a : achievements)
        {
            if (achievement == a)
            {
                return true;
            }
        }
        return false;
    }

    public void awardAchievement(Achievement achievement, boolean inform)
    {
        if (hasAchievement(achievement))
        {
            return;
        }

        getAchievements().add(achievement);
        save();

        Player player = DesireCore.getInstance().getServer().getPlayer(uuid);

        if (inform)
        {
            DesireCore.getLangHandler().sendRenderMessage(player, "achievement.award.header");
            DesireCore.getLangHandler().sendRenderMessage(player, "achievement.award.title", true);
            DesireCore.getLangHandler().sendRenderMessage(player, "achievement.award.name", true);
            DesireCore.getLangHandler().sendRenderMessage(player, "achievement.award.desc", true);
            if (achievement.getReward() > 0)
            {
                DesireCore.getLangHandler().sendRenderMessage(player, "achievement.award.reward", true);
            }
            DesireCore.getLangHandler().sendRenderMessage(player, "achievement.award.header");
        }

        if (achievement.getReward() > 0)
        {
            addTokens(achievement.getReward(), false);
        }

        FancyMessage nessage = new FancyMessage(DesireCore.getLangHandler().getPrefix() + " " + player.getName() + " " +
                "has received the achievement ")
                        .color(ChatColor.WHITE)
                        .then(achievement.getName())
                        .tooltip(achievement.getName(), achievement.getDescription(), "Tokens: " + achievement.getReward())
                        .color(ChatColor.WHITE);

        for (Player target : Bukkit.getOnlinePlayers())
        {
            nessage.send(target);
        }
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
            DesireCore.getLangHandler().sendRenderMessage(getPlayer(), "tokens.add", "{tokens}", tokens + "");
        }
    }

    public void setTokens(int tokens)
    {
        this.tokens = tokens;
        save();
    }

    public SessionSettings getSettings()
    {
        return settings;
    }

    public void setSettings(SessionSettings settings)
    {
        this.settings = settings;
        save();
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

    private void save()
    {
        SessionHandler.getInstance().save(this);
    }

}
