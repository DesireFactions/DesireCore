package com.desiremc.core.commands.punishment;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.LangHandler;
import com.desiremc.core.api.command.ValidCommand;
import com.desiremc.core.parsers.PlayerSessionParser;
import com.desiremc.core.parsers.StringParser;
import com.desiremc.core.punishment.Punishment;
import com.desiremc.core.punishment.Punishment.Type;
import com.desiremc.core.punishment.PunishmentHandler;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;
import com.desiremc.core.session.SessionHandler;
import com.desiremc.core.validators.PlayerValidator;
import com.desiremc.core.validators.SenderNotTargetValidator;
import com.desiremc.core.validators.SenderOutranksTargetValidator;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public class WarnCommand extends ValidCommand
{

    private static final LangHandler LANG = DesireCore.getLangHandler();

    public WarnCommand()
    {
        super("warn", "Warn a user on the server.", Rank.HELPER, ARITY_REQUIRED_VARIADIC, new String[] {"target", "reason"});

        addParser(new PlayerSessionParser(), "target");
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

        if (((String) args[1]).contains("-s"))
        {
            args[1] = ((String) args[1]).replace("-s", "");
        }
        else
        {
            Bukkit.broadcastMessage(LANG.renderMessage("warn.warn_issued", "{target}", target.getName(), "{reason}", args[1], "{player}", sender.getName()));
        }

        Punishment punishment = new Punishment();
        punishment.setIssued(System.currentTimeMillis());
        punishment.setType(Type.WARN);
        punishment.setPunished(target.getUniqueId());
        punishment.setIssuer(session != null ? session.getUniqueId() : DesireCore.getConsoleUUID());
        punishment.setReason((String) args[1]);
        PunishmentHandler.getInstance().save(punishment);

        if (target.getOfflinePlayer() != null && target.getOfflinePlayer().isOnline())
        {
            LANG.sendRenderMessage(target, "warn.warned",
                    "{player}", session.getName(),
                    "{reason}", args[1]);
        }
    }
}
