package com.desiremc.core.commands.timings;

import com.desiremc.core.api.command.ValidBaseCommand;
import com.desiremc.core.session.Rank;

public class TimingsCommand extends ValidBaseCommand
{

    public TimingsCommand()
    {
        super("timings", "Run and upload timings.", Rank.DEVELOPER, new String[] { "timing" });

        addSubCommand(new TimingsToggleCommand());
        addSubCommand(new TimingsReportCommand());
    }

}
