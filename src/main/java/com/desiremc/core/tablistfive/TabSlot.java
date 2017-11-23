package com.desiremc.core.tablistfive;

import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

import com.comphenix.protocol.events.PacketContainer;

public class TabSlot
{

    private TabList list;
    protected boolean sent;

    private UUID uuid;
    private String prefix, name, suffix;
    private int ping;

    public TabSlot(TabList list, String prefix, String name, String suffix)
    {
        this(list, prefix, name, suffix, UUID.randomUUID());
    }

    public TabSlot(TabList list, String prefix, String name, String suffix, UUID uuid)
    {
        this.list = list;
        if (prefix != null)
        {
            this.prefix = prefix.substring(0, Math.min(prefix.length(), 16));//Limit to 16 chars to avoid client crash
        }
        else
        {
            this.prefix = "";
        }
        this.name = name.substring(0, Math.min(name.length(), 16)); //Limit to 16 chars to avoid client crash
        if (suffix != null)
        {
            this.suffix = suffix.substring(0, Math.min(suffix.length(), 16)); //Limit to 16 chars to avoid client crash
        }
        else
        {
            suffix = "";
        }
        this.sent = false;
        this.ping = list.getDefaultPing();

        this.uuid = uuid;
    }

    public TabSlot(TabList list, String name)
    {
        this(list, name, UUID.randomUUID());
    }

    public TabSlot(TabList list, String name, UUID uuid)
    {
        this.list = list;

        this.name = name.substring(0, Math.min(name.length(), 16)); //Limit to 16 chars to avoid client crash

        this.sent = false;
        this.ping = list.getDefaultPing();

        this.uuid = uuid;
    }

    public UUID getUniqueId()
    {
        return uuid;
    }

    public void setPing(int ping)
    {
        this.ping = ping;
    }

    public int getPing()
    {
        return ping;
    }

    public String getPrefix()
    {
        return prefix;
    }

    public void setPrefix(String prefix)
    {
        this.prefix = prefix;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getSuffix()
    {
        return suffix;
    }

    public void setSuffix(String suffix)
    {
        this.suffix = suffix;
    }

    public void createPrefixAndSuffix(String prefix, String suffix)
    {
        if (TabAPI.getProtocolManager().getProtocolVersion(list.getPlayer()) >= 47)
        {
            return;
        }
        if (!sent)
        {
            return;
        }
        if (this.prefix != null || this.suffix != null)
        {
            updatePrefixAndSuffix(prefix, suffix);
            return;
        }

        this.prefix = prefix.substring(0, Math.min(prefix.length(), 16)); //Limit to 16 chars to avoid client crash
        this.suffix = suffix.substring(0, Math.min(suffix.length(), 16)); //Limit to 16 chars to avoid client crash

        PacketContainer packet = TabAPI.buildTeamPacket(name, name, this.prefix, this.suffix, 0, name);
        try
        {
            TabAPI.getProtocolManager().sendServerPacket(list.getPlayer(), packet);
        }
        catch (InvocationTargetException e)
        {
            e.printStackTrace();
        }
    }

    public void updatePrefixAndSuffix(String prefix, String suffix)
    {
        if (!sent)
        {
            return;
        }
        if (this.prefix == null && this.suffix == null)
        {
            createPrefixAndSuffix(prefix, suffix);
            return;
        }

        this.prefix = prefix.substring(0, Math.min(prefix.length(), 16)); //Limit to 16 chars to avoid client crash
        this.suffix = suffix.substring(0, Math.min(suffix.length(), 16)); //Limit to 16 chars to avoid client crash

        PacketContainer packet = TabAPI.buildTeamPacket(name, name, this.prefix, this.suffix, 2, name);
        try
        {
            TabAPI.getProtocolManager().sendServerPacket(list.getPlayer(), packet);
        }
        catch (InvocationTargetException e)
        {
            e.printStackTrace();
        }
    }

    public void removePrefixAndSuffix()
    {
        if ((this.prefix == null && this.suffix == null) || !sent)
        {
            return;
        }

        PacketContainer packet = TabAPI.buildTeamPacket(name, name, null, null, 1, name);
        try
        {
            TabAPI.getProtocolManager().sendServerPacket(list.getPlayer(), packet);
        }
        catch (InvocationTargetException e)
        {
            e.printStackTrace();
        }
    }

    protected void setUniqueId(UUID uuid)
    {
        this.uuid = uuid;
    }

    public String getComplete()
    {
        return prefix + name + suffix;
    }

}