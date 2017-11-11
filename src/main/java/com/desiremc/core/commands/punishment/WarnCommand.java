package com.desiremc.core.commands.punishment;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.LangHandler;
import com.desiremc.core.api.command.ValidCommand;
import com.desiremc.core.parsers.PlayerSessionParser;
import com.desiremc.core.parsers.StringParser;
import com.desiremc.core.parsers.TimeParser;
import com.desiremc.core.punishment.Punishment;
import com.desiremc.core.punishment.Punishment.Type;
import com.desiremc.core.punishment.PunishmentHandler;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;
import com.desiremc.core.session.SessionHandler;
import com.desiremc.core.utils.DateUtils;
import com.desiremc.core.validators.PlayerValidator;
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
        addValidator(new SenderOutranksTargetValidator(), "target");
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args)
    {
        Session session = SessionHandler.getSession(sender);
        Session target = (Session) args[0];
        long time = (long) args[1];

        Punishment punishment = new Punishment();
        punishment.setPunished(target.getUniqueId());
        punishment.setIssued(System.currentTimeMillis());
        punishment.setExpirationTime(time);
        punishment.setReason((String) args[1]);
        punishment.setIssuer(session != null ? session.getUniqueId() : DesireCore.getConsoleUUID());
        punishment.setType(Type.WARN);
        PunishmentHandler.getInstance().save(punishment);

        LANG.sendRenderMessage(sender, "warn.warned",
                "{time}", DateUtils.formatDateDiff(time),
                "{player}", target.getName(),
                "{reason}", punishment.getReason());
    }
}
