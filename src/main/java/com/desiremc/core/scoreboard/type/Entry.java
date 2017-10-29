package com.desiremc.core.scoreboard.type;

import java.util.UUID;

import com.desiremc.core.scoreboard.common.Strings;

public class Entry
{

    private String name;
    private UUID uuid;
    private int position;

    public Entry(String name, UUID uuid, int position)
    {
        this.name = Strings.format(name);
        this.uuid = uuid;
        this.position = position;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setUniqueId(UUID uuid)
    {
        this.uuid = uuid;
    }

    public UUID getUniqueId()
    {
        return uuid;
    }

    public int getPosition()
    {
        return position;
    }

    public void setPosition(int position)
    {
        this.position = position;
    }

}
