package com.desiremc.core.commands.auth;

import org.bukkit.command.CommandSender;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.command.ValidCommand;
import com.desiremc.core.listeners.AuthListener;
import com.desiremc.core.parsers.IntegerParser;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;
import com.desiremc.core.session.SessionHandler;
import com.desiremc.core.validators.AuthCodeValidator;
import com.desiremc.core.validators.PlayerIsAuthBlockedValidator;
import com.desiremc.core.validators.PlayerValidator;

public class AuthLoginCommand extends ValidCommand
{

    public AuthLoginCommand()
    {
        super("login", "Authenticate with Google Auth.", Rank.JRMOD, new String[] { "code" });
        addParser(new IntegerParser(), "code");

        addValidator(new PlayerValidator());
        addValidator(new PlayerIsAuthBlockedValidator());
        addValidator(new AuthCodeValidator(), "code");
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args)
    {
        Session session = SessionHandler.getSession(sender);

        AuthListener.authBlocked.remove(session.getUniqueId());
        session.setHasAuthorized(true);
        DesireCore.getLangHandler().sendRenderMessage(session, "auth.authenticated");
    }

}
