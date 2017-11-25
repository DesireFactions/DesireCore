package com.desiremc.core.commands.tokens;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.command.ValidCommand;
import com.desiremc.core.parsers.IntegerParser;
import com.desiremc.core.parsers.PlayerSessionParser;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;
import org.bukkit.command.CommandSender;

public class AddTokensCommand extends ValidCommand
{
    public AddTokensCommand()
    {
        super("add", "Add tokens to a player.", Rank.ADMIN, new String[] {"target", "amount"});
        addParser(new PlayerSessionParser(), "target");
        addParser(new IntegerParser(), "amount");
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args)
    {
        Session target = (Session) args[0];
        int amount = (Integer) args[1];

        target.addTokens(amount, true);

        DesireCore.getLangHandler().sendRenderMessage(sender, "tokens.added", "{amount}", amount, "{player}", target.getName());
    }
}
