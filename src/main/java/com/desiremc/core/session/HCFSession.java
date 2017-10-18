package com.desiremc.core.session;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.IdGetter;
import org.mongodb.morphia.annotations.Property;
import org.mongodb.morphia.annotations.Transient;

import com.desiremc.core.DesireCore;

@Entity(value = "hcf_sessions", noClassnameStored = true)
public class HCFSession
{

    @Id
    private UUID uuid;

    @Property("safe_timer")
    private long safeTimer;

    private int lives;

    private Map<String, Integer> kills;

    private Map<String, Integer> deaths;

    @Embedded
    private Map<String, List<DeathBan>> deathBans;

    @Transient
    private Session session;

    @Transient
    private PVPTimer pvpTimer;

    public HCFSession()
    {
        pvpTimer = new PVPTimer();
        kills = new HashMap<>();
        deaths = new HashMap<>();
        deathBans = new HashMap<>();
    }

    public Player getPlayer()
    {
        return Bukkit.getPlayer(uuid);
    }

    @IdGetter
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
        return session.getName();
    }

    public long getSafeTimeLeft()
    {
        return safeTimer;
    }

    public void setSafeTimeLeft(int safeTimer)
    {
        this.safeTimer = safeTimer;
    }

    public int getLives()
    {
        return lives;
    }

    public void setLives(int lives)
    {
        this.lives = lives;
    }

    public void takeLives(int lives)
    {
        this.lives -= lives;
    }

    public void addLives(int lives)
    {
        this.lives += lives;
    }

    public int getKills(String server)
    {
        Integer save = kills.get(server);
        if (save == null)
        {
            kills.put(server, 0);
            return 0;
        }
        return save;
    }

    public int getDeaths(String server)
    {
        Integer save = deaths.get(server);
        if (save == null)
        {
            deaths.put(server, 0);
            return 0;
        }
        return save;
    }

    public void addKill(String server)
    {
        kills.put(server, getKills(server) + 1);
    }

    public void addDeaths(String server)
    {
        deaths.put(server, getDeaths(server) + 1);
    }

    public int getTokens()
    {
        return session.getTokens();
    }

    public void sendMessage(String message)
    {
        Player p = Bukkit.getPlayer(uuid);
        if (p != null)
        {
            p.sendMessage(message);
        }
    }

    public PVPTimer getTimer()
    {
        return pvpTimer;
    }

    public boolean hasDeathBan(String server)
    {
        return getActiveDeathBan(server) != null;
    }

    public long getDeathBanEnd(String server)
    {
        DeathBan ban = getActiveDeathBan(server);
        if (ban == null)
        {
            throw new IllegalStateException("Player does not have a deathban.");
        }
        return ban != null ? ban.getStartTime() : -1;
    }

    public void revive(String server)
    {
        DeathBan ban = getActiveDeathBan(server);
        if (ban == null)
        {
            throw new IllegalStateException("Player does not have a deathban.");
        }
        ban.setRevived(true);
    }

    private DeathBan getActiveDeathBan(String server)
    {
        List<DeathBan> bans = deathBans.get(server);
        if (bans == null)
        {
            return null;
        }
        for (DeathBan ban : bans)
        {
            if (!ban.revived && ban.getStartTime() + Rank.getDeathBanTime(session.getRank()) > System.currentTimeMillis())
            {
                return ban;
            }
        }
        return null;
    }

    public class PVPTimer implements Runnable
    {

        private long lastRunTime;

        private boolean pause;

        @Override
        public void run()
        {
            if (!pause && safeTimer > 0)
            {
                Bukkit.getScheduler().runTaskLater(DesireCore.getInstance(), this, 5);
            }
            safeTimer -= System.currentTimeMillis() - lastRunTime;
            lastRunTime = System.currentTimeMillis();
        }

        public void pause()
        {
            pause = true;
        }

        public void resume()
        {
            pause = false;
            run();
        }

    }

    @Embedded
    public static class DeathBan
    {
        private long startTime;
        private boolean revived;

        public long getStartTime()
        {
            return startTime;
        }

        public boolean isRevived()
        {
            return revived;
        }

        public void setRevived(boolean revived)
        {
            this.revived = revived;
        }

    }

    public void setSession(Session session)
    {
        this.session = session;
    }

    @Override
    public boolean equals(Object o)
    {
        if (!(o instanceof HCFSession))
        {
            return false;
        }
        return ((HCFSession) o).getUniqueId().equals(uuid);
    }

}
