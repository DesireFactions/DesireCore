package com.desiremc.core.commands.staff;

import com.desiremc.core.api.command.ValidBaseCommand;
import com.desiremc.core.session.Rank;

public class StaffCommand extends ValidBaseCommand
{

    public StaffCommand()
    {
        super("staff", "staff tools", Rank.JRMOD);
        addSubCommand(new StaffModeCommand("mode", "toggle", "mod"));
        addSubCommand(new StaffFreezeCommand());
        addSubCommand(new StaffCPSCommand());
        addSubCommand(new StaffInvisibilityCommand());
        addSubCommand(new StaffFollowCommand());
        addSubCommand(new StaffChatCommand("chat", "sc"));
        addSubCommand(new StaffReportsCommand("reports", "report"));
    }

}