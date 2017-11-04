package com.desiremc.core.validators;

import org.bukkit.command.CommandSender;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.command.CommandValidator;
import com.desiremc.core.session.SessionHandler;

public class SenderHasIncomingFriendRequestsValidator extends CommandValidator
{

    @Override
    public boolean validateArgument(CommandSender sender, String label, Object arg)
    {
        if (SessionHandler.getSession(sender).getIncomingFriendRequests().size() > 0)
        {
            return true;
        }
        else
        {
            DesireCore.getLangHandler().sendString(sender, "friend.no_incoming");
            return false;
        }
    }

}
