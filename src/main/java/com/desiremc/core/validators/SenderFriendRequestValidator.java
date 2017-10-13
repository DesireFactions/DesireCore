package com.desiremc.core.validators;

import com.desiremc.core.session.SessionHandler;
import org.bukkit.command.CommandSender;

import com.desiremc.core.api.command.CommandValidator;
import com.desiremc.core.session.Session;
import com.desiremc.core.utils.FriendUtils;

public class SenderFriendRequestValidator extends CommandValidator
{

    @Override
    public boolean validateArgument(CommandSender sender, String label, Object arg)
    {
        return FriendUtils.hasRequest((Session) arg, SessionHandler.getSession(sender));
    }

}
