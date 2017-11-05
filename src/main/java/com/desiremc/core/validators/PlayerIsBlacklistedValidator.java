package com.desiremc.core.validators;

import com.desiremc.core.DesireCore;
import com.desiremc.core.punishment.Punishment;
import com.desiremc.core.session.Session;
import org.bukkit.command.CommandSender;

public class PlayerIsBlacklistedValidator extends PlayerValidator
{
    @Override
    public boolean validateArgument(CommandSender sender, String label, Object arg)
    {
        Session session = (Session) arg;

        if (session == null)
        {
            return false;
        }

        for(Punishment punishment : session.getActivePunishments())
        {
            if(punishment.isBlacklisted())
            {
                DesireCore.getLangHandler().sendRenderMessage(sender, "not_blacklisted");
                return false;
            }
        }

        return true;
    }
}
