package com.desiremc.core.commands.tokens;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.command.ValidCommand;
import com.desiremc.core.parsers.PlayerSessionParser;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;
import org.bukkit.command.CommandSender;

public class CheckTokensCommand extends ValidCommand
{
    public CheckTokensCommand()
    {
        super("check", "Check a players tokens.", Rank.ADMIN, new String[] {"target"});
        addParser(new PlayerSessionParser(), "target");
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args)
    {
        Session target = (Session) args[0];

        DesireCore.getLangHandler().sendRenderMessage(sender, "tokens.check", "{amount}", target.getTokens(), "{player}", target.getName());
    }
}
