package com.desiremc.core.commands.punishment;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.LangHandler;
import com.desiremc.core.api.command.ValidCommand;
import com.desiremc.core.parsers.PastTimeParser;
import com.desiremc.core.parsers.PlayerSessionParser;
import com.desiremc.core.punishment.Punishment;
import com.desiremc.core.punishment.PunishmentHandler;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;
import com.desiremc.core.session.SessionHandler;
import com.desiremc.core.utils.DateUtils;
import com.desiremc.core.validators.PlayerValidator;
import com.desiremc.core.validators.PunishmentTimeValidator;
import org.bukkit.command.CommandSender;

public class RollbackCommand extends ValidCommand
{

    private static final LangHandler LANG = DesireCore.getLangHandler();

    public RollbackCommand()
    {
        super("rollback", "Rollback all punishments from a user within a time period.", Rank.JRMOD, ValidCommand.ARITY_REQUIRED_VARIADIC, new String[] {"target", "time"});

        addParser(new PlayerSessionParser(), "target");
        addParser(new PastTimeParser(), "time");

        addValidator(new PlayerValidator());
        //addValidator(new SenderOutranksTargetValidator(), "target");
        addValidator(new PunishmentTimeValidator(), "time");
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args)
    {
        Session session = SessionHandler.getSession(sender);
        Session target = (Session) args[0];
        long time = (long) args[1];

        for (Punishment punishment : PunishmentHandler.getInstance().getAllPunishments(target.getUniqueId(), time))
        {
            punishment.setRepealed(true);
            PunishmentHandler.getInstance().save(punishment);
        }

        LANG.sendRenderMessage(session, "rollback.finished", "{player}", target.getName(), "{time}", DateUtils.formatDateDiff(time));
    }
}
