package com.desiremc.core.commands.punishment;

import java.util.List;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.CommandArgumentBuilder;
import com.desiremc.core.api.newcommands.ValidCommand;
import com.desiremc.core.parsers.SessionParser;
import com.desiremc.core.parsers.TimeParser;
import com.desiremc.core.punishment.Punishment;
import com.desiremc.core.punishment.PunishmentHandler;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;
import com.desiremc.core.utils.DateUtils;
import com.desiremc.core.validators.NumberSizeValidator;
import com.desiremc.core.validators.SenderNotTargetValidator;
import com.desiremc.core.validators.SenderOutranksTargetValidator;

public class RollbackCommand extends ValidCommand
{

    public RollbackCommand()
    {
        super("rollback", "Rollback punishments from a user in a time period.", Rank.HELPER);

        addArgument(CommandArgumentBuilder.createBuilder(Session.class)
                .setName("target")
                .setParser(new SessionParser())
                .addValidator(new SenderNotTargetValidator())
                .addValidator(new SenderOutranksTargetValidator())
                .build());

        addArgument(CommandArgumentBuilder.createBuilder(Long.class)
                .setName("time")
                .setParser(new TimeParser())
                .addValidator(new NumberSizeValidator<Long>(0l, 1209600000l, "punishment.too_low", "punishment.too_high"))
                .build());
    }

    @Override
    public void validRun(Session sender, String[] label, List<CommandArgument<?>> args)
    {
        Session target = (Session) args.get(0).getValue();
        long time = (Long) args.get(1).getValue();

        for (Punishment punishment : PunishmentHandler.getInstance().getAllPunishments(target.getUniqueId(), time))
        {
            punishment.setRepealed(true);
            PunishmentHandler.getInstance().save(punishment);
        }

        DesireCore.getLangHandler().sendRenderMessage(sender, "rollback.finished", true, false,
                "{player}", target.getName(),
                "{time}", DateUtils.formatDateDiff(time));
    }
}
