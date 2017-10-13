package com.desiremc.core.parsers;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.LangHandler;
import org.bukkit.command.CommandSender;

import com.desiremc.core.api.command.ArgumentParser;
import com.desiremc.core.utils.DateUtils;

public class SecondsParser implements ArgumentParser
{

    private static final LangHandler LANG = DesireCore.getLangHandler();

    @Override
    public Object parseArgument(CommandSender sender, String label, String arg)
    {
        long time;
        try
        {
            time = DateUtils.parseDateDiff(arg, true);
        }
        catch (Exception e)
        {
            LANG.sendString(sender, "not_time");
            return null;
        }
        return time / 1000;
    }

}
