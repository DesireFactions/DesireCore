package com.desiremc.core.bungee;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class StatusResponse17
        implements StatusResponse
{
    private String description;
    private Players players;
    private Version version;
    private String favicon;

    public Integer getOnlinePlayers()
    {
        return Integer.valueOf(this.players.getOnline());
    }

    public Integer getMaxPlayers()
    {
        return Integer.valueOf(this.players.getMax());
    }

    public List<String> getPlayersName()
    {
        return this.players.getPlayersByName();
    }

    public List<UUID> getPlayersUUID()
    {
        return this.players.getPlayersByUUID();
    }

    public String getDescription()
    {
        return this.description;
    }

    public Integer getProtocol()
    {
        return Integer.valueOf(this.version.getProtocol());
    }

    public String getVersion()
    {
        return this.version.getName();
    }

    public String getFavicon()
    {
        return this.favicon;
    }

    public List<Player> getPlayers()
    {
        return this.players.getPlayers();
    }

    public class Players
    {
        private int max;
        private int online;
        private List<StatusResponse17.Player> sample;

        public Players()
        {
        }

        public int getMax()
        {
            return this.max;
        }

        public int getOnline()
        {
            return this.online;
        }

        public List<StatusResponse17.Player> getPlayers()
        {
            return this.sample;
        }

        public List<String> getPlayersByName()
        {
            ArrayList<String> localArrayList = new ArrayList<>();
            if ((this.sample != null) && (!this.sample.isEmpty()))
            {
                for (StatusResponse17.Player localPlayer : this.sample)
                {
                    localArrayList.add(localPlayer.getName());
                }
            }
            return localArrayList;
        }

        public List<UUID> getPlayersByUUID()
        {
            ArrayList<UUID> localArrayList = new ArrayList<>();
            if ((this.sample != null) && (!this.sample.isEmpty()))
            {
                for (StatusResponse17.Player localPlayer : this.sample)
                {
                    localArrayList.add(UUID.fromString(localPlayer.getUuid()));
                }
            }
            return localArrayList;
        }
    }

    public class Player
    {
        private String name;
        private String uuid;

        public Player()
        {
        }

        public String getName()
        {
            return this.name;
        }

        public String getUuid()
        {
            return this.uuid;
        }
    }

    public class Version
    {
        private String name;
        private int protocol;

        public Version()
        {
        }

        public String getName()
        {
            return this.name;
        }

        public int getProtocol()
        {
            return this.protocol;
        }
    }
}
