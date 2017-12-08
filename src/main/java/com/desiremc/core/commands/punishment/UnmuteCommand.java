package com.desiremc.core.commands.punishment;

import java.util.List;

import org.bukkit.Bukkit;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.CommandArgumentBuilder;
import com.desiremc.core.api.newcommands.ValidCommand;
import com.desiremc.core.newparsers.SessionParser;
import com.desiremc.core.newparsers.StringParser;
import com.desiremc.core.newvalidators.SessionMutedValidator;
import com.desiremc.core.punishment.Punishment;
import com.desiremc.core.punishment.PunishmentHandler;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;

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

        if (target.getOfflinePlayer() != null && target.getOfflinePlayer().isOnline())
        {
            DesireCore.getLangHandler().sendRenderMessage(target, "mute.unmute_target", "{player}", sender.getName());
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
            Bukkit.broadcastMessage(DesireCore.getLangHandler().renderMessage("mute.unmute_broadcast",
                    "{player}", sender.getName(),
                    "{target}", target.getName()));
        }
        else
        {
            DesireCore.getLangHandler().sendRenderMessage(sender, "mute.unmute",
                    "{target}", target.getName());
        }
    }

}
