package com.desiremc.core.commands.punishment;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.CommandArgumentBuilder;
import com.desiremc.core.api.newcommands.ValidCommand;
import com.desiremc.core.parsers.SessionParser;
import com.desiremc.core.parsers.StringParser;
import com.desiremc.core.punishment.Punishment;
import com.desiremc.core.punishment.PunishmentHandler;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;
import com.desiremc.core.validators.SenderNotTargetValidator;
import com.desiremc.core.validators.SenderOutranksTargetValidator;
import org.bukkit.Bukkit;

import java.util.List;

public class IpbanCommand extends ValidCommand
{

    public IpbanCommand()
    {
        super("ipban", "Permanently ip ban a user from the server.", Rank.ADMIN);

        addArgument(CommandArgumentBuilder.createBuilder(Session.class)
                .setName("target")
                .setParser(new SessionParser())
                .addValidator(new SenderNotTargetValidator())
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
            Bukkit.broadcastMessage(DesireCore.getLangHandler().renderMessage("ipban.ban_message", true, false,
                    "{player}", sender.getName(),
                    "{target}", target.getName(),
                    "{reason}", reason));
        }

        Punishment punishment = new Punishment();
        punishment.setIssued(System.currentTimeMillis());
        punishment.setType(Punishment.Type.IP_BAN);
        punishment.setPunished(target.getUniqueId());
        punishment.setIssuer(sender.getUniqueId());
        punishment.setReason(reason);
        punishment.setPermanent(true);
        punishment.save();

        PunishmentHandler.getInstance().refreshPunishments(target);

        if (target.isOnline())
        {
            target.getPlayer().kickPlayer(("&c&lYour IP is permanently banned from the network!\n"
                    + "&cReason: &7{reason}\n" + "&cBanned By: &7{issuer}\n"
                    + "&7Visit &ehttps://desirehcf.com/rules&7 for our terms and rules")
                            .replace("{reason}", reason)
                            .replace("{issuer}", sender.getName())
                            .replace("&", "§"));
        }
    }
}
