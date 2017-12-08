package com.desiremc.core.commands.punishment;

import java.util.List;

import org.bukkit.Bukkit;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.CommandArgumentBuilder;
import com.desiremc.core.api.newcommands.ValidCommand;
import com.desiremc.core.newparsers.SessionParser;
import com.desiremc.core.newparsers.StringParser;
import com.desiremc.core.newparsers.TimeParser;
import com.desiremc.core.newvalidators.NumberSizeValidator;
import com.desiremc.core.newvalidators.SenderNotTargetValidator;
import com.desiremc.core.newvalidators.SenderOutranksTargetValidator;
import com.desiremc.core.punishment.Punishment;
import com.desiremc.core.punishment.Punishment.Type;
import com.desiremc.core.punishment.PunishmentHandler;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;
import com.desiremc.core.utils.DateUtils;

public class TempMuteCommand extends ValidCommand
{

    public TempMuteCommand()
    {
        super("tempmute", "Temporarily mute a user on the server.", Rank.JRMOD);

        addArgument(CommandArgumentBuilder.createBuilder(Session.class)
                .setName("target")
                .setParser(new SessionParser())
                .addValidator(new SenderNotTargetValidator())
                .addValidator(new SenderOutranksTargetValidator())
                .build());

        addArgument(CommandArgumentBuilder.createBuilder(Number.class)
                .setName("time")
                .setParser(new TimeParser())
                .addValidator(new NumberSizeValidator(0, 1209600000, "punishment.too_low", "punishment.too_high"))
                .build());

        addArgument(CommandArgumentBuilder.createBuilder(String.class)
                .setName("reason")
                .setParser(new StringParser())
                .build());
    }

    @Override
    public void validRun(Session sender, String[] label, List<CommandArgument<?>> args)
    {
        ;
        Session target = (Session) args.get(0).getValue();
        long time = (Long) args.get(1).getValue();
        String reason = (String) args.get(2).getValue();

        if (reason.contains("-s"))
        {
            reason = reason.replace("-s", "");

            DesireCore.getLangHandler().sendRenderMessage(sender, "mute.temp_silent",
                    "{target}", target.getName(),
                    "{reason}", reason,
                    "{duration}", DateUtils.formatDateDiff(time));
        }
        else
        {
            Bukkit.broadcastMessage(DesireCore.getLangHandler().renderMessage("mute.temp_broadcast",
                    "{target}", target.getName(),
                    "{reason}", reason,
                    "{duration}", DateUtils.formatDateDiff(time),
                    "{player}", sender.getName()));
        }

        Punishment punishment = new Punishment();
        punishment.setIssued(System.currentTimeMillis());
        punishment.setType(Type.MUTE);
        punishment.setPunished(target.getUniqueId());
        punishment.setExpirationTime(time);
        punishment.setIssuer(sender.getUniqueId());
        punishment.setReason(reason);
        PunishmentHandler.getInstance().save(punishment);

        if (target.getOfflinePlayer() != null && target.getOfflinePlayer().isOnline())
        {
            DesireCore.getLangHandler().sendRenderMessage(target, "mute.temp_target",
                    "{duration}", DateUtils.formatDateDiff(time),
                    "{player}", sender.getName(),
                    "{reason}", reason);
        }
    }

}
