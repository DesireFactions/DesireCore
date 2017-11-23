package com.desiremc.core.validators;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.command.CommandValidator;
import org.bukkit.command.CommandSender;

public class PunishmentFlagValidator extends CommandValidator
{
    @Override
    public boolean validateArgument(CommandSender sender, String label, Object arg)
    {
        String flag = (String) arg;

        if (!flag.equalsIgnoreCase("-s"))
        {
            DesireCore.getLangHandler().sendRenderMessage(sender, "invalid_flag");
            return false;
        }
        return true;
    }
}
