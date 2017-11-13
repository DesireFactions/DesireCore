package com.desiremc.core.commands.punishment;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.LangHandler;
import com.desiremc.core.api.command.ValidCommand;
import com.desiremc.core.parsers.PlayerSessionParser;
import com.desiremc.core.parsers.StringParser;
import com.desiremc.core.parsers.TimeParser;
import com.desiremc.core.punishment.Punishment.Type;
import com.desiremc.core.punishment.PunishmentHandler;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;
import com.desiremc.core.session.SessionHandler;
import com.desiremc.core.utils.DateUtils;
import com.desiremc.core.validators.PlayerValidator;
import com.desiremc.core.validators.SenderNotTargetValidator;
import com.desiremc.core.validators.SenderOutranksTargetValidator;
import org.bukkit.command.CommandSender;

public class TempMuteCommand extends ValidCommand
{

    private static final LangHandler LANG = DesireCore.getLangHandler();

    public TempMuteCommand()
    {
        super("tempmute", "Temporarily mute a user on the server.", Rank.JRMOD, ValidCommand.ARITY_REQUIRED_VARIADIC,
                new String[] {"target", "time", "reason"});

        addParser(new PlayerSessionParser(), "target");
        addParser(new TimeParser(), "time");
        addParser(new StringParser(), "reason");

        addValidator(new PlayerValidator());
        addValidator(new SenderNotTargetValidator(), "target");
        addValidator(new SenderOutranksTargetValidator(), "target");
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args)
    {
        Session session = SessionHandler.getSession(sender);
        Session target = (Session) args[0];
        long time = (long) args[1];

        PunishmentHandler.getInstance().issuePunishment(Type.MUTE, target.getUniqueId(), session != null ? session
                .getUniqueId() : DesireCore.getConsoleUUID(), time, (String) args[2]);

        LANG.sendRenderMessage(sender, "mute.tempmute_message",
                "{duration}", DateUtils.formatDateDiff(time),
                "{player}", target.getName(),
                "{reason}", args[2]);

        if (target.getOfflinePlayer() != null && target.getOfflinePlayer().isOnline())
        {
            LANG.sendRenderMessage(target, "mute.tempmute_message_target",
                    "{duration}", DateUtils.formatDateDiff(time),
                    "{player}", session.getName(),
                    "{reason}", args[2]);
        }
    }

}
