package com.desiremc.core.tablist;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import net.minecraft.server.v1_7_R4.EntityPlayer;
import net.minecraft.server.v1_7_R4.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_7_R4.PlayerConnection;
import net.minecraft.util.com.mojang.authlib.GameProfile;

public class PlayerList
{

    private HashMap<Integer, ListWrapper> entries = new HashMap<>();
    private HashMap<String, Team> teams = new HashMap<>();

    private Player player;

    public PlayerList(Player player)
    {
        this.player = player;
    }

    public void initTable()
    {
        if (!getPlayer().isOnline())
        {
            return;
        }
        PlayerConnection connection = getHandle(getPlayer()).playerConnection;
        for (Player player : Bukkit.getOnlinePlayers())
        {
            connection.sendPacket(buildPacket(player.getUniqueId(), player.getName(), REMOVE));
        }

        if (connection.networkManager.getVersion() > 5)
        {
            for (int i = 0; i < 80; i++)
            {
                ListWrapper wrapper = new ListWrapper(UUID.fromString("00000000-0000-0000-0000-0000000000" + Integer.toString(i)), "");
                if (i == 61)
                {
                    wrapper.setName("For the best");
                }
                else if (i == 62)
                {
                    wrapper.setName("experience use");
                }
                else if (i == 63)
                {
                    wrapper.setName("1.7.10");
                }
                entries.put(i, wrapper);
                connection.sendPacket(buildPacket(wrapper, ADD));

            }
        }
        else
        {
            for (int i = 0; i < 60; i++)
            {
                ListWrapper wrapper = new ListWrapper(UUID.fromString("00000000-0000-0000-0000-0000000000" + Integer.toString(i)), "");
                entries.put(i, wrapper);
                connection.sendPacket(buildPacket(wrapper, ADD));
            }
        }

    }

    public void updateLocation(int slot, String name)
    {
        ListWrapper wrapper = entries.get(slot);
        if (name.length() <= 16)
        {
            wrapper.setName(name);
        }
        else
        {
            int min = Math.min(16, name.length());
            String prefix = name.substring(0, min - 16);
            name = name.substring(name.length() - 16, min);
            Team team = teams.get(prefix);
            if (team == null)
            {
                team = getPlayer().getScoreboard().registerNewTeam(prefix);
            }
            team.addEntry(name);
            wrapper.setName(name);
        }
        getHandle(getPlayer()).playerConnection.sendPacket(buildPacket(wrapper, UPDATE));
    }

    public void updateLocation(int x, int y, String name)
    {
        if (x > 2)
        {
            throw new IllegalArgumentException("x must be less than 3.");
        }
        if (y > 19)
        {
            throw new IllegalArgumentException("y must be less than 20.");
        }
        this.updateLocation(x * 20 + y, name);
    }

    public ListWrapper getListWrapper(String name)
    {
        for (ListWrapper wrapper : entries.values())
        {
            if (wrapper.getName().equals(name))
            {
                return wrapper;
            }
        }
        return null;
    }

    public Player getPlayer()
    {
        return player;
    }

    private static final int ADD = 0;
    private static final int UPDATE = 3;
    private static final int REMOVE = 4;

    private static final Class<?> PACKET_PLAYER_INFO_CLASS = PacketPlayOutPlayerInfo.class;
    private static Field PLAYER_INFO_PACKET_ACTION;
    private static Field PLAYER_INFO_PACKET_PROFILE;
    private static Field PLAYER_INFO_PACKET_GAMEMODE;
    private static Field PLAYER_INFO_PACKET_PING;
    private static Field PLAYER_INFO_PACKET_USERNAME;

    static
    {
        try
        {
            PLAYER_INFO_PACKET_ACTION = PACKET_PLAYER_INFO_CLASS.getDeclaredField("action");
            PLAYER_INFO_PACKET_PROFILE = PACKET_PLAYER_INFO_CLASS.getDeclaredField("player");
            PLAYER_INFO_PACKET_GAMEMODE = PACKET_PLAYER_INFO_CLASS.getDeclaredField("gamemode");
            PLAYER_INFO_PACKET_PING = PACKET_PLAYER_INFO_CLASS.getDeclaredField("ping");
            PLAYER_INFO_PACKET_USERNAME = PACKET_PLAYER_INFO_CLASS.getDeclaredField("username");
            PLAYER_INFO_PACKET_ACTION.setAccessible(true);
            PLAYER_INFO_PACKET_PROFILE.setAccessible(true);
            PLAYER_INFO_PACKET_GAMEMODE.setAccessible(true);
            PLAYER_INFO_PACKET_PING.setAccessible(true);
            PLAYER_INFO_PACKET_USERNAME.setAccessible(true);
        }
        catch (NoSuchFieldException | SecurityException e1)
        {
            e1.printStackTrace();
        }
    }

    private static EntityPlayer getHandle(Player player)
    {
        return ((CraftPlayer) player).getHandle();
    }

    private static PacketPlayOutPlayerInfo buildPacket(ListWrapper wrapper, int action)
    {
        return buildPacket(wrapper.getUniqueId(), wrapper.getName(), action);
    }

    @SuppressWarnings("deprecation")
    private static PacketPlayOutPlayerInfo buildPacket(UUID uuid, String name, int action)
    {
        PacketPlayOutPlayerInfo packet = new PacketPlayOutPlayerInfo();
        try
        {
            PLAYER_INFO_PACKET_ACTION.set(packet, action);
            PLAYER_INFO_PACKET_PROFILE.set(packet, new GameProfile(uuid, name));
            PLAYER_INFO_PACKET_GAMEMODE.set(packet, GameMode.SURVIVAL.getValue());
            PLAYER_INFO_PACKET_PING.set(packet, 0);
            PLAYER_INFO_PACKET_USERNAME.set(packet, name);
        }
        catch (IllegalArgumentException | IllegalAccessException e)
        {
            e.printStackTrace();
        }
        return packet;
    }

    private static class ListWrapper
    {
        private UUID uuid;
        private String name;

        public ListWrapper(UUID uuid, String name)
        {
            this.uuid = uuid;
            this.name = name;
        }

        public UUID getUniqueId()
        {
            return uuid;
        }

        public String getName()
        {
            return name;
        }

        public void setName(String name)
        {
            this.name = name;
        }
    }

}
