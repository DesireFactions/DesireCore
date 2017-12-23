package com.desiremc.core.commands.auth;

import java.util.List;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.CommandArgumentBuilder;
import com.desiremc.core.api.newcommands.ValidCommand;
import com.desiremc.core.parsers.SessionParser;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;

public class AuthResetCommand extends ValidCommand
{

    public AuthResetCommand()
    {
        super("reset", "Authenticate with Google Auth.", Rank.ADMIN, new String[] { "target" });

        addArgument(CommandArgumentBuilder.createBuilder(Session.class)
                .setName("target")
                .setParser(new SessionParser())
                .build());
    }

    @Override
    public void validRun(Session sender, String label[], List<CommandArgument<?>> args)
    {
        Session target = (Session) args.get(0).getValue();

        target.setAuthKey("");
        target.setHasAuthorized(false);
        target.save();

        DesireCore.getLangHandler().sendRenderMessage(sender, "auth.reset", "{player}", target.getName());
    }
}
