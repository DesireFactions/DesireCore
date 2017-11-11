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
import com.desiremc.core.validators.SenderOutranksTargetValidator;
import org.bukkit.command.CommandSender;

public class BlacklistCommand extends ValidCommand
{

    private static final LangHandler LANG = DesireCore.getLangHandler();

    public BlacklistCommand()
    {
        super("blacklist", "Blacklist a user from the server.", Rank.ADMIN, ValidCommand.ARITY_REQUIRED_VARIADIC, new String[] { "target", "reason" });
        addParser(new PlayerSessionParser(), "target");
        addParser(new StringParser(), "reason");
        addValidator(new PlayerValidator());
        addValidator(new SenderOutranksTargetValidator(), "target");
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args)
    {
        Session session = SessionHandler.getSession(sender);
        Session target = (Session) args[0];
        StringBuilder sb = new StringBuilder();

        if (args.length >= 2)
        {
            for (int i = 1; i < args.length; i++)
            {
                sb.append(args[i] + " ");
            }
        }

        Punishment punishment = new Punishment();
        punishment.setPunished(target.getUniqueId());
        punishment.setIssued(System.currentTimeMillis());
        punishment.setExpirationTime(Long.MAX_VALUE);
        punishment.setReason(sb.toString().trim());
        punishment.setIssuer(session != null ? session.getUniqueId() : DesireCore.getConsoleUUID());
        punishment.setType(Type.BAN);
        punishment.setBlacklisted(true);
        PunishmentHandler.getInstance().save(punishment);

        LANG.sendRenderMessage(sender, "blacklist.blacklist_message",
                "{player}", target.getName(),
                "{reason}", punishment.getReason());
    }
}
