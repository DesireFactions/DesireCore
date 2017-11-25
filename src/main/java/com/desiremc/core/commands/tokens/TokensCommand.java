package com.desiremc.core.commands.tokens;

import com.desiremc.core.api.command.ValidBaseCommand;
import com.desiremc.core.session.Rank;

public class TokensCommand extends ValidBaseCommand
{
    public TokensCommand()
    {
        super("tokens", "Run and upload timings.", Rank.GUEST, "token");

        addSubCommand(new AddTokensCommand());
        addSubCommand(new CheckTokensCommand());
        addSubCommand(new RemoveTokensCommand());
    }
}