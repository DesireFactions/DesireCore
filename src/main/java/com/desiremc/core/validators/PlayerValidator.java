package com.desiremc.core.validators;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.desiremc.core.api.command.CommandValidator;

public class PlayerValidator extends CommandValidator
{

    @Override
    public boolean validateArgument(CommandSender sender, String label, Object arg)
    {
        if (!(sender instanceof Player))
        {
            LANG.sendString(sender, "only_players");
            return false;
        }

        return true;
    }

}
