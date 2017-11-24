package com.desiremc.core.tablistold;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;

public class TabList
{

    private static final boolean DEBUG = true;

    public static final int PACKET_INFO_ACTION = 0;
    public static final int PACKET_INFO_GAMEMODE = 1;
    public static final int PACKET_INFO_PING = 2;
    public static final int PACKET_INFO_PROFILE = 0;
    public static final int PACKET_INFO_USERNAME = 0;

    private Player instancePlayer;

    private boolean old;

    private HashMap<Integer, TabSlot> slots = new HashMap<>();

    private int defaultPing = 10;

    private List<String> toRemoveName = new LinkedList<>();

    protected TabList(Player instancePlayer)
    {
        this.instancePlayer = instancePlayer;
        old = ((CraftPlayer) instancePlayer).getHandle().playerConnection.networkManager.getVersion() < 20;
    }

    public TabSlot getSlot(int column, int row)
    {
        return getSlot(column * (row - 1));
    }

    public TabSlot getSlot(int slot)
    {
        return slots.get(slot);
    }

    public void setDefaultPing(int ping)
    {
        defaultPing = ping;
    }

    public int getDefaultPing()
    {
        return defaultPing;
    }

    public Player getPlayer()
    {
        return instancePlayer;
    }

    public void clearSlot(int slot)
    {
        TabSlot tabSlot = slots.get(slot);

        if (tabSlot.getPrefix() != null && !tabSlot.getPrefix().equals(""))
        {
            tabSlot.removePrefixAndSuffix();
            tabSlot.setPrefix(null);
            tabSlot.setSuffix(null);
        }

        tabSlot.setName(getNullName(slot));
    }

    public TabSlot setSlot(int column, int row, String name)
    {
        return setSlot(column * (row - 1), name);
    }

    public TabSlot setSlot(int slot, String name)
    {
        TabSlot tabSlot = slots.get(slot);

        if (tabSlot.getPrefix() != null && !tabSlot.getPrefix().equals(""))
        {
            tabSlot.removePrefixAndSuffix();
            tabSlot.setPrefix(null);
            tabSlot.setSuffix(null);
        }
        if (old)
        {
            toRemoveName.add(tabSlot.getName());
        }
        tabSlot.setName(name);

        return tabSlot;
    }

    public TabSlot setSlot(int column, int row, String prefix, String name, String suffix)
    {
        return setSlot(column * (row - 1), prefix, name, suffix);
    }

    public TabSlot setSlot(int slot, String prefix, String name, String suffix)
    {
        TabSlot tabSlot = slots.get(slot);

        if (tabSlot.getPrefix() != null && !tabSlot.getPrefix().equals(""))
        {
            tabSlot.updatePrefixAndSuffix(prefix, suffix);
        }
        else
        {
            tabSlot.createPrefixAndSuffix(prefix, suffix);
        }
        if (old)
        {
            toRemoveName.add(tabSlot.getName());
        }
        tabSlot.setPrefix(prefix);
        tabSlot.setName(name);
        tabSlot.setSuffix(suffix);

        return tabSlot;
    }

    public void send()
    {
        if (DEBUG)
        {
            System.out.println("TabList.send() called for player " + getPlayer().getName());
        }
        int count = getCount();
        if (DEBUG)
        {
            System.out.println("TabList.send() count: " + count);
        }
        if (slots.size() != count)
        {
            TabSlot slot;
            for (int i = 0; i < getCount(); i++)
            {
                slot = new TabSlot(this, getNullName(i));
                slots.put(i, slot);
            }
        }
        if (DEBUG)
        {
            System.out.println("TabList.send() slots size: " + slots.size());
        }
        if (old)
        {
            if (DEBUG)
            {
                System.out.println("TabList.send() is old version.");
            }
            slots.values().forEach(x -> toRemoveName.add(x.getName()));
            if (DEBUG)
            {
                System.out.println("TabList.send() toRemoveName size: " + toRemoveName.size());
            }
            int itSize = 0, itSize2= 0;
            for (String str : toRemoveName)
            {
                PacketContainer packet = TabAPI.getProtocolManager().createPacket(PacketType.Play.Server.PLAYER_INFO);
                packet.getStrings().write(PACKET_INFO_USERNAME, str);
                packet.getIntegers().write(PACKET_INFO_ACTION, 4);
                packet.getIntegers().write(PACKET_INFO_GAMEMODE, 0);
                packet.getIntegers().write(PACKET_INFO_PING, -1);
                try
                {
                    TabAPI.getProtocolManager().sendServerPacket(getPlayer(), packet);
                }
                catch (InvocationTargetException ex)
                {
                    ex.printStackTrace();
                }
                itSize++;
            }
            if (DEBUG)
            {
                System.out.println("TabList.send() toRemoveName iter count: " + itSize);
            }
            itSize = 0;
            toRemoveName.clear();
            TabSlot slot;
            for (int i = 0; i < count; i++)
            {
                PacketContainer packet = TabAPI.getProtocolManager().createPacket(PacketType.Play.Server.PLAYER_INFO);
                slot = slots.get(i);
                packet.getStrings().write(PACKET_INFO_USERNAME, slot.getName());
                packet.getIntegers().write(PACKET_INFO_ACTION, 0);
                packet.getIntegers().write(PACKET_INFO_GAMEMODE, 0);
                packet.getIntegers().write(PACKET_INFO_PING, -1);
                try
                {
                    TabAPI.getProtocolManager().sendServerPacket(getPlayer(), packet);
                }
                catch (InvocationTargetException ex)
                {
                    ex.printStackTrace();
                }
                itSize++;
                if (slot.getPrefix() != null || slot.getSuffix() != null)
                {
                    PacketContainer team = TabAPI.buildTeamPacket(slot.getName(), slot.getName(), slot.getPrefix(), slot.getSuffix(), 0, slot.getName());
                    try
                    {
                        TabAPI.getProtocolManager().sendServerPacket(getPlayer(), team);
                    }
                    catch (InvocationTargetException e)
                    {
                        e.printStackTrace();
                    }
                    itSize2++;
                }
            }

            if (DEBUG)
            {
                System.out.println("TabList.send() add packet count: " + itSize);
                System.out.println("TabList.send() team packet count: " + itSize2);
            }
        }
    }

    private int getCount()
    {
        return !old ? 80 : 60;
    }

    public void clearSlot(String match)
    {
        int key = -1;
        for (Entry<Integer, TabSlot> entry : slots.entrySet())
        {
            if (entry.getValue().getComplete().contains(match))
            {
                key = entry.getKey();
            }
        }
        if (key != -1)
        {
            clearSlot(key);
        }
    }

    private static String getNullName(int slot)
    {
        String name;
        if (slot < 10)
        {
            name = "§" + slot;
        }
        else
        {
            name = "§" + (slot / 10) + "§" + (slot % 10);
        }
        return name;
    }

    public Collection<TabSlot> getSlots()
    {
        return slots.values();
    }

    public boolean isOld()
    {
        return old;
    }

}