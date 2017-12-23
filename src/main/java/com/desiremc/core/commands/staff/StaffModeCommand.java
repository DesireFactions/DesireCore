package com.desiremc.core.commands.staff;

import java.util.List;

import com.desiremc.core.api.StaffAPI;
import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.ValidCommand;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;

public class StaffModeCommand extends ValidCommand
{

    public StaffModeCommand(String name, String... aliases)
    {
        super(name, "Toggle staff mode", Rank.HELPER, true, aliases);
    }

    @Override
    public void validRun(Session sender, String label[], List<CommandArgument<?>> args)
    {
        StaffAPI.toggleStaffMode(sender.getPlayer());
    }

}
