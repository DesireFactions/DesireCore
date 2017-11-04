package com.desiremc.core.validators;

import org.bukkit.command.CommandSender;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.command.CommandValidator;
import com.desiremc.core.session.Session;
import com.desiremc.core.session.SessionHandler;
import com.desiremc.core.utils.FriendUtils;

public class SenderIsFriendsValidator extends CommandValidator
{

    @Override
    public boolean validateArgument(CommandSender sender, String label, Object arg)
    {
        if (!FriendUtils.areFriends(SessionHandler.getSession(sender), (Session) arg))
        {
            DesireCore.getLangHandler().sendRenderMessage(sender, "friend.not_friends", "{player}", ((Session) arg).getName());
            return false;
        }

        return true;
    }

}
