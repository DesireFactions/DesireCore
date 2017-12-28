package com.desiremc.core.commands.timings;

import java.util.List;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.ValidCommand;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;

public class TimingsToggleCommand extends ValidCommand
{

    public TimingsToggleCommand()
    {
        super("toggle", "Toggle timings.", Rank.DEVELOPER, new String[] {});
    }

    @Override
    public void validRun(Session sender, String label[], List<CommandArgument<?>> args)
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
