package com.desiremc.core.newparsers;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.newcommands.Parser;
import com.desiremc.core.session.Session;
import com.desiremc.core.session.SessionHandler;

public class SessionParser implements Parser<Session>
{

    @Override
    public Session parseArgument(CommandSender sender, String[] label, String argument)
    {
        Player p = Bukkit.getPlayerExact(argument);
        Session s;
        if (p == null)
        {
            s = SessionHandler.findOfflinePlayerByName(argument);
        }
        else
        {
            s = SessionHandler.getSession(p.getUniqueId());
        }
        if (s == null)
        {
            DesireCore.getLangHandler().sendString(sender, "player-not-found");
            return null;
        }

        return s;
    }

    @Override
    public List<String> getRecommendations(CommandSender sender, String str)
    {
        return null;
    }

}
