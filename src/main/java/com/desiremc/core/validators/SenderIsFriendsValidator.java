package com.desiremc.core.validators;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.desiremc.core.api.command.CommandValidator;
import com.desiremc.core.session.Session;
import com.desiremc.core.utils.FriendUtils;

public class SenderIsFriendsValidator extends CommandValidator
{

    @Override
    public boolean validateArgument(CommandSender sender, String label, Object arg)
    {
        if (!FriendUtils.isFriends((Session) arg, ((Player) sender).getUniqueId()))
        {
            LANG.sendRenderMessage(sender, "friend.not_friends",
                    "{player}", ((Session) arg).getName());
            return false;
        }

        return true;
    }

}
