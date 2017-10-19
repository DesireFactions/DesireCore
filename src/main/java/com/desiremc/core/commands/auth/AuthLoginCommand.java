package com.desiremc.core.commands.auth;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.command.ValidCommand;
import com.desiremc.core.listeners.AuthListener;
import com.desiremc.core.parsers.IntegerParser;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;
import com.desiremc.core.session.SessionHandler;
import com.desiremc.core.validators.PlayerValidator;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import org.bukkit.command.CommandSender;

public class AuthLoginCommand extends ValidCommand
{

    public AuthLoginCommand()
    {
        super("login", "Authenticate with Google Auth.", Rank.JRMOD, new String[]{"code"});
        addParser(new IntegerParser(), "code");
        addValidator(new PlayerValidator());
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args)
    {
        Session session = SessionHandler.getSession(sender);

        if (AuthListener.authBlocked.contains(session.getUniqueId()))
        {
            Integer code = Integer.parseInt(args[0] + "");

            playerAuth(session, code);
        }
        else
        {
            DesireCore.getLangHandler().sendRenderMessage(session, "auth.already-auth");
        }
    }

    private void playerAuth(Session session, int authCode)
    {
        String key = session.getAuthkey();

        GoogleAuthenticator auth = new GoogleAuthenticator();

        boolean validAuth = auth.authorize(key, authCode);

        if (validAuth)
        {
            AuthListener.authBlocked.remove(session.getUniqueId());
            DesireCore.getLangHandler().sendRenderMessage(session, "auth.authenticated");
        }
        else
        {
            DesireCore.getLangHandler().sendRenderMessage(session, "auth.invalid-code");
        }
    }
}
