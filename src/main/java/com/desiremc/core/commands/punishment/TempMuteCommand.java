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
import com.desiremc.core.validators.PunishmentTimeValidator;
import com.desiremc.core.validators.SenderNotTargetValidator;
import com.desiremc.core.validators.SenderOutranksTargetValidator;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public class TempMuteCommand extends ValidCommand
{

    private static final LangHandler LANG = DesireCore.getLangHandler();

    public TempMuteCommand()
    {
        super("tempmute", "Temporarily mute a user on the server.", Rank.JRMOD, ValidCommand.ARITY_REQUIRED_VARIADIC, new String[] {"target", "time", "reason"});

        addParser(new PlayerSessionParser(), "target");
        addParser(new TimeParser(), "time");
        addParser(new StringParser(), "reason");

        addValidator(new PlayerValidator());
        addValidator(new SenderNotTargetValidator(), "target");
        addValidator(new SenderOutranksTargetValidator(), "target");
        addValidator(new PunishmentTimeValidator(), "time");
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args)
    {
        Session session = SessionHandler.getSession(sender);
        Session target = (Session) args[0];
        long time = (long) args[1];

        if (((String) args[2]).contains("-s"))
        {
            args[2] = ((String) args[2]).replace("-s", "");
        }
        else
        {
            Bukkit.broadcastMessage(LANG.renderMessage("mute.tempmute_message", "{duration}", DateUtils.formatDateDiff(time), "{target}", target.getName(), "{reason}", args[2], "{player}", sender.getName()));
        }

        Punishment punishment = new Punishment();
        punishment.setIssued(System.currentTimeMillis());
        punishment.setType(Type.MUTE);
        punishment.setPunished(target.getUniqueId());
        punishment.setExpirationTime(time);
        punishment.setIssuer(session != null ? session.getUniqueId() : DesireCore.getConsoleUUID());
        punishment.setReason((String) args[2]);
        PunishmentHandler.getInstance().save(punishment);

        if (target.getOfflinePlayer() != null && target.getOfflinePlayer().isOnline())
        {
            LANG.sendRenderMessage(target, "mute.tempmute_message_target", "{duration}", DateUtils.formatDateDiff(time), "{player}", session.getName(), "{reason}", args[2]);
        }
    }

}
