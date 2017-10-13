package com.desiremc.core.parsers;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.LangHandler;
import org.bukkit.command.CommandSender;

import com.desiremc.core.api.command.ArgumentParser;
import com.desiremc.core.session.Session;
import com.desiremc.core.session.SessionHandler;

public class PlayerSessionParser implements ArgumentParser
{

    private static final LangHandler LANG = DesireCore.getLangHandler();

    @Override
    public Session parseArgument(CommandSender sender, String label, String arg)
    {
        Session s = SessionHandler.getSession(arg);
        
        if (s == null)
        {
            LANG.sendString(sender, "player-not-found");
            return null;
        }

        return s;
    }

}