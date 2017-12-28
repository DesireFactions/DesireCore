package com.desiremc.core.punishment;

import org.bukkit.Bukkit;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Indexed;

import com.desiremc.core.DesireCore;

import java.util.UUID;

@Entity(noClassnameStored = true, value = "punishments")
public class Punishment
{

    @Id
    private long id;

    @Indexed
    private UUID punished;

    @Indexed
    private UUID issuer;

    private Type type;

    private long issued;

    private long expirationTime;

    private boolean repealed;

    private boolean permanent;

    private boolean blacklisted;

    private String reason;

    public UUID getPunished()
    {
        return punished;
    }

    public void setPunished(UUID punished)
    {
        this.punished = punished;
    }

    public UUID getIssuer()
    {
        return issuer;
    }

    public void setIssuer(UUID issuer)
    {
        this.issuer = issuer;
    }

    public Type getType()
    {
        return type;
    }

    public void setType(Type type)
    {
        this.type = type;
    }

    public long getIssued()
    {
        return issued;
    }

    public void setIssued(long issued)
    {
        this.issued = issued;
    }

    public long getExpirationTime()
    {
        return expirationTime;
    }

    public void setExpirationTime(long expires)
    {
        this.expirationTime = expires;
    }

    public boolean isRepealed()
    {
        return repealed;
    }

    public void setRepealed(boolean repealed)
    {
        this.repealed = repealed;
    }

    public boolean isPermanent()
    {
        return permanent;
    }

    public void setPermanent(boolean permanent)
    {
        this.permanent = permanent;
    }

    public String getReason()
    {
        return reason;
    }

    public void setReason(String reason)
    {
        this.reason = reason;
    }

    public void setBlacklisted(boolean blacklisted)
    {
        this.blacklisted = blacklisted;
    }

    public boolean isBlacklisted()
    {
        return blacklisted;
    }

    public void save()
    {
        Bukkit.getScheduler().runTaskAsynchronously(DesireCore.getInstance(), new Runnable()
        {

            @Override
            public void run()
            {
                PunishmentHandler.getInstance().save(Punishment.this);
            }
        });
    }

    public static enum Type
    {
        MUTE,
        BAN,
        WARN,
        IP_BAN
    }

}
