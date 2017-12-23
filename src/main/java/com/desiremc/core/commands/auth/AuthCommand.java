package com.desiremc.core.commands.auth;

import com.desiremc.core.api.newcommands.ValidBaseCommand;
import com.desiremc.core.session.Rank;

public class AuthCommand extends ValidBaseCommand
{

    public AuthCommand()
    {
        super("auth", "Authorization system.", Rank.HELPER, new String[] { "authorize", "authorization" });
        addSubCommand(new AuthAllowCommand());
        addSubCommand(new AuthResetCommand());
    }

}
