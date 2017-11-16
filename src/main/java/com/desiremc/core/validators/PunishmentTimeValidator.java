package com.desiremc.core.validators;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.command.CommandValidator;
import org.bukkit.command.CommandSender;

public class PunishmentTimeValidator extends CommandValidator
{
    @Override
    public boolean validateArgument(CommandSender sender, String label, Object arg)
    {
        long time = (long) arg;

        if ((time - System.currentTimeMillis()) >= 1209600000)
        {
            DesireCore.getLangHandler().sendRenderMessage(sender, "punishment_too_long");
            return false;
        }
        return true;
    }
}
