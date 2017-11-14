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
import org.bukkit.command.CommandSender;

public class BlacklistCommand extends ValidCommand
{

    private static final LangHandler LANG = DesireCore.getLangHandler();

    public BlacklistCommand()
    {
        super("blacklist", "Blacklist a user from the server.", Rank.ADMIN, ValidCommand.ARITY_REQUIRED_VARIADIC, new
                String[] {"target", "reason"});

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

        Punishment punishment = new Punishment();
        punishment.setIssued(System.currentTimeMillis());
        punishment.setType(Type.BAN);
        punishment.setPunished(target.getUniqueId());
        punishment.setIssuer(session != null ? session.getUniqueId() : DesireCore.getConsoleUUID());
        punishment.setReason((String) args[1]);
        punishment.setBlacklisted(true);
        PunishmentHandler.getInstance().save(punishment);

        PunishmentHandler.getInstance().refreshPunishments(target);

        LANG.sendRenderMessage(sender, "blacklist.blacklist_message",
                "{player}", target.getName(),
                "{reason}", args[2]);

        if (target.getOfflinePlayer() != null && target.getOfflinePlayer().isOnline())
        {
            LANG.sendRenderMessage(target, "blacklist.blacklist_message_target",
                    "{player}", session.getName(),
                    "{reason}", args[2]);
        }
    }
}
