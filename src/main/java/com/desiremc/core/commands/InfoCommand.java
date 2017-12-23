package com.desiremc.core.commands;

import java.util.List;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.LangHandler;
import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.CommandArgumentBuilder;
import com.desiremc.core.api.newcommands.ValidCommand;
import com.desiremc.core.parsers.SessionParser;
import com.desiremc.core.punishment.Punishment;
import com.desiremc.core.punishment.Punishment.Type;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;
import com.desiremc.core.utils.DateUtils;

/**
 * Command used to display the player's basic information values.
 * 
 * @author Michael Ziluck and Christian Tooley
 * @since 12/06/2017
 */
public class InfoCommand extends ValidCommand
{
    public InfoCommand()
    {
        super("info", "Get information about a player.", Rank.ADMIN);
        addArgument(CommandArgumentBuilder.createBuilder(Session.class).setName("target").setParser(new SessionParser()).build());
    }

    @Override
    public void validRun(Session sender, String label[], List<CommandArgument<?>> args)
    {
        Session target = (Session) args.get(0).getValue();
        DesireCore.getLangHandler().sendRenderMessageNoPrefix(sender, "info.header-footer");
        DesireCore.getLangHandler().sendRenderMessageCenteredNoPrefix(sender, "info.name", "{player}", target.getName());
        DesireCore.getLangHandler().sendRenderMessageCenteredNoPrefix(sender, "info.uuid", "{uuid}", target.getUniqueId().toString());
        DesireCore.getLangHandler().sendRenderMessageCenteredNoPrefix(sender, "info.ip", "{ip}", target.getIp());
        DesireCore.getLangHandler().sendRenderMessageCenteredNoPrefix(sender, "info.tokens", "{tokens}", target.getTokens() + "");
        DesireCore.getLangHandler().sendRenderMessageCenteredNoPrefix(sender, "info.status", "{status}", getStatus(target));
        DesireCore.getLangHandler().sendRenderMessageNoPrefix(sender, "info.header-footer");
    }

    /**
     * Gets the player's punishment status.
     * 
     * @param player the player to check.
     * @return the string representation of their punishment status.
     */
    private String getStatus(Session player)
    {
        LangHandler lang = DesireCore.getLangHandler();

        // check if they are ip banned
        Punishment punishment = player.isIPBanned();

        // check if they are banned normally
        if (punishment == null)
        {
            punishment = player.isBanned();
        }

        // check if they are muted
        if (punishment == null)
        {
            punishment = player.isMuted();
        }

        // if they this is still null, they have a normal status
        if (punishment == null)
        {
            return lang.renderMessageNoPrefix("info.normal");
        }

        // return ip ban message, it never has a time
        if (punishment.getType() == Type.IP_BAN)
        {
            return lang.renderMessageNoPrefix("info.ipbanned");
        }

        // return the ban message, varies by permanence
        if (punishment.getType() == Type.BAN)
        {
            if (punishment.isPermanent())
            {
                return lang.renderMessageNoPrefix("info.permbanned");
            }
            else
            {
                return lang.renderMessageNoPrefix("info.banned", "{time}", longToTime(punishment.getExpirationTime()));
            }
        }

        // return the mute message, varies by permanence
        if (punishment.getType() == Type.MUTE)
        {
            if (punishment.isPermanent())
            {
                return lang.renderMessageNoPrefix("info.permmuted");
            }
            else
            {
                return lang.renderMessageNoPrefix("info.muted", "{time}", longToTime(punishment.getExpirationTime()));
            }
        }

        // this shouldn't be possible as we terminated earlier
        return null;
    }

    private String longToTime(long time)
    {
        return DateUtils.formatDateDiff(time);
    }

}
