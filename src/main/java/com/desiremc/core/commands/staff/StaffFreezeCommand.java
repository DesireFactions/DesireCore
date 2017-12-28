package com.desiremc.core.commands.staff;

import com.desiremc.core.api.StaffAPI;
import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.CommandArgumentBuilder;
import com.desiremc.core.api.newcommands.ValidCommand;
import com.desiremc.core.parsers.SessionParser;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;
import com.desiremc.core.validators.SenderOutranksTargetValidator;

import java.util.List;

public class StaffFreezeCommand extends ValidCommand
{

    public StaffFreezeCommand()
    {
        super("freeze", "Freeze a target player", Rank.HELPER, new String[] { "ss" });

        addArgument(CommandArgumentBuilder.createBuilder(Session.class)
                .setName("target")
                .setParser(new SessionParser())
                .addValidator(new SenderOutranksTargetValidator())
                .build());

    }

    @Override
    public void validRun(Session sender, String label[], List<CommandArgument<?>> args)
    {
        Session target = (Session) args.get(0).getValue();
        StaffAPI.freeze(sender, target.getPlayer());
    }

}
