package com.desiremc.core.commands.auth;

import com.desiremc.core.api.command.ValidBaseCommand;
import com.desiremc.core.session.Rank;

public class AuthCommand extends ValidBaseCommand
{

    public AuthCommand()
    {
        super("auth", "Authorization system.", Rank.HELPER, "authorize", "authorization");
        addSubCommand(new AuthAllowCommand());
        addSubCommand(new AuthResetCommand());
    }

}
