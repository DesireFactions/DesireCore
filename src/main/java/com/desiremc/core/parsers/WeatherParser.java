package com.desiremc.core.parsers;

import org.bukkit.command.CommandSender;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.WeatherType;
import com.desiremc.core.api.command.ArgumentParser;

public class WeatherParser implements ArgumentParser
{

    @Override
    public Object parseArgument(CommandSender sender, String label, String arg)
    {
        WeatherType type = WeatherType.getByName(arg);
        if (type == null)
        {
            DesireCore.getLangHandler().sendRenderMessage(sender, "not_weather");
            return null;
        }
        
        return type;
    }

}
