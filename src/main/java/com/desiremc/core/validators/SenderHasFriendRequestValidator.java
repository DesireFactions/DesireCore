package com.desiremc.core.validators;

import org.bukkit.command.CommandSender;

import com.desiremc.core.api.command.CommandValidator;
import com.desiremc.core.session.Session;
import com.desiremc.core.session.SessionHandler;
import com.desiremc.core.utils.FriendUtils;

public class SenderHasFriendRequestValidator extends CommandValidator
{

    @Override
    public boolean validateArgument(CommandSender sender, String label, Object arg)
    {
        if (FriendUtils.hasRequest(SessionHandler.getSession(sender), (Session) arg))
        {
            return true;
        }
        else
        {
            LANG.sendRenderMessage(sender, "friend.no_request", "{player}", ((Session) arg).getName());
            return false;
        }
    }

}
