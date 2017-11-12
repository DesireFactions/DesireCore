package com.desiremc.core.commands.timings;

import org.bukkit.command.CommandSender;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.command.ValidCommand;
import com.desiremc.core.session.Rank;

public class TimingsToggleCommand extends ValidCommand
{

    public TimingsToggleCommand()
    {
        super("toggle", "Toggle timings.", Rank.DEVELOPER, new String[] {});
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args)
    {
        if (DesireCore.toggleTimings())
        {
            sender.sendMessage("Turned on timings.");
        }
        else
        {
            sender.sendMessage("Turned off timings.");
        }
    }

}
