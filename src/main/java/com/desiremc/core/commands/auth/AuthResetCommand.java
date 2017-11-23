package com.desiremc.core.commands.auth;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.command.ValidCommand;
import com.desiremc.core.parsers.PlayerSessionParser;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;
import com.desiremc.core.session.SessionHandler;
import org.bukkit.command.CommandSender;

public class AuthResetCommand extends ValidCommand
{

    public AuthResetCommand()
    {
        super("reset", "Authenticate with Google Auth.", Rank.ADMIN, new String[] { "target" });

        addParser(new PlayerSessionParser(), "target");
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args)
    {
        Session target = (Session) args[0];

        target.setAuthKey("");
        target.setHasAuthorized(false);
        SessionHandler.getInstance().save(target);

        DesireCore.getLangHandler().sendRenderMessage(sender, "auth.reset", "{player}", target.getName());
    }
}
