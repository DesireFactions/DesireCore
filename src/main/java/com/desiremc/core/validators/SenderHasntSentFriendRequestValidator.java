package com.desiremc.core.validators;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.command.CommandValidator;
import com.desiremc.core.session.Session;
import com.desiremc.core.session.SessionHandler;
import com.desiremc.core.utils.FriendUtils;
import org.bukkit.command.CommandSender;

public class SenderHasntSentFriendRequestValidator extends CommandValidator
{
    @Override
    public boolean validateArgument(CommandSender sender, String label, Object arg)
    {
        Session session = SessionHandler.getSession(sender);

        if (FriendUtils.hasRequest((Session) arg, session))
        {
            DesireCore.getLangHandler().sendString(sender, "already_sent_request");
            return false;
        }
        return true;
    }
}
