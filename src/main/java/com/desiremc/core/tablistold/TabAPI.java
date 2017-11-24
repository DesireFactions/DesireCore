package com.desiremc.core.tablistold;

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

    private static ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();

    public static TabList createTabListForPlayer(Player player)
    {
        TabList list = new TabList(player);
        tabLists.put(player.getUniqueId(), list);
        return list;
    }

    public static ProtocolManager getProtocolManager()
    {
        return protocolManager;
    }

    public static TabList getPlayerTabList(Player player)
    {
        return tabLists.get(player.getUniqueId());
    }

    public static void removePlayer(Player player)
    {
        tabLists.remove(player.getUniqueId());
    }

    public static PacketContainer buildTeamPacket(String name, String display, String prefix, String suffix, int flag, String... members)
    {
        PacketContainer packet = protocolManager.createPacket(PacketType.Play.Server.SCOREBOARD_TEAM);
        packet.getIntegers().write(0, flag);
        packet.getStrings().write(0, name).write(1, display).write(2, prefix).write(3, suffix);
        packet.getSpecificModifier(Collection.class).write(0, Arrays.asList(members));
        return packet;
    }

    public static void initialize()
    {
        getProtocolManager().addPacketListener(new PacketListener());
    }

}