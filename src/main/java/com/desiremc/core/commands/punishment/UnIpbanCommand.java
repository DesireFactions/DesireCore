package com.desiremc.core.commands.punishment;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.LangHandler;
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
import com.desiremc.core.validators.punishments.SessionIPBannedValidator;
import com.desiremc.core.validators.punishments.SessionNotBlacklistedValidator;
import org.bukkit.Bukkit;

import java.util.List;

public class UnIpbanCommand extends ValidCommand
{
    private static final LangHandler LANG = DesireCore.getLangHandler();

    public UnIpbanCommand()
    {
        super("unipban", "Unban a users ip from the server.", Rank.ADMIN);

        addArgument(CommandArgumentBuilder.createBuilder(Session.class)
                .setName("target")
                .setParser(new SessionParser())
                .addValidator(new SessionNotBlacklistedValidator())
                .addValidator(new SessionIPBannedValidator())
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

        Punishment p = PunishmentHandler.getInstance().getPunishment(target.getUniqueId(), Type.IP_BAN);
        p.setRepealed(true);
        PunishmentHandler.getInstance().save(p);
        PunishmentHandler.getInstance().refreshPunishments(target);

        if (flag.equals("-s"))
        {
            return;
        }

        Bukkit.broadcastMessage(LANG.renderMessage("ipban.unban_message", true, false, "{target}", target.getName(), "{player}", sender.getName()));
    }
}
