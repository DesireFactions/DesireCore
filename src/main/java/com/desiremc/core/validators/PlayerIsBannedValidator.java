package com.desiremc.core.validators;

import org.bukkit.command.CommandSender;

import com.desiremc.core.session.Session;

public class PlayerIsBannedValidator extends PlayerValidator
{

    @Override
    public boolean validateArgument(CommandSender sender, String label, Object arg)
    {
        Session session = (Session) arg;

        if (session == null)
        {
            return false;
        }

        if (session.isBanned() != null)
        {
            LANG.sendString(sender, "not_banned");
            return false;
        }

        return true;
    }

}
