package com.desiremc.core.tablist;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Player;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;

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

    public static ProtocolManager getProtocolManager()
    {
        return ProtocolLibrary.getProtocolManager();
    }

    public static void initialize()
    {
        getProtocolManager().addPacketListener(new PacketListener());
    }

}