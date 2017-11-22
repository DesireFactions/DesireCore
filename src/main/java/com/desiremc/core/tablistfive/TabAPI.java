package com.desiremc.core.tablistfive;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;

import org.bukkit.entity.Player;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;

public class TabAPI
{

    private static HashMap<String, TabList> tabLists = new HashMap<>();

    private static ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();

    public static TabList createTabListForPlayer(Player player)
    {
        TabList list = new TabList(player);
        tabLists.put(player.getName(), list);
        return list;
    }

    public static ProtocolManager getProtocolManager()
    {
        return protocolManager;
    }

    public static TabList getPlayerTabList(Player player)
    {
        return tabLists.get(player.getName());
    }

    public static void removePlayer(Player player)
    {
        tabLists.remove(player.getName());
    }
    
    public static PacketContainer buildTeamPacket(String name, String display, String prefix, String suffix, int flag, String... members)
    {
        PacketContainer packet = protocolManager.createPacket(PacketType.Play.Server.SCOREBOARD_TEAM);
        packet.getIntegers().write(0, flag);
        packet.getStrings().write(0, name).write(1, display).write(2, prefix).write(3, suffix);
        packet.getSpecificModifier(Collection.class).write(0, Arrays.asList(members));
        return packet;
    }

}