package com.desiremc.core.bungee;

import java.util.List;
import java.util.UUID;

public abstract interface StatusResponse
{
    public abstract Integer getOnlinePlayers();

    public abstract Integer getMaxPlayers();

    public abstract Integer getProtocol();

    public abstract String getVersion();

    public abstract String getDescription();

    public abstract String getFavicon();

    public abstract List<String> getPlayersName();

    public abstract List<UUID> getPlayersUUID();
}
