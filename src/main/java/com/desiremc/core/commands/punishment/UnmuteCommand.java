package com.desiremc.core.commands.punishment;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.CommandArgumentBuilder;
import com.desiremc.core.api.newcommands.ValidCommand;
import com.desiremc.core.parsers.SessionParser;
import com.desiremc.core.parsers.StringParser;
import com.desiremc.core.punishment.Punishment;
import com.desiremc.core.punishment.PunishmentHandler;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;
import com.desiremc.core.validators.punishments.SessionMutedValidator;
import org.bukkit.Bukkit;

import java.util.List;

public class UnmuteCommand extends ValidCommand
{

    public UnmuteCommand()
    {
        super("unmute", "Unmute a user on the server.", Rank.MODERATOR);

        addArgument(CommandArgumentBuilder.createBuilder(Session.class)
                .setName("target")
                .setParser(new SessionParser())
                .addValidator(new SessionMutedValidator())
                .build());

        addArgument(CommandArgumentBuilder.createBuilder(String.class)
                .setName("flag")
                .setParser(new StringParser())
                .setOptional()
                .build());
    }

    @Override
    public void validRun(Session sender, String[] label, List<CommandArgument<?>> args)
    {
        Session target = (Session) args.get(0).getValue();

        Punishment p = target.isMuted();
        p.setRepealed(true);
        PunishmentHandler.getInstance().save(p);
        PunishmentHandler.getInstance().refreshPunishments(target);

        if (target.isOnline())
        {
            DesireCore.getLangHandler().sendRenderMessage(target, "mute.unmute_target", true, false, "{player}", sender.getName());
        }

        boolean broadcast = true;
        if (args.get(1).hasValue())
        {
            String flag = ((String) args.get(0).getValue()).toLowerCase();
            if (flag.contains("-s"))
            {
                broadcast = false;
            }
        }

        if (broadcast)
        {
            Bukkit.broadcastMessage(DesireCore.getLangHandler().renderMessage("mute.unmute_broadcast", true, false,
                    "{player}", sender.getName(),
                    "{target}", target.getName()));
        }
        else
        {
            DesireCore.getLangHandler().sendRenderMessage(sender, "mute.unmute", true, false,
                    "{target}", target.getName());
        }
    }

}
