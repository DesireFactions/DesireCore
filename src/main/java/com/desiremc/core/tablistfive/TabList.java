package com.desiremc.core.tablistfive;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
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

    private Player player;

    private int defaultPing = 10;

    private HashMap<Integer, TabSlot> slots = new HashMap<>();

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
        TabSlot tabSlot = slots.remove(slot);
        if (tabSlot == null)
        {
            return;
        }

        tabSlot.removePrefixAndSuffix();
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
        if (TabAPI.getProtocolManager().getProtocolVersion(player) >= 47)
        {
            ProtocolInjector.PacketTabHeader packet = new ProtocolInjector.PacketTabHeader(ChatSerializer.a("{\"text\": \"" + header + "\"}"), ChatSerializer.a("{\"text\": \"" + footer + "\"}"));
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
        }
        for (int i = 0; i < getCount(); i++)
        {
            TabSlot slot = slots.get(i);
            if (slot == null)
            {
                slot = new TabSlot(this, getNullName(i));
                slots.put(i, slot);
            }
            if (slot != null)
            {
                slot.sent = true;
                PacketContainer packet = TabAPI.getProtocolManager().createPacket(PacketType.Play.Server.PLAYER_INFO);
                packet.getStrings().write(0, slot.getName());
                packet.getIntegers().write(0, slot.state);
                packet.getIntegers().write(1, 0);
                packet.getIntegers().write(2, -1);
                packet.getGameProfiles().write(0, new WrappedGameProfile(slot.getUniqueId(), slot.getName()));

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
        }
    }

    private int getCount()
    {
        return TabAPI.getProtocolManager().getProtocolVersion(player) >= 47 ? 80 : 60;
    }

    public void clearSlot(String match)
    {
        for (Entry<Integer, TabSlot> entry : slots.entrySet())
        {
            if (entry.getValue().getComplete().contains(match))
            {
                clearSlot(entry.getKey());
            }
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
}