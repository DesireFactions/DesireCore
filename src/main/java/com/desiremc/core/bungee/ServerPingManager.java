package com.desiremc.core.bungee;

import java.io.IOException;
import java.net.InetSocketAddress;

public class ServerPingManager
{

    public StatusResponse getServerInfo(String address, int port, int timeout)
    {
        try
        {
            return new LoadData().fetchData(new InetSocketAddress(address, port), timeout);
        }
        catch (IOException e)
        {
            return null;
        }
    }

    public StatusResponse getServerInfo(String address, int port)
    {
        return getServerInfo(address, port, 250);
    }

    public StatusResponse getServerInfo(String address)
    {
        return getServerInfo(address, 25565, 250);
    }

    public boolean testInfo(String address, int port, int timeout)
    {
        try
        {
            StatusResponse localStatusResponse = new LoadData().fetchData(new InetSocketAddress(address, port), timeout);
            System.out.println("MOTD: " + localStatusResponse.getDescription());
            System.out.println("OnlinePlayers: " + localStatusResponse.getOnlinePlayers());
            System.out.println("MaxPlayers: " + localStatusResponse.getMaxPlayers());
            System.out.println("Version: " + localStatusResponse.getVersion());
            System.out.println("Protocol: " + localStatusResponse.getProtocol());
            System.out.println("Players: " + localStatusResponse.getPlayersName());
            return true;
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
        return false;
    }

    public boolean testInfo(String address, int port)
    {
        return testInfo(address, port, 250);
    }

    public boolean testInfo(String address)
    {
        return testInfo(address, 22565, 250);
    }
}
