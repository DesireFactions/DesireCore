package com.desiremc.core.validators;

import org.bukkit.command.CommandSender;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.command.CommandValidator;
import com.desiremc.core.session.Session;
import com.desiremc.core.session.SessionHandler;
import com.warrenstrange.googleauth.GoogleAuthenticator;

public class AuthCodeValidator extends CommandValidator
{

    @Override
    public boolean validateArgument(CommandSender sender, String label, Object arg)
    {
        Integer authCode = (Integer) arg;
        Session session = SessionHandler.getSession(sender);
        String key = session.getAuthkey();

        GoogleAuthenticator auth = new GoogleAuthenticator();

        boolean validAuth = auth.authorize(key, authCode);

        if (!validAuth)
        {
            DesireCore.getLangHandler().sendRenderMessage(session, "auth.invalid-code");
        }

        return validAuth;
    }

}
