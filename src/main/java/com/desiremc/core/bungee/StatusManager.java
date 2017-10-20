package com.desiremc.core.bungee;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;

import com.desiremc.core.DesireCore;

public class StatusManager
{
    public static Map<String, ServerStatus> servers = new HashMap<>();

    private static ServerPingManager spm = new ServerPingManager();

    public static ServerPingManager getServerPingManager()
    {
        return spm;
    }

    public static boolean isOnline(String server)
    {
        if (servers.containsKey(server))
        {
            return ((ServerStatus) servers.get(server)).isOnline();
        }
        return false;
    }

    public static int getOnlinePlayers(String server)
    {
        if (servers.containsKey(server))
        {
            return ((ServerStatus) servers.get(server)).getOnlinePlayers();
        }
        return 0;
    }

    public static int getMaxPlayers(String server)
    {
        if (servers.containsKey(server))
        {
            return ((ServerStatus) servers.get(server)).getMaxPlayers();
        }
        return 0;
    }

    public static ServerStatus getServerStatus(String server)
    {
        if (servers.containsKey(server))
        {
            return (ServerStatus) servers.get(server);
        }
        return null;
    }

    public static void updateStatus(String server, boolean online, int onlinePlayers, int maxPlayers)
    {
        ServerStatus serverStatus = new ServerStatus(server);
        serverStatus.setMaxPlayers(maxPlayers);
        serverStatus.setOnline(online);
        serverStatus.setOnlinePlayers(onlinePlayers);
        servers.put(server, serverStatus);
    }

    public static void startPingTask()
    {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(DesireCore.getInstance(), new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    ConfigurationSection cs = DesireCore.getConfigHandler().getConfigurationSection("hubs");
                    String[] pieces;
                    String ip;
                    int port;
                    for (String hub : cs.getKeys(false))
                    {
                        pieces = cs.getString(hub).split(":");
                        ip = pieces[0];
                        port = pieces.length == 2 ? Integer.parseInt(pieces[1]) : 25565;
                        try
                        {
                            StatusResponse status = getServerPingManager().getServerInfo(ip, port);
                            StatusManager.updateStatus(hub, true, status.getOnlinePlayers(), status.getMaxPlayers());

                        }
                        catch (Exception localException2)
                        {
                            StatusManager.updateStatus(hub, false, 0, 0);
                        }
                    }
                }
                catch (Exception localException1)
                {
                    localException1.printStackTrace();
                }
            }
        }, 100, 100);

    }
}