package com.desiremc.core.bungee;

import com.desiremc.core.DesireCore;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

public class StatusManager
{
    public static Map<String, ServerStatus> servers = new ConcurrentHashMap<>();

    private static ServerPingManager spm = new ServerPingManager();

    public static ServerPingManager getServerPingManager()
    {
        return spm;
    }

    public static boolean isOnline(String server)
    {
        if (servers.containsKey(server))
        {
            return servers.get(server).isOnline();
        }
        return false;
    }

    public static int getOnlinePlayers(String server)
    {
        if (servers.containsKey(server))
        {
            return servers.get(server).getOnlinePlayers();
        }
        return 0;
    }

    public static int getMaxPlayers(String server)
    {
        if (servers.containsKey(server))
        {
            return servers.get(server).getMaxPlayers();
        }
        return 0;
    }

    public static ServerStatus getServerStatus(String server)
    {
        if (servers.containsKey(server))
        {
            return servers.get(server);
        }
        return null;
    }

    private static void updateStatus(String server, boolean online, int onlinePlayers, int maxPlayers)
    {
        ServerStatus serverStatus = new ServerStatus(server);
        serverStatus.setMaxPlayers(maxPlayers);
        serverStatus.setOnline(online);
        serverStatus.setOnlinePlayers(onlinePlayers);
        servers.put(server, serverStatus);
    }

    public static String getLeastPopulous()
    {
        if (DesireCore.DEBUG)
        {
            System.out.println("getLeastPopulous() called.");
        }
        String leastPopulated = null;
        ServerStatus leastPopulatedStatus = null;
        ServerStatus it;
        if (DesireCore.DEBUG)
        {
            System.out.println("getLeastPopulous() servers size is " + servers.size());
        }
        for (Entry<String, ServerStatus> server : servers.entrySet())
        {
            if (DesireCore.DEBUG)
            {
                System.out.println("getLeastPopulous() checking if " + server.getKey() + " is a lobby.");
            }
            it = servers.get(server.getKey());
            if (it != null && it.isOnline())
            {
                if (DesireCore.DEBUG)
                {
                    System.out.println("getLeastPopulous() it is a lobby.");
                }
                if (leastPopulated == null)
                {
                    if (DesireCore.DEBUG)
                    {
                        System.out.println("getLeastPopulous() it is the least populous lobby.");
                    }
                    leastPopulated = server.getKey();
                    leastPopulatedStatus = it;
                }
                else if (it.getOnlinePlayers() < leastPopulatedStatus.getOnlinePlayers())
                {
                    if (DesireCore.DEBUG)
                    {
                        System.out.println("getLeastPopulous() it is the least populous lobby.");
                    }
                    leastPopulated = server.getKey();
                    leastPopulatedStatus = it;
                }
            }

        }
        if (DesireCore.DEBUG)
        {
            System.out.println("getLeastPopulous() returned server " + leastPopulated);
        }
        return leastPopulated;
    }
    
    public static void startPingTask()
    {
        Bukkit.getScheduler().runTaskTimerAsynchronously(DesireCore.getInstance(), new Runnable()
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