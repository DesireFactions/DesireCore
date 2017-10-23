package com.desiremc.core.session;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
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
import com.desiremc.core.utils.PlayerUtils;

@Entity(value = "hcf_sessions", noClassnameStored = true)
public class HCFSession
{

    @Id
    private UUID uuid;

    @Property("safe_timer")
    private long safeTimer;

    private int lives;

    @Embedded
    private Map<String, List<DeathBan>> deathBans;

    @Embedded
    private Map<String, List<Ticker>> kills;

    @Embedded
    private Map<String, List<Ticker>> deaths;

    @Transient
    private Session session;

    @Transient
    private PVPTimer pvpTimer;

    @Transient
    private PVPClass pvpClass;

    public HCFSession()
    {
        pvpTimer = new PVPTimer();
        kills = new HashMap<>();
        deaths = new HashMap<>();
        deathBans = new HashMap<>();
    }

    protected void assignDefault(UUID uuid)
    {
        this.uuid = uuid;
        this.safeTimer = DesireCore.getConfigHandler().getInteger("timers.pvp.time");
    }

    public Rank getRank()
    {
        return session.getRank();
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

    protected void setUniqueId(UUID uuid)
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
        save();
    }

    public int getLives()
    {
        return lives;
    }

    public void setLives(int lives)
    {
        this.lives = lives;
        save();
    }

    public void takeLives(int lives)
    {
        this.lives -= lives;
        save();
    }

    public void addLives(int lives)
    {
        this.lives += lives;
        save();
    }

    public int getTotalKills(String server)
    {
        List<Ticker> local = kills.get(server);
        if (local == null)
        {
            kills.put(server, new LinkedList<>());
            return 0;
        }
        int count = 0;
        for (Ticker ticker : local)
        {
            count += ticker.getCount();
        }
        return count;
    }

    public int getTotalDeaths(String server)
    {
        List<Ticker> local = deaths.get(server);
        if (local == null)
        {
            deaths.put(server, new LinkedList<>());
            return 0;
        }
        int count = 0;
        for (Ticker ticker : local)
        {
            count += ticker.getCount();
        }
        return count;
    }

    public void addKill(String server, UUID victim)
    {
        List<Ticker> local = kills.get(server);
        if (local == null)
        {
            local = new LinkedList<>();
            kills.put(server, local);
        }
        for (Ticker tick : local)
        {
            if (tick.getUniqueId().equals(victim))
            {
                tick.setCount(tick.getCount());
                return;
            }
        }
        local.add(new Ticker(victim));
        save();
    }

    public void addDeath(String server, UUID killer)
    {
        System.out.println("addDeath() called with server " + server + " and killer " + (killer == null ? "null" : killer.toString()) + ".");
        List<DeathBan> bans = deathBans.get(server);
        if (bans == null)
        {
            bans = new LinkedList<>();
            deathBans.put(server, bans);
        }
        bans.add(new DeathBan(System.currentTimeMillis()));
        save();

        if (killer != null)
        {
            List<Ticker> local = deaths.get(server);
            if (local == null)
            {
                local = new LinkedList<>();
                deaths.put(server, local);
            }
            for (Ticker tick : local)
            {
                if (tick.getUniqueId().equals(killer))
                {
                    tick.setCount(tick.getCount());
                    save();
                    return;
                }
            }
            local.add(new Ticker(killer));
            save();
        }
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
        save();
    }

    public DeathBan getActiveDeathBan(String server)
    {
        System.out.println("getActiveDeathBan() called.");
        System.out.println("getActiveDeathBan() rank time = " + session.getRank().getDeathBanTime());
        List<DeathBan> bans = deathBans.get(server);
        if (bans == null)
        {
            return null;
        }
        System.out.println("getActiveDeathBan() found for server " + server + ".");
        for (DeathBan ban : bans)
        {
            System.out.println("getActiveDeathBan() loop with values " + ban.getStartTime() + " and " + ban.isRevived());
            if (!ban.isRevived() && ban.getStartTime() + session.getRank().getDeathBanTime() > System.currentTimeMillis())
            {
                System.out.println("getActiveDeathBan() returned ban.");
                return ban;
            }
        }
        System.out.println("getActiveDeathBan() returned null.");
        return null;
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

    public String[] getKillDisplay(String server)
    {
        List<Ticker> kills = this.kills.get(server);
        if (kills == null)
        {
            return new String[] {};
        }
        Collections.sort(kills);
        String[] array = new String[kills.size()];
        int i = 0;
        for (Ticker tick : kills)
        {
            array[i] = PlayerUtils.getName(tick.getUniqueId()) + " x" + tick.getCount();
        }
        return array;
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

    public PVPClass getPvpClass()
    {
        return pvpClass;
    }

    public void setPvpClass(PVPClass pvpClass)
    {
        this.pvpClass = pvpClass;
    }

    private void save()
    {
        HCFSessionHandler.getInstance().save(this);
    }

}
