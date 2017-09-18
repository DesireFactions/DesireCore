package com.desiremc.core.validators;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.desiremc.core.api.command.CommandValidator;
import com.desiremc.core.session.SessionHandler;

public class SenderNoFriendRequestValidator extends CommandValidator
{

    @Override
    public boolean validateArgument(CommandSender sender, String label, Object arg)
    {
        return SessionHandler.getSession((Player) sender).getIncomingFriendRequests().size() != 0;
    }

}
