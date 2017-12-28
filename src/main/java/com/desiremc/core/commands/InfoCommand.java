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
        DesireCore.getLangHandler().sendRenderMessage(sender, "info.header-footer", false, false);
        DesireCore.getLangHandler().sendRenderMessage(sender, "info.name", false, true, "{player}", target.getName());
        DesireCore.getLangHandler().sendRenderMessage(sender, "info.uuid", false, true, "{uuid}", target.getUniqueId().toString());
        DesireCore.getLangHandler().sendRenderMessage(sender, "info.ip", false, true, "{ip}", target.getIp());
        DesireCore.getLangHandler().sendRenderMessage(sender, "info.tokens", false, true, "{tokens}", target.getTokens() + "");
        DesireCore.getLangHandler().sendRenderMessage(sender, "info.status", false, true, "{status}", getStatus(target));
        DesireCore.getLangHandler().sendRenderMessage(sender, "info.header-footer", false, false);
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
            return lang.renderMessage("info.normal", false, false);
        }

        // return ip ban message, it never has a time
        if (punishment.getType() == Type.IP_BAN)
        {
            return lang.renderMessage("info.ipbanned", false, false);
        }

        // return the ban message, varies by permanence
        if (punishment.getType() == Type.BAN)
        {
            if (punishment.isPermanent())
            {
                return lang.renderMessage("info.permbanned", false, false);
            }
            else
            {
                return lang.renderMessage("info.banned", false, false, "{time}", longToTime(punishment.getExpirationTime()));
            }
        }

        // return the mute message, varies by permanence
        if (punishment.getType() == Type.MUTE)
        {
            if (punishment.isPermanent())
            {
                return lang.renderMessage("info.permmuted", false, false);
            }
            else
            {
                return lang.renderMessage("info.muted", false, false, "{time}", longToTime(punishment.getExpirationTime()));
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
