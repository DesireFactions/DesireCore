package com.desiremc.core.commands.auth;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.CommandArgumentBuilder;
import com.desiremc.core.api.newcommands.ValidCommand;
import com.desiremc.core.listeners.AuthListener;
import com.desiremc.core.parsers.PositiveIntegerParser;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;
import com.desiremc.core.validators.auth.AuthCodeValidator;
import com.desiremc.core.validators.auth.PlayerAuthBlockedValidator;

import java.util.List;

public class LoginCommand extends ValidCommand
{

    public LoginCommand()
    {
        super("login", "Authenticate with Google Auth.", Rank.HELPER, true);

        addSenderValidator(new PlayerAuthBlockedValidator());

        addArgument(CommandArgumentBuilder.createBuilder(Integer.class)
                .setName("code")
                .setParser(new PositiveIntegerParser())
                .addValidator(new AuthCodeValidator())
                .build());
    }

    @Override
    public void validRun(Session sender, String label[], List<CommandArgument<?>> args)
    {
        AuthListener.authBlocked.remove(sender.getUniqueId());
        sender.setHasAuthorized(true);
        sender.setHasAuthorizedIP(true);
        sender.save();
        DesireCore.getLangHandler().sendRenderMessage(sender, "auth.authenticated", true, false);
    }

}
