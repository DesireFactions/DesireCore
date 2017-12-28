package com.desiremc.core.commands.auth;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.CommandArgumentBuilder;
import com.desiremc.core.api.newcommands.ValidCommand;
import com.desiremc.core.listeners.AuthListener;
import com.desiremc.core.parsers.SessionParser;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;

import java.util.List;

public class AuthAllowCommand extends ValidCommand
{

    public AuthAllowCommand()
    {
        super("allow", "Allow a user to connect.", Rank.DEVELOPER, new String[] { "target" });

        addArgument(CommandArgumentBuilder.createBuilder(Session.class)
                .setName("target")
                .setParser(new SessionParser())
                .build());
    }

    @Override
    public void validRun(Session sender, String label[], List<CommandArgument<?>> args)
    {
        Session session = (Session) args.get(0).getValue();

        AuthListener.authBlocked.remove(session.getUniqueId());
        session.setHasAuthorized(true);
        DesireCore.getLangHandler().sendRenderMessage(sender, "auth.allow", true, false, "{player}", session.getName());

    }

}
