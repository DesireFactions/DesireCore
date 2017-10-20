package com.desiremc.core.session;

import java.util.UUID;

import org.mongodb.morphia.annotations.Embedded;

@Embedded
public class Ticker implements Comparable<Ticker>
{
    private UUID player;
    private int count;

    public Ticker(UUID uuid)
    {
        count = 1;
    }

    public Ticker()
    {
    }

    public UUID getUniqueId()
    {
        return player;
    }

    public int getCount()
    {
        return count;
    }

    public void setCount(int count)
    {
        this.count = count;
    }

    @Override
    public int compareTo(Ticker tick)
    {
        return Integer.compare(count, tick.count);
    }

}