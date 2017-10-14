package com.desiremc.core.parsers;

import org.bukkit.command.CommandSender;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.command.ArgumentParser;
import com.desiremc.core.utils.DateUtils;

public class TimeParser implements ArgumentParser
{

    @Override
    public Object parseArgument(CommandSender sender, String label, String arg)
    {
        try
        {
            return DateUtils.parseDateDiff(arg, true);
        }
        catch (Exception e)
        {
            DesireCore.getLangHandler().sendString(sender, "not_time");
            return null;
        }
    }

}
