package com.desiremc.core.parsers;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.desiremc.core.api.command.ArgumentParser;

public class PlayerParser implements ArgumentParser
{

    @Override
    public Player parseArgument(CommandSender sender, String label, String arg)
    {
        Player player = Bukkit.getPlayer(arg);

        if (player == null)
        {
            LANG.sendString(sender, "player_not_found");
        }

        return player;
    }

}
