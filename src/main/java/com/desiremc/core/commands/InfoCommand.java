package com.desiremc.core.commands;

import java.util.List;

import org.bukkit.command.CommandSender;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.CommandArgumentBuilder;
import com.desiremc.core.api.newcommands.ValidCommand;
import com.desiremc.core.newparsers.SessionParser;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;
import com.desiremc.core.utils.DateUtils;

public class InfoCommand extends ValidCommand
{
    public InfoCommand()
    {
        super("info", "Get information about a player.", Rank.ADMIN);
        addArgument(CommandArgumentBuilder.createBuilder(Session.class).setName("target").setParser(new SessionParser()).build());
    }

    @Override
    public void validRun(CommandSender sender, String label[], List<CommandArgument<?>> args)
    {
        Session target = (Session) args.get(0).getValue();
        DesireCore.getLangHandler().sendRenderMessageNoPrefix(sender, "info.header-footer");
        DesireCore.getLangHandler().sendRenderMessageCenteredeNoPrefix(sender, "info.name", "{player}", target.getName());
        DesireCore.getLangHandler().sendRenderMessageCenteredeNoPrefix(sender, "info.uuid", "{uuid}", target.getUniqueId().toString());
        DesireCore.getLangHandler().sendRenderMessageCenteredeNoPrefix(sender, "info.ip", "{ip}", target.getIp());
        DesireCore.getLangHandler().sendRenderMessageCenteredeNoPrefix(sender, "info.tokens", "{tokens}", target.getTokens() + "");
        DesireCore.getLangHandler().sendRenderMessageCenteredeNoPrefix(sender, "info.status", "{status}", getStatus(target));
        DesireCore.getLangHandler().sendRenderMessageNoPrefix(sender, "info.header-footer");
    }

    private String getStatus(Session player)
    {
        if (player.isBanned() != null)
        {
            return DesireCore.getLangHandler().renderMessageNoPrefix("info.banned", "{time}", longToTime(player.isBanned().getExpirationTime()));
        }
        if (player.isMuted() != null)
        {
            return DesireCore.getLangHandler().renderMessageNoPrefix("info.muted", "{time}", longToTime(player.isBanned().getExpirationTime()));
        }
        return DesireCore.getLangHandler().renderMessageNoPrefix("info.normal");
    }

    private String longToTime(long time)
    {
        return DateUtils.formatDateDiff(time);
    }

}
