package com.desiremc.core.parsers;

import org.bukkit.command.CommandSender;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.command.ArgumentParser;

public class DoubleParser implements ArgumentParser
{

    @Override
    public Double parseArgument(CommandSender sender, String label, String arg)
    {
        try
        {
            return Double.parseDouble(arg);
        }
        catch (NumberFormatException ex)
        {
            DesireCore.getLangHandler().sendString(sender, "arg_not_number");
            return null;
        }
    }

}
