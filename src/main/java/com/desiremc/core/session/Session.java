package com.desiremc.core.session;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Indexed;
import org.mongodb.morphia.annotations.Property;
import org.mongodb.morphia.annotations.Transient;

import com.desiremc.core.DesireCore;
import com.desiremc.core.punishment.Punishment;
import com.desiremc.core.punishment.Punishment.Type;

public class Session
{

    @Id
    private UUID uuid;

    @Indexed
    private String name;

    private Rank rank;

    @Indexed
    private String ip;

    @Property("first_login")
    private long firstLogin;

    @Property("last_login")
    private long lastLogin;

    @Property("total_played")
    private long totalPlayed;

    private List<String> achievements;

    private List<UUID> friends;

    private List<UUID> incomingFriendRequests;

    private List<UUID> outgoingFriendRequests;

    @Transient
    private List<Punishment> activePunishments;

    private int tokens;

    public Session()
    {
        activePunishments = new LinkedList<>();
        friends = new LinkedList<>();
        incomingFriendRequests = new LinkedList<>();
        outgoingFriendRequests = new LinkedList<>();
        achievements = new LinkedList<>();
    }

    public Player getPlayer()
    {
        return Bukkit.getPlayer(uuid);
    }

    public UUID getUniqueId()
    {
        return uuid;
    }

    public void setUniqueId(UUID uuid)
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
        for (Punishment p : activePunishments)
        {
            if (p.getType() == Type.BAN)
            {
                if (!p.isRepealed()) { return p; }
            }
        }
        return null;
    }

    public Punishment isMuted()
    {
        for (Punishment p : activePunishments)
        {
            if (p.getType() == Type.MUTE) { return p; }
        }
        return null;
    }

    public List<UUID> getFriends()
    {
        return friends;
    }

    public void setFriends(List<UUID> friends)
    {
        this.friends = friends;
    }

    public List<UUID> getIncomingFriendRequests()
    {
        return incomingFriendRequests;
    }

    public void setIncomingFriendRequests(List<UUID> incomingFriendRequests)
    {
        this.incomingFriendRequests = incomingFriendRequests;
    }

    public List<UUID> getOutgoingFriendRequests()
    {
        return outgoingFriendRequests;
    }

    public void setOutgoingFriendRequests(List<UUID> outgoingFriendRequests)
    {
        this.outgoingFriendRequests = outgoingFriendRequests;
    }

    public long getFirstLogin()
    {
        return firstLogin;
    }

    public void setFirstLogin(long firstLogin)
    {
        this.firstLogin = firstLogin;
    }

    public long getLastLogin()
    {
        return lastLogin;
    }

    public void setLastLogin(long lastLogin)
    {
        this.lastLogin = lastLogin;
    }

    public long getTotalPlayed()
    {
        return totalPlayed;
    }

    public void setTotalPlayed(long totalPlayed)
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
    }

    public List<String> getAchievements()
    {
        return achievements;
    }

    public void setAchievements(List<String> achievements)
    {
        this.achievements = achievements;
    }

    public boolean hasAchievement(String string)
    {
        for (String achievement : achievements)
        {
            if (achievement.equalsIgnoreCase(string)) { return true; }
        }
        return false;
    }

    public void awardAchievement(Achievement achievement, boolean inform)
    {
        if (hasAchievement(achievement.getId())) return;

        getAchievements().add(achievement.getId());

        if (inform)
        {
            Player player = DesireCore.getInstance().getServer().getPlayer(uuid);
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
            tokens += achievement.getReward();
        }
        SessionHandler.getInstance().save(this);
    }

    public int getTokens()
    {
        return tokens;
    }

    public void addTokens(int tokens, boolean notify)
    {
        this.tokens += tokens;
        if (notify)
        {
            DesireCore.getLangHandler().sendRenderMessage(getPlayer(), "tokens.add", "{tokens}", tokens + "");
        }
        SessionHandler.getInstance().save(this);
    }

    public void setTokens(int tokens)
    {
        this.tokens = tokens;
    }
}
