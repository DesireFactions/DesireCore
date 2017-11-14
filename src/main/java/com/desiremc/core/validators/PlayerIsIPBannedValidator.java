package com.desiremc.core.validators;

import com.desiremc.core.DesireCore;
import com.desiremc.core.session.Session;
import org.bukkit.command.CommandSender;

public class PlayerIsIPBannedValidator extends PlayerValidator
{

    @Override
    public boolean validateArgument(CommandSender sender, String label, Object arg)
    {
        Session session = (Session) arg;

        if (session.isIPBanned() == null)
        {
            DesireCore.getLangHandler().sendRenderMessage(sender, "not_ip_banned", "{player}", session.getName());
            return false;
        }

        return true;
    }
}
