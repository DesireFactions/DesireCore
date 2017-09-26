package com.desiremc.core.session;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Property;
import org.mongodb.morphia.annotations.Transient;

import com.desiremc.core.DesireCore;

@Entity(value = "players", noClassnameStored = true)
public class HCFSession
{

    @Id
    private UUID uuid;

    @Property("safe_timer")
    private int safeTimer;

    private int lives;

    private Map<String, Integer> kills;
    
    private Map<String, Integer> deaths;

    private Map<String, String> settings;

    @Transient
    private HCFSession session;

    @Transient
    private PVPTimer pvpTimer;

    public HCFSession()
    {
        pvpTimer = new PVPTimer();
        kills = new HashMap<>();
        deaths = new HashMap<>();
        settings = new HashMap<>();
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
        return session.getName();
    }

    public int getSafeTimeLeft()
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

    public int getKills(String server) {
        return kills.get(server);
    }
    
    public int getDeaths(String server) {
        return deaths.get(server);
    }

    public void addKill(String server) {
        kills.put(server, kills.get(server) + 1);
    }
    
    public void addDeaths(String server) {
        deaths.put(server, deaths.get(server) + 1);
    }

    public Map<String, String> getSettings()
    {
        return settings;
    }

    public void setSettings(Map<String, String> settings)
    {
        this.settings = settings;
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

    public class PVPTimer implements Runnable
    {

        private boolean pause;

        @Override
        public void run()
        {
            if (!pause && safeTimer > 0)
            {
                Bukkit.getScheduler().runTaskLater(DesireCore.getInstance(), this, 20);
            }
            safeTimer--;
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

}
