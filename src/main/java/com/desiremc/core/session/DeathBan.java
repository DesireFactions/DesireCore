package com.desiremc.core.session;

import java.util.UUID;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.IdGetter;

import com.desiremc.core.DesireCore;

@Entity(noClassnameStored = true, value = "death_bans")
public class DeathBan
{

    @Id
    private int id;

    private UUID player;

    private String server;

    private long startTime;

    private boolean revived;

    private boolean staffRevive;

    private String reviveReason;
    
    private UUID reviver;

    public DeathBan(int id, HCFSession session)
    {
        this.id = id;
        this.player = session.getUniqueId();
        this.server = DesireCore.getCurrentServer();
        this.startTime = System.currentTimeMillis();
    }

    public DeathBan()
    {

    }

    @IdGetter
    public int getId()
    {
        return id;
    }

    public UUID getPlayer()
    {
        return player;
    }

    public void setPlayer(UUID player)
    {
        this.player = player;
    }

    public String getServer()
    {
        return server;
    }

    public void setServer(String server)
    {
        this.server = server;
    }

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

    public boolean wasStaffRevived()
    {
        return staffRevive;
    }

    public void setStaffRevive(boolean staffRevive)
    {
        this.staffRevive = staffRevive;
    }

    public String getReviveReason()
    {
        return reviveReason;
    }

    public void setReviveReason(String reviveReason)
    {
        this.reviveReason = reviveReason;
    }

    public UUID getReviver()
    {
        return reviver;
    }

    public void setReviver(UUID reviver)
    {
        this.reviver = reviver;
    }
    
    public void save()
    {
        DeathBanHandler.getInstance().save(this);
    }

}