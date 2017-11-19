package com.desiremc.core.parsers;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.command.ArgumentParser;
import com.desiremc.core.utils.DateUtils;
import org.bukkit.command.CommandSender;

public class PastTimeParser implements ArgumentParser
{

    @Override
    public Object parseArgument(CommandSender sender, String label, String arg)
    {
        try
        {
            return DateUtils.parseDateDiff(arg, false);
        } catch (Exception e)
        {
            DesireCore.getLangHandler().sendString(sender, "not_time");
            return null;
        }
    }

}
