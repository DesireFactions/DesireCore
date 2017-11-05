package com.desiremc.core.session;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.IdGetter;
import org.mongodb.morphia.annotations.Indexed;
import org.mongodb.morphia.annotations.Property;
import org.mongodb.morphia.annotations.Transient;

import com.desiremc.core.DesireCore;
import com.desiremc.core.utils.PlayerUtils;

@Entity(value = "hcf_sessions", noClassnameStored = true)
public class HCFSession
{

    @Transient
    private static final boolean DEBUG = true;

    @Indexed
    private UUID uuid;

    @Indexed
    String server;

    @Property("safe_timer")
    private long safeTimer;

    private int lives;

    private int diamonds;
    
    private double balance;

    @Embedded
    private List<DeathBan> deathBans;

    @Embedded
    private List<Ticker> kills;

    @Embedded
    private List<Ticker> deaths;

    @Transient
    private Session session;

    @Transient
    private PVPTimer pvpTimer;

    @Transient
    private Player player;

    @Transient
    private PVPClass pvpClass;

    public HCFSession()
    {
        pvpTimer = new PVPTimer();
        kills = new LinkedList<>();
        deaths = new LinkedList<>();
        deathBans = new LinkedList<>();
    }

    protected void assignDefault(UUID uuid, String server)
    {
        this.uuid = uuid;
        this.server = server;
        this.safeTimer = DesireCore.getConfigHandler().getInteger("timers.pvp.time");
    }

    public Rank getRank()
    {
        return session.getRank();
    }

    public Player getPlayer()
    {
        if (player == null)
        {
            player = PlayerUtils.getPlayer(uuid);
        }
        return player;
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

    public int getDiamonds()
    {
        return diamonds;
    }

    public void setDiamonds(int diamonds)
    {
        this.diamonds = diamonds;
        save();
    }

    public void addDiamonds(int diamonds)
    {
        this.diamonds += diamonds;
        save();
    }

    public double getBalance()
    {
        return balance;
    }
    
    public void setBalance(double balance)
    {
        this.balance = balance;
        save();
    }
    
    public void depositBalance(double amount)
    {
        this.balance += amount;
        save();
    }
    
    public void withdrawBalance(double amount)
    {
        this.balance -= amount;
        save();
    }
    
    
    public int getTotalKills()
    {
        int count = 0;
        for (Ticker ticker : kills)
        {
            count += ticker.getCount();
        }
        return count;
    }

    public int getTotalDeaths()
    {
        int count = 0;
        for (Ticker ticker : deaths)
        {
            count += ticker.getCount();
        }
        return count;
    }

    public void addKill(UUID victim)
    {
        for (Ticker tick : kills)
        {
            if (tick.getUniqueId().equals(victim))
            {
                tick.setCount(tick.getCount());
                return;
            }
        }
        kills.add(new Ticker(victim));
        save();
    }

    public void addDeath(UUID killer)
    {
        System.out.println("addDeath() called with server " + server + " and killer " + (killer == null ? "null" : killer.toString()) + ".");

        deathBans.add(new DeathBan(System.currentTimeMillis()));

        if (killer != null)
        {
            for (Ticker tick : deaths)
            {
                if (tick.getUniqueId().equals(killer))
                {
                    tick.setCount(tick.getCount());
                    save();
                    return;
                }
            }
            deaths.add(new Ticker(killer));
            save();
        }
    }

    public int getTokens()
    {
        return session.getTokens();
    }

    public void sendMessage(String message)
    {
        getPlayer().sendMessage(message);
    }

    public PVPTimer getTimer()
    {
        return pvpTimer;
    }

    public boolean hasDeathBan()
    {
        return getActiveDeathBan() != null;
    }

    public long getDeathBanEnd()
    {
        DeathBan ban = getActiveDeathBan();
        return ban != null ? ban.getStartTime() : -1;
    }

    public void revive()
    {
        DeathBan ban = getActiveDeathBan();
        if (ban == null)
        {
            throw new IllegalStateException("Player does not have a deathban.");
        }
        ban.setRevived(true);
        save();
    }

    public DeathBan getActiveDeathBan()
    {
        if (DEBUG)
        {
            System.out.println("getActiveDeathBan() called.");
            System.out.println("getActiveDeathBan() rank time = " + session.getRank().getDeathBanTime());
        }
        for (DeathBan ban : deathBans)
        {
            if (DEBUG)
            {
                System.out.println("getActiveDeathBan() loop with values " + ban.getStartTime() + " and " + ban.isRevived());
            }
            if (!ban.isRevived() && ban.getStartTime() + session.getRank().getDeathBanTime() > System.currentTimeMillis())
            {
                if (DEBUG)
                {
                    System.out.println("getActiveDeathBan() returned ban.");
                }
                return ban;
            }
        }
        if (DEBUG)
        {
            System.out.println("getActiveDeathBan() returned null.");
        }
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

    public String[] getKillDisplay()
    {
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
        save();
    }

    private void save()
    {
        HCFSessionHandler.getInstance().save(this);
    }

}
