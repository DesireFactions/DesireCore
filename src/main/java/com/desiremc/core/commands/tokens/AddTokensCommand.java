package com.desiremc.core.commands.tokens;

import java.util.List;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.CommandArgumentBuilder;
import com.desiremc.core.api.newcommands.ValidCommand;
import com.desiremc.core.newparsers.PositiveIntegerParser;
import com.desiremc.core.newparsers.SessionParser;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;

public class AddTokensCommand extends ValidCommand
{
    public AddTokensCommand()
    {
        super("add", "Add tokens to a player.", Rank.ADMIN);

        addArgument(CommandArgumentBuilder.createBuilder(Session.class).setName("target").setParser(new SessionParser()).build());
        addArgument(CommandArgumentBuilder.createBuilder(Integer.class).setName("amount").setParser(new PositiveIntegerParser()).build());
    }

    @Override
    public void validRun(Session sender, String[] label, List<CommandArgument<?>> args)
    {
        Session target = (Session) args.get(0).getValue();
        int amount = (Integer) args.get(1).getValue();

        target.addTokens(amount, true);

        DesireCore.getLangHandler().sendRenderMessage(sender, "tokens.added",
                "{amount}", amount,
                "{player}", target.getName());
    }
}
