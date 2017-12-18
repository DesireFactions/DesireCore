package com.desiremc.core.parsers;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.command.ArgumentParser;
import com.desiremc.core.session.Session;
import com.desiremc.core.session.SessionHandler;

public class PlayerSessionParser implements ArgumentParser
{

    @Override
    public Session parseArgument(CommandSender sender, String label, String arg)
    {
        Player p = Bukkit.getPlayerExact(arg);
        Session s;
        if (p == null)
        {
            s = SessionHandler.findOfflinePlayerByName(arg);
        }
        else
        {
            s = SessionHandler.getOnlineSession(p.getUniqueId());
        }
        if (s == null)
        {
            DesireCore.getLangHandler().sendString(sender, "player-not-found");
            return null;
        }

        return s;
    }

}