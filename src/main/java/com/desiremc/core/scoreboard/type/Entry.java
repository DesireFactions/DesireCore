package com.desiremc.core.scoreboard.type;

import java.util.UUID;

public class Entry
{

    private String prefix;
    private String value;
    private UUID uuid;
    private int position;

    public Entry(String prefix, String value, UUID uuid, int position)
    {
        this.prefix = prefix;
        this.value = value;
        this.uuid = uuid;
        this.position = position;
    }

    public String getPrefix()
    {
        return prefix;
    }

    public void setPrefix(String prefix)
    {
        this.prefix = prefix;
    }

    public String getValue()
    {
        return value;
    }

    public void setValue(String value)
    {
        this.value = value;
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
