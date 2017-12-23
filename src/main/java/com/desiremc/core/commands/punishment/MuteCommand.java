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
import com.desiremc.core.validators.SenderNotTargetValidator;
import com.desiremc.core.validators.SenderOutranksTargetValidator;

public class MuteCommand extends ValidCommand
{

    public MuteCommand()
    {
        super("mute", "Permanently mute a user on the server.", Rank.MODERATOR, new String[] { "target", "reason" });

        addArgument(CommandArgumentBuilder.createBuilder(Session.class)
                .setName("target")
                .setParser(new SessionParser())
                .addValidator(new SenderNotTargetValidator())
                .addValidator(new SenderOutranksTargetValidator())
                .build());

        addArgument(CommandArgumentBuilder.createBuilder(String.class)
                .setName("reason")
                .setParser(new StringParser())
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
            DesireCore.getLangHandler().sendRenderMessage(sender, "mute.perm_silent",
                    "{target}", target.getName(),
                    "{reason}", reason);
        }
        else
        {
            Bukkit.broadcastMessage(DesireCore.getLangHandler().renderMessage("mute.perm_broadcast",
                    "{target}", target.getName(),
                    "{reason}", reason,
                    "{player}", sender.getName()));
        }

        Punishment punishment = new Punishment();
        punishment.setIssued(System.currentTimeMillis());
        punishment.setType(Type.MUTE);
        punishment.setPunished(target.getUniqueId());
        punishment.setIssuer(sender.getUniqueId());
        punishment.setReason(reason);
        punishment.setPermanent(true);
        PunishmentHandler.getInstance().save(punishment);

        PunishmentHandler.getInstance().refreshPunishments(target);

        if (target.getOfflinePlayer() != null && target.getOfflinePlayer().isOnline())
        {
            DesireCore.getLangHandler().sendRenderMessage(target, "mute.perm_target",
                    "{player}", sender.getName(),
                    "{reason}", reason);
        }
    }
}
