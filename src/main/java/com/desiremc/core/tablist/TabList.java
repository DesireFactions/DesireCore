package com.desiremc.core.tablist;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;

public class TabList
{

    private static final int ADD_PLAYER = 0;
    private static final int REMOVE_PLAYER = 4;

    /*
     *  00,01,02
     *  03,04,05
     *  06,07,08
     *  09,10,11
     *  12,13,14
     *  15,16,17
     *  18,19,20
     *  21,22,23
     *  24,25,26
     *  27,28,29
     *  30,31,32
     *  33,31,35
     *  36,37,38
     *  39,40,41
     *  42,43,44
     *  45,46,47
     *  48,59,50
     *  51,52,53
     *  54,55,56
     *  57,58,59 
     */

    private HashMap<Integer, TabSlot> slots = new HashMap<>();

    public Player player;

    public int defaultPing = 1000;

    public TabList(Player player)
    {
        this.player = player;
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

    public void generate()
    {
        slots.clear();
        TabSlot slot;
        for (int i = 0; i < 60; i++)
        {
            slot = new TabSlot(this, processName(i));
            slots.put(i, slot);
            send(slot);
            sendTeamCreate(slot);
        }
    }

    public void terminate()
    {
        TabSlot slot;
        for (int i = 0; i < 60; i++)
        {
            slot = slots.get(i);
            clear(slot);
        }
    }

    public void emptyAllSlots()
    {
        for (TabSlot slot : slots.values())
        {
            slot.setPrefix(null);
            slot.setSuffix(null);
            sendTeamUpdate(slot);
        }
        slots.clear();
    }

    public Collection<TabSlot> getSlots()
    {
        return slots.values();
    }

    /**
     * Set the value of the slot at the given row and column. The indexes for these start at 0.
     * 
     * @param row the row.
     * @param column the column.
     * @param name the data to be displayed.
     * @return the tab slot.
     */
    public TabSlot setSlot(int row, int column, String name)
    {
        return setSlot((row * 3) + column, name);
    }

    /**
     * Set the value of the slot at the given index. Index starts at 0.
     * 
     * @param slot the index of the slot.
     * @param name the data to be displayed.
     * @return the tab slot.
     */
    public TabSlot setSlot(int slot, String name)
    {
        TabSlot tabSlot = slots.get(slot);
        if (name.length() < 16)
        {
            tabSlot.setPrefix(name.substring(0, name.length()));
            tabSlot.setSuffix(null);
        }
        else if (name.length() <= 28)
        {
            tabSlot.setPrefix(name.substring(0, 16));
            if (tabSlot.getPrefix().endsWith("§"))
            {
                tabSlot.setPrefix(name.substring(0, 15));
            }
            tabSlot.setSuffix(ChatColor.getLastColors(tabSlot.getPrefix()) + (tabSlot.getPrefix().endsWith("&") ? "&" : "") + name.substring(16, name.length()));
        }
        else
        {
            throw new IllegalArgumentException("Name can't be longer than 28 characters.");
        }

        sendTeamUpdate(tabSlot);
        return tabSlot;
    }

    @SuppressWarnings("deprecation")
    public void send(TabSlot slot)
    {
        PacketContainer packet = TabAPI.getProtocolManager().createPacket(PacketType.Play.Server.PLAYER_INFO);
        packet.getStrings().write(0, slot.getName());
        packet.getIntegers().write(0, ADD_PLAYER);
        packet.getIntegers().write(1, GameMode.SURVIVAL.getValue());
        packet.getIntegers().write(2, -1);
        try
        {
            TabAPI.getProtocolManager().sendServerPacket(player, packet);
        }
        catch (InvocationTargetException ex)
        {
            ex.printStackTrace();
        }
    }

    @SuppressWarnings("deprecation")
    public void clear(TabSlot slot)
    {
        PacketContainer packet = TabAPI.getProtocolManager().createPacket(PacketType.Play.Server.PLAYER_INFO);
        packet.getStrings().write(0, slot.getName());
        packet.getIntegers().write(0, REMOVE_PLAYER);
        packet.getIntegers().write(1, GameMode.SURVIVAL.getValue());
        packet.getIntegers().write(2, -1);
        try
        {
            TabAPI.getProtocolManager().sendServerPacket(player, packet);
        }
        catch (InvocationTargetException ex)
        {
            ex.printStackTrace();
        }
    }

    private void sendTeamUpdate(TabSlot slot)
    {
        PacketContainer packet = TabAPI.buildTeamPacket(slot.getName(), slot.getName(), slot.getPrefix(), slot.getSuffix(), 2, slot.getName());

        // Strings:
        // 1: Name
        // 2: Display Name
        // 3: Prefix
        // 4: Suffix

        // Integers:
        // 0: Mode (0 = create, 2 = update, 3 = add player, 4 = remove player)

        // Specific:
        // 0: Members

        try
        {
            TabAPI.getProtocolManager().sendServerPacket(player, packet);
        }
        catch (InvocationTargetException ex)
        {
            ex.printStackTrace();
        }
    }

    private void sendTeamCreate(TabSlot slot)
    {
        PacketContainer packet = TabAPI.buildTeamPacket(slot.getName(), slot.getName(), slot.getPrefix(), slot.getSuffix(), 0, slot.getName());

        // Strings:
        // 1: Name
        // 2: Display Name
        // 3: Prefix
        // 4: Suffix

        // Integers:
        // 0: Mode (0 = create, 2 = update)

        // Specific:
        // 0: Members

        try
        {
            TabAPI.getProtocolManager().sendServerPacket(player, packet);
        }
        catch (InvocationTargetException ex)
        {
            ex.printStackTrace();
        }
    }

    private static String processName(int slot)
    {
        return "§" + ((char) ('a' + (slot / 10))) + "§" + (slot % 10);
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