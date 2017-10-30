package com.desiremc.core.commands.staff;

import com.desiremc.core.api.command.ValidBaseCommand;
import com.desiremc.core.session.Rank;

public class StaffCommand extends ValidBaseCommand
{

    public StaffCommand()
    {
        super("staff", "staff tools", Rank.ADMIN);
        addSubCommand(new StaffToggleCommand());
        addSubCommand(new StaffFreezeCommand());
        addSubCommand(new StaffClicksPerSecondCommand());
        addSubCommand(new StaffInvisibilityCommand());
        addSubCommand(new StaffMountCommand());
        addSubCommand(new StaffChatCommand());
        addSubCommand(new StaffReportsCommand());
    }

}