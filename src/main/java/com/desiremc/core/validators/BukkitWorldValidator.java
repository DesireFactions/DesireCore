package com.desiremc.core.validators;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.command.CommandValidator;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public class BukkitWorldValidator extends CommandValidator
{
    @Override
    public boolean validateArgument(CommandSender sender, String label, Object arg)
    {
        String world = (String) arg;
        if (Bukkit.getWorld(world) == null)
        {
            DesireCore.getLangHandler().sendString(sender, "not_world");
            return false;
        }
        return true;
    }
}
