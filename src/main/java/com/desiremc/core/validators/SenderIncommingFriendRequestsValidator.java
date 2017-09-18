package com.desiremc.core.validators;

import org.bukkit.command.CommandSender;

import com.desiremc.core.api.command.CommandValidator;
import com.desiremc.core.session.SessionHandler;

public class SenderIncommingFriendRequestsValidator extends CommandValidator
{

    @Override
    public boolean validateArgument(CommandSender sender, String label, Object arg)
    {
        return SessionHandler.getSession(sender).getIncomingFriendRequests().size() > 0;
    }

}
