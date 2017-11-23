package com.desiremc.core.tablistfive;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.spigotmc.ProtocolInjector;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedGameProfile;

import net.minecraft.server.v1_7_R4.ChatSerializer;

public class TabList
{

    private static final int PACKET_INFO_ACTION = 0;
    private static final int PACKET_INFO_GAMEMODE = 1;
    private static final int PACKET_INFO_PING = 2;
    private static final int PACKET_INFO_PROFILE = 0;
    private static final int PACKET_INFO_USERNAME = 0;

    private Player player;

    private boolean old = TabAPI.getProtocolManager().getProtocolVersion(player) >= 20;

    private HashMap<Integer, TabSlot> slots = new HashMap<>();

    private int defaultPing = 10;

    private List<String> toRemove = new LinkedList<>();

    private String header = " ";
    private String footer = " ";

    protected TabList(Player player)
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

    public Player getPlayer()
    {
        return player;
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
        tabSlot.state = 3;
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
            toRemove.add(tabSlot.getName());
        }
        tabSlot.setName(name);
        tabSlot.state = 3;

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
            toRemove.add(tabSlot.getName());
        }
        tabSlot.setPrefix(prefix);
        tabSlot.setName(name);
        tabSlot.setSuffix(suffix);
        tabSlot.state = 3;

        return tabSlot;
    }

    public void setHeader(String header)
    {
        this.header = (header != null && header.length() != 0) ? header : "\"\"";
    }

    public void setFooter(String footer)
    {
        this.footer = (footer != null && footer.length() != 0) ? footer : "\"\"";
    }

    public void send()
    {
        if (slots.size() != getCount())
        {
            for (int i = 0; i < getCount(); i++)
            {
                slots.put(i, new TabSlot(this, getNullName(i)));
            }
        }
        if (old)
        {
            slots.values().forEach(x -> toRemove.add(x.getName()));
            for (String str : toRemove)
            {
                PacketContainer packet = TabAPI.getProtocolManager().createPacket(PacketType.Play.Server.PLAYER_INFO);
                packet.getStrings().write(PACKET_INFO_USERNAME, str);
                packet.getIntegers().write(PACKET_INFO_ACTION, 4);
                packet.getIntegers().write(PACKET_INFO_GAMEMODE, 0);
                packet.getIntegers().write(PACKET_INFO_PING, 0);
                try
                {
                    TabAPI.getProtocolManager().sendServerPacket(player, packet);
                }
                catch (InvocationTargetException ex)
                {
                    ex.printStackTrace();
                }
            }
            toRemove.clear();
            TabSlot slot;
            for (int i = 0; i < 60; i++)
            {
                PacketContainer packet = TabAPI.getProtocolManager().createPacket(PacketType.Play.Server.PLAYER_INFO);
                slot = slots.get(i);
                packet.getStrings().write(PACKET_INFO_USERNAME, slot.getName());
                packet.getIntegers().write(PACKET_INFO_ACTION, 0);
                packet.getIntegers().write(PACKET_INFO_GAMEMODE, 0);
                packet.getIntegers().write(PACKET_INFO_PING, 0);
                try
                {
                    TabAPI.getProtocolManager().sendServerPacket(player, packet);
                }
                catch (InvocationTargetException ex)
                {
                    ex.printStackTrace();
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
        }
        else
        {
            ProtocolInjector.PacketTabHeader headerFooterPacket = new ProtocolInjector.PacketTabHeader(ChatSerializer.a("{\"text\": \"" + header + "\"}"), ChatSerializer.a("{\"text\": \"" + footer + "\"}"));
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(headerFooterPacket);
            for (int i = 0; i < 80; i++)
            {
                TabSlot slot = slots.get(i);
                if (slot.state != -1)
                {
                    PacketContainer packet = TabAPI.getProtocolManager().createPacket(PacketType.Play.Server.PLAYER_INFO);
                    packet.getStrings().write(PACKET_INFO_USERNAME, slot.getName());
                    packet.getIntegers().write(PACKET_INFO_ACTION, slot.state);
                    packet.getIntegers().write(PACKET_INFO_GAMEMODE, 0);
                    packet.getIntegers().write(PACKET_INFO_PING, 0);
                    packet.getGameProfiles().write(PACKET_INFO_PROFILE, new WrappedGameProfile(slot.getUniqueId(), slot.getName()));
                    try
                    {
                        TabAPI.getProtocolManager().sendServerPacket(player, packet);
                    }
                    catch (InvocationTargetException ex)
                    {
                        ex.printStackTrace();
                    }
                    slot.state = -1;
                }
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
        clearSlot(key);
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
}