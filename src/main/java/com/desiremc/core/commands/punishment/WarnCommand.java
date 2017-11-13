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

public class WarnCommand extends ValidCommand
{

    private static final LangHandler LANG = DesireCore.getLangHandler();

    public WarnCommand()
    {
        super("warn", "Warn a user on the server.", Rank.MODERATOR, new String[] {"target", "time", "reason"});

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

        PunishmentHandler.getInstance().issuePunishment(Type.WARN, target.getUniqueId(), session != null ? session
                .getUniqueId() : DesireCore.getConsoleUUID(), time, (String) args[2]);

        LANG.sendRenderMessage(sender, "warn.warn_issued",
                "{player}", target.getName(),
                "{reason}", args[2]);

        if (target.getOfflinePlayer() != null && target.getOfflinePlayer().isOnline())
        {
            LANG.sendRenderMessage(target, "warn.warned",
                    "{time}", DateUtils.formatDateDiff(time),
                    "{player}", session.getName(),
                    "{reason}", args[2]);
        }
    }
}
