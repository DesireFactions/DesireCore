package com.desiremc.core.commands.tokens;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.CommandArgumentBuilder;
import com.desiremc.core.api.newcommands.ValidCommand;
import com.desiremc.core.parsers.SessionParser;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;

import java.util.List;

public class CheckTokensCommand extends ValidCommand
{
    public CheckTokensCommand()
    {
        super("check", "Check a players tokens.", Rank.GUEST);

        addArgument(CommandArgumentBuilder.createBuilder(Session.class)
                .setName("target")
                .setParser(new SessionParser())
                .setRequiredRank(Rank.HELPER)
                .setOptional()
                .build());
    }

    @Override
    public void validRun(Session sender, String[] label, List<CommandArgument<?>> args)
    {
        Session target;

        if (!args.get(0).hasValue())
        {
            target = sender;
        }
        else
        {
            target = (Session) args.get(0).getValue();
        }

        DesireCore.getLangHandler().sendRenderMessage(sender, "tokens.check",
                "{amount}", target.getTokens(),
                "{player}", target.getName());
    }
}
