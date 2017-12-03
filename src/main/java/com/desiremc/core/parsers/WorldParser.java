package com.desiremc.core.parsers;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.command.ArgumentParser;

public class WorldParser implements ArgumentParser
{

    @Override
    public Object parseArgument(CommandSender sender, String label, String arg)
    {
        World world = Bukkit.getWorld(arg);
        if (world == null)
        {
            DesireCore.getLangHandler().sendString(sender, "not_world");
            return null;
        }
        return world;
    }

}
