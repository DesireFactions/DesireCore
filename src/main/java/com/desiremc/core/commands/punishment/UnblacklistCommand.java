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
import com.desiremc.core.validators.punishments.SessionBannedValidator;
import com.desiremc.core.validators.punishments.SessionBlacklistedValidator;
import org.bukkit.Bukkit;

import java.util.List;

public class UnblacklistCommand extends ValidCommand
{

    public UnblacklistCommand()
    {
        super("unblacklist", "Unblacklist a user from the server.", Rank.DEVELOPER);

        addArgument(CommandArgumentBuilder.createBuilder(Session.class)
                .setName("target")
                .setParser(new SessionParser())
                .addValidator(new SessionBannedValidator())
                .addValidator(new SessionBlacklistedValidator())
                .build());

        addArgument(CommandArgumentBuilder.createBuilder(String.class)
                .setName("flag")
                .setParser(new StringParser())
                .build());
    }

    @Override
    public void validRun(Session sender, String[] label, List<CommandArgument<?>> args)
    {
        Session target = (Session) args.get(0).getValue();
        String flag = args.get(1).hasValue() ? (String) args.get(1).getValue() : "";

        for (Punishment punishment : target.getActivePunishments())
        {
            punishment.setBlacklisted(false);
        }

        PunishmentHandler.getInstance().save(target.isBanned());
        PunishmentHandler.getInstance().refreshPunishments(target);

        if (flag.equals("-s"))
        {
            DesireCore.getLangHandler().sendRenderMessage(sender, "blacklist.unblacklist_message", true, false,
                    "{target}", target.getName(),
                    "{player}", sender.getName());
        }
        else
        {
            Bukkit.broadcastMessage(DesireCore.getLangHandler().renderMessage("blacklist.unblacklist_message", true, false,
                    "{target}", target.getName(),
                    "{player}", sender.getName()));
        }
    }
}
