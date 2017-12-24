package com.desiremc.core.commands.punishment;

import java.util.List;

import org.bukkit.Bukkit;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.CommandArgumentBuilder;
import com.desiremc.core.api.newcommands.ValidCommand;
import com.desiremc.core.parsers.SessionParser;
import com.desiremc.core.parsers.StringParser;
import com.desiremc.core.punishment.Punishment;
import com.desiremc.core.punishment.Punishment.Type;
import com.desiremc.core.punishment.PunishmentHandler;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;
import com.desiremc.core.validators.SenderOutranksTargetValidator;
import com.desiremc.core.validators.friends.SenderNotFriendsValidator;

public class WarnCommand extends ValidCommand
{

    public WarnCommand()
    {
        super("warn", "Warn a user on the server.", Rank.HELPER);

        addArgument(CommandArgumentBuilder.createBuilder(Session.class)
                .setName("target")
                .setParser(new SessionParser())
                .addValidator(new SenderNotFriendsValidator())
                .addValidator(new SenderOutranksTargetValidator())
                .build());

        addArgument(CommandArgumentBuilder.createBuilder(String.class)
                .setName("reason")
                .setParser(new StringParser())
                .setVariableLength()
                .build());

    }

    @Override
    public void validRun(Session sender, String[] label, List<CommandArgument<?>> args)
    {
        Session target = (Session) args.get(0).getValue();
        String reason = (String) args.get(1).getValue();

        if (reason.contains("-s"))
        {
            reason = reason.replace("-s", "");
        }
        else
        {
            Bukkit.broadcastMessage(DesireCore.getLangHandler().renderMessage("warn.warn_issued",
                    "{target}", target.getName(),
                    "{reason}", reason,
                    "{player}", sender.getName()));
        }

        Punishment punishment = new Punishment();
        punishment.setIssued(System.currentTimeMillis());
        punishment.setType(Type.WARN);
        punishment.setPunished(target.getUniqueId());
        punishment.setIssuer(sender.getUniqueId());
        punishment.setReason(reason);
        PunishmentHandler.getInstance().save(punishment);

        if (target.isOnline())
        {
            DesireCore.getLangHandler().sendRenderMessage(target, "warn.warned",
                    "{player}", sender.getName(),
                    "{reason}", reason);
        }
    }
}
