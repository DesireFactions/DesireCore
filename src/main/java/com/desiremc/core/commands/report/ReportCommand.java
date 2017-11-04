package com.desiremc.core.commands.report;

import com.desiremc.core.api.command.ValidBaseCommand;
import com.desiremc.core.commands.staff.StaffReportsCommand;
import com.desiremc.core.session.Rank;

public class ReportCommand extends ValidBaseCommand
{
    public ReportCommand()
    {
        super("report", "Report management tool", Rank.GUEST);
        addSubCommand(new ReportClearCommand());
        addSubCommand(new ReportCreateCommand());
        addSubCommand(new ReportGetCommand());
        addSubCommand(new StaffReportsCommand("view", new String[] {}));
    }
}
