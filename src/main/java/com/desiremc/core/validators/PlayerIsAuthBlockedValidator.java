package com.desiremc.core.validators;

import com.desiremc.core.DesireCore;
import com.desiremc.core.listeners.AuthListener;
import com.desiremc.core.session.Session;
import org.bukkit.command.CommandSender;

public class PlayerIsAuthBlockedValidator extends PlayerValidator
{
    @Override
    public boolean validateArgument(CommandSender sender, String label, Object arg)
    {
        Session session = (Session) arg;

        if (AuthListener.authBlocked.contains(session.getUniqueId()))
        {
            return true;
        }
        else
        {
            DesireCore.getLangHandler().sendRenderMessage(session, "auth.already-auth");
            return false;
        }
    }
}
