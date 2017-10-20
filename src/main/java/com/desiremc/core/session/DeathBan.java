package com.desiremc.core.session;

import org.mongodb.morphia.annotations.Embedded;

@Embedded
public class DeathBan
{
    private long startTime;
    private boolean revived;

    public DeathBan(long startTime)
    {
        this.startTime = startTime;
    }

    public DeathBan()
    {

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

}