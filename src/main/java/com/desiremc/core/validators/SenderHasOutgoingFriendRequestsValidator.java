package com.desiremc.core.validators;

import org.bukkit.command.CommandSender;

import com.desiremc.core.api.command.CommandValidator;
import com.desiremc.core.session.SessionHandler;

public class SenderHasOutgoingFriendRequestsValidator extends CommandValidator
{

    @Override
    public boolean validateArgument(CommandSender sender, String label, Object arg)
    {
        if (SessionHandler.getSession(sender).getOutgoingFriendRequests().size() > 0)
        {
            return true;
        }
        else 
        {
            LANG.sendString(sender, "friend.no_outgoing");
            return false;
        }
    }

}
