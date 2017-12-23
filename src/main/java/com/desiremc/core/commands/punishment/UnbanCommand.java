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
import com.desiremc.core.punishment.PunishmentHandler;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;
import com.desiremc.core.validators.punishments.SessionBannedValidator;
import com.desiremc.core.validators.punishments.SessionNotBlacklistedValidator;

public class UnbanCommand extends ValidCommand
{

    public UnbanCommand()
    {
        super("unban", "Unban a user from the server.", Rank.SRMOD);

        addArgument(CommandArgumentBuilder.createBuilder(Session.class)
                .setName("target")
                .setParser(new SessionParser())
                .addValidator(new SessionBannedValidator())
                .addValidator(new SessionNotBlacklistedValidator())
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

        Punishment p = target.isBanned();
        p.setRepealed(true);
        PunishmentHandler.getInstance().save(p);
        PunishmentHandler.getInstance().refreshPunishments(target);

        if (flag.equals("-s"))
        {
            return;
        }
        Bukkit.broadcastMessage(DesireCore.getLangHandler().renderMessage("ban.unban_message",
                "{target}", target.getName(),
                "{player}", sender.getName()));
    }

}
