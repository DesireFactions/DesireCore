package com.desiremc.core.tablist;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Player;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;

public class TabAPI
{

    private static HashMap<UUID, TabList> tabLists = new HashMap<>();

    public static TabList createTabListForPlayer(Player player)
    {
        TabList list = new TabList(player);
        tabLists.put(player.getUniqueId(), list);
        list.generate();
        return list;
    }

    public static TabList getPlayerTabList(Player player)
    {
        return tabLists.get(player.getUniqueId());
    }

    public static void removePlayer(Player player)
    {
        TabList list = tabLists.remove(player.getUniqueId());
        if (list != null)
        {
            list.terminate();
        }
    }
    
    public static PacketContainer buildTeamPacket(String name, String display, String prefix, String suffix, int flag, String... members)
    {
        PacketContainer packet = getProtocolManager().createPacket(PacketType.Play.Server.SCOREBOARD_TEAM);
        packet.getIntegers().write(0, flag);
        packet.getStrings().write(0, name).write(1, display).write(2, prefix).write(3, suffix);
        packet.getSpecificModifier(Collection.class).write(0, Arrays.asList(members));
        return packet;
    }

    public static ProtocolManager getProtocolManager()
    {
        return ProtocolLibrary.getProtocolManager();
    }

    public static void initialize()
    {
        getProtocolManager().addPacketListener(new PacketListener());
    }

}