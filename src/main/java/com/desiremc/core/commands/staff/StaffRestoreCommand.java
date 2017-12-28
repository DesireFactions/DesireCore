package com.desiremc.core.commands.staff;

import java.util.List;

import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.CommandArgumentBuilder;
import com.desiremc.core.api.newcommands.ValidCommand;
import com.desiremc.core.parsers.SessionParser;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;
import com.desiremc.core.staff.StaffHandler;
import com.desiremc.core.validators.SessionOnlineValidator;

public class StaffRestoreCommand extends ValidCommand
{
    public StaffRestoreCommand(String name, String... aliases)
    {
        super(name, "Restore a players inventory.", Rank.HELPER, true, aliases);

        addArgument(CommandArgumentBuilder.createBuilder(Session.class)
                .setName("target")
                .setParser(new SessionParser())
                .addValidator(new SessionOnlineValidator())
                .build());
    }

    @Override
    public void validRun(Session sender, String label[], List<CommandArgument<?>> args)
    {
        Session target = (Session) args.get(0).getValue();

        StaffHandler.getInstance().restoreInventory(sender, target);
    }
}