package com.desiremc.core.validators;

import com.desiremc.core.DesireCore;
import com.desiremc.core.session.Session;
import org.bukkit.command.CommandSender;

public class PlayerIsMutedValidator extends PlayerValidator
{

    @Override
    public boolean validateArgument(CommandSender sender, String label, Object arg)
    {
        Session session = (Session) arg;

        if (session.isMuted() == null)
        {
            DesireCore.getLangHandler().sendRenderMessage(sender, "not_muted", "{player}", session.getName());
            return false;
        }

        return true;
    }

}
