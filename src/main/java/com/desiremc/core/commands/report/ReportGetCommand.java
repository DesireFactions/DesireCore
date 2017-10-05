package com.desiremc.core.commands.report;

import com.desiremc.core.api.command.ValidCommand;
import com.desiremc.core.session.Rank;
import org.bukkit.command.CommandSender;


public class ReportGetCommand extends ValidCommand
{
    public ReportGetCommand()
    {
        super("get", "Get a players reports.", Rank.GUEST, new String[]{"target"});
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args)
    {

    }
}
