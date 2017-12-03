package com.desiremc.core.tablist;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashMap;

import org.bukkit.entity.Player;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.reflect.FieldAccessException;

public class TabList
{

    Player player;

    int defaultPing = 1000;

    TabList(Player player)
    {
        this.player = player;
    }

    HashMap<Integer, TabSlot> slots = new HashMap<>();
    HashMap<Integer, TabSlot> toRemove = new HashMap<>();

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

    public void clearSlot(int slot)
    {
        TabSlot tabSlot = slots.remove(slot);
        if (tabSlot == null)
        {
            return;
        }
        tabSlot.toRemove = true;
    }

    public void clearAllSlots()
    {
        for (TabSlot slot : slots.values())
        {
            slot.toRemove = true;
        }
        slots.clear();
    }
    
    public Collection<TabSlot> getSlots()
    {
        return slots.values();
    }

    public TabSlot setSlot(int column, int row, String name)
    {
        return setSlot(column * (row - 1), name);
    }

    public TabSlot setSlot(int slot, String name)
    {
        TabSlot tabSlot = new TabSlot(this, name);
        slots.put(slot, tabSlot);
        return tabSlot;
    }

    public TabSlot setSlot(int column, int row, String prefix, String name, String suffix)
    {
        return setSlot(column * (row - 1), prefix, name, suffix);
    }

    public TabSlot setSlot(int slot, String prefix, String name, String suffix)
    {
        TabSlot tabSlot = new TabSlot(this, prefix, name, suffix);
        slots.put(slot, tabSlot);
        return tabSlot;
    }

    public void send()
    {
        if (TabAPI.getProtocolManager().getProtocolVersion(player) >= 47)
        {
            return;
        }
        for (int i = 0; i < 60; i++)
        {
            TabSlot slot = slots.get(i);
            if (slot != null)
            {
                toRemove.put(i, slot);
                slot.sent = true;
                PacketContainer packet = TabAPI.getProtocolManager().createPacket(PacketType.Play.Server.PLAYER_INFO);
                packet.getStrings().write(0, slot.getName());
                try
                {
                    packet.getBooleans().write(0, true);
                    packet.getIntegers().write(0, -1);
                }
                catch (FieldAccessException ex)
                {
                    packet.getIntegers().write(0, 0);
                    packet.getIntegers().write(1, 0);
                    packet.getIntegers().write(2, -1);
                }
                try
                {
                    TabAPI.getProtocolManager().sendServerPacket(player, packet);
                }
                catch (InvocationTargetException e)
                {
                    e.printStackTrace();
                }
                if (slot.getPrefix() != null || slot.getSuffix() != null)
                {
                    PacketContainer team = TabAPI.buildTeamPacket(slot.getName(), slot.getName(), slot.getPrefix(), slot.getSuffix(), 0, slot.getName());
                    try
                    {
                        TabAPI.getProtocolManager().sendServerPacket(player, team);
                    }
                    catch (InvocationTargetException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
            else
            {
                String nullName = "§" + String.valueOf(i);
                if (i >= 10)
                {
                    nullName = "§" + String.valueOf(i / 10) + "§" + String.valueOf(i % 10);
                }
                PacketContainer packet = TabAPI.getProtocolManager().createPacket(PacketType.Play.Server.PLAYER_INFO);
                packet.getStrings().write(0, nullName);
                try
                {
                    packet.getBooleans().write(0, true);
                    packet.getIntegers().write(0, -1);
                }
                catch (FieldAccessException ex)
                {
                    packet.getIntegers().write(0, 0);
                    packet.getIntegers().write(1, 0);
                    packet.getIntegers().write(2, -1);
                }
                try
                {
                    TabAPI.getProtocolManager().sendServerPacket(player, packet);
                }
                catch (InvocationTargetException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    public void update()
    {
        clear();
        send();
    }

    public void clear()
    {
        if (TabAPI.getProtocolManager().getProtocolVersion(player) >= 47)
        {
            return;
        }
        for (int i = 0; i < 60; i++)
        {
            TabSlot slot = toRemove.remove(i);
            if (slot != null)
            {
                slot.sent = false;
                if (slot.getPrefix() != null || slot.getSuffix() != null)
                {
                    PacketContainer team = TabAPI.buildTeamPacket(slot.getName(), slot.getName(), null, null, 1, slot.getName());
                    try
                    {
                        TabAPI.getProtocolManager().sendServerPacket(player, team);
                    }
                    catch (InvocationTargetException e)
                    {
                        e.printStackTrace();
                    }
                }
                PacketContainer packet = TabAPI.getProtocolManager().createPacket(PacketType.Play.Server.PLAYER_INFO);
                packet.getStrings().write(0, slot.getName());
                try
                {
                    packet.getBooleans().write(0, false);
                    packet.getIntegers().write(0, -1);
                }
                catch (FieldAccessException ex)
                {
                    packet.getIntegers().write(0, 4);
                    packet.getIntegers().write(1, 0);
                    packet.getIntegers().write(2, -1);
                }
                try
                {
                    TabAPI.getProtocolManager().sendServerPacket(player, packet);
                }
                catch (InvocationTargetException e)
                {
                    e.printStackTrace();
                }
            }
            else
            {
                String nullName = "§" + String.valueOf(i);
                if (i >= 10)
                {
                    nullName = "§" + String.valueOf(i / 10) + "§" + String.valueOf(i % 10);
                }
                PacketContainer packet = TabAPI.getProtocolManager().createPacket(PacketType.Play.Server.PLAYER_INFO);
                packet.getStrings().write(0, nullName);
                try
                {
                    packet.getBooleans().write(0, false);
                    packet.getIntegers().write(0, -1);
                }
                catch (FieldAccessException ex)
                {
                    packet.getIntegers().write(0, 4);
                    packet.getIntegers().write(1, 0);
                    packet.getIntegers().write(2, -1);
                }
                try
                {
                    TabAPI.getProtocolManager().sendServerPacket(player, packet);
                }
                catch (InvocationTargetException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    public Player getPlayer()
    {
        return player;
    }

    public boolean isOld()
    {
        return TabAPI.getProtocolManager().getProtocolVersion(getPlayer()) < 20;
    }

}