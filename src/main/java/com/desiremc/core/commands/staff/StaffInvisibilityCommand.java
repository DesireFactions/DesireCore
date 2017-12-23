package com.desiremc.core.commands.staff;

import java.util.List;

import com.desiremc.core.api.StaffAPI;
import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.ValidCommand;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;

public class StaffInvisibilityCommand extends ValidCommand
{

    public StaffInvisibilityCommand()
    {
        super("invisibility", "Toggle invisibility.", Rank.HELPER, true, new String[] { "invisible", "invis" });

    }

    @Override
    public void validRun(Session sender, String label[], List<CommandArgument<?>> args)
    {
        StaffAPI.toggleInvisibility(sender.getPlayer(), false);
    }

}
