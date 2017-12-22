package com.desiremc.core.parsers;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.command.ArgumentParser;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PlayerParser implements ArgumentParser
{

    @Override
    public Player parseArgument(CommandSender sender, String label, String arg)
    {
        Player player = Bukkit.getPlayer(arg);

        if (player == null)
        {
            DesireCore.getLangHandler().sendString(sender, "player_not_found");
            return null;
        }

        return player;
    }

}
