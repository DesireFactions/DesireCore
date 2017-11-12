package com.desiremc.core.validators;

import org.bukkit.command.CommandSender;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.command.CommandValidator;
import com.desiremc.core.session.Session;
import com.desiremc.core.session.SessionHandler;

public class SenderNotTargetValidator extends CommandValidator
{

    @Override
    public boolean validateArgument(CommandSender sender, String label, Object arg)
    {
        if (arg instanceof CommandSender)
        {
            if (arg == sender)
            {
                sendError(sender);
                return false;
            }
            return true;
        }
        if (arg instanceof Session)
        {
            Session s = SessionHandler.getSession(sender);
            if (s == arg)
            {
                sendError(sender);
                return false;
            }
            return true;
        }

        return false;
    }

    private void sendError(CommandSender sender)
    {
        DesireCore.getLangHandler().sendRenderMessage(sender, "cant_to_self");
    }

}
