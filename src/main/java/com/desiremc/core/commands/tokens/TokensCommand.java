package com.desiremc.core.commands.tokens;

import com.desiremc.core.api.newcommands.ValidBaseCommand;
import com.desiremc.core.session.Rank;

public class TokensCommand extends ValidBaseCommand
{
    public TokensCommand()
    {
        super("tokens", "Run and upload timings.", Rank.GUEST, new String[] { "token" });

        addSubCommand(new AddTokensCommand());
        addSubCommand(new CheckTokensCommand());
        addSubCommand(new RemoveTokensCommand());
    }
}