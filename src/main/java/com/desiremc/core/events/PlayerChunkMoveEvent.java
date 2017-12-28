package com.desiremc.core.events;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class PlayerChunkMoveEvent extends PlayerBlockMoveEvent
{

    public PlayerChunkMoveEvent(Player player, Location from, Location to)
    {
        super(player, from, to);
    }

}
