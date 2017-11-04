package com.desiremc.core.commands.auth;

import org.bukkit.command.CommandSender;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.command.ValidCommand;
import com.desiremc.core.listeners.AuthListener;
import com.desiremc.core.parsers.PlayerSessionParser;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;

public class AuthAllowCommand extends ValidCommand
{

    public AuthAllowCommand()
    {
        super("allow", "Allow a user to connect.", Rank.DEVELOPER, new String[] { "target" });

        addParser(new PlayerSessionParser(), "target");
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args)
    {
        Session session = (Session) args[0];
        
        AuthListener.authBlocked.remove(session.getUniqueId());
        session.setHasAuthorized(true);
        DesireCore.getLangHandler().sendRenderMessage(session, "auth.allow", "{player}", session.getName());
        
    }

}
