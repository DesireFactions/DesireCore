package com.desiremc.core.commands.punishment;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.CommandArgumentBuilder;
import com.desiremc.core.api.newcommands.ValidCommand;
import com.desiremc.core.newparsers.SessionParser;
import com.desiremc.core.newparsers.StringParser;
import com.desiremc.core.newvalidators.SenderOutranksTargetValidator;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;
import org.bukkit.Bukkit;

import java.util.List;

public class KickCommand extends ValidCommand
{

    public KickCommand()
    {
        super("kick", "Kick a user from the server.", Rank.HELPER);

        addArgument(CommandArgumentBuilder.createBuilder(Session.class).setName("target").setParser(new SessionParser()).addValidator(new SenderOutranksTargetValidator()).build());
        addArgument(CommandArgumentBuilder.createBuilder(String.class).setName("reason").setParser(new StringParser()).build());

    }

    @Override
    public void validRun(Session sender, String[] label, List<CommandArgument<?>> args)
    {
        Session target = (Session) args.get(0).getValue();
        String reason = (String) args.get(1).getValue();

        if (reason.contains("-s"))
        {
            reason = reason.replace("-s", "");
            DesireCore.getLangHandler().sendRenderMessage(sender, "kick.silent", "{target}", target.getName(), "{reason}", reason);
        }
        else
        {
            Bukkit.broadcastMessage(DesireCore.getLangHandler().renderMessage("kick.broadcast", "{target}", target.getName(), "{reason}", reason, "{player}", sender.getName()));
        }

        target.getPlayer().kickPlayer(DesireCore.getLangHandler().renderMessage("kick.kicked", "{player}", sender.getName(), "{reason}", reason));
    }
}
