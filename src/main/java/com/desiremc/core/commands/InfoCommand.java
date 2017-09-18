package com.desiremc.core.commands;

import org.bukkit.command.CommandSender;

import com.desiremc.core.api.command.ValidCommand;
import com.desiremc.core.parsers.PlayerSessionParser;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;
import com.desiremc.core.utils.DateUtils;
import com.desiremc.core.validators.PlayerValidator;

/**
 * @author Christian Tooley
 */

public class InfoCommand extends ValidCommand
{
    public InfoCommand()
    {
        super("info", "Get information about a player.", Rank.MODERATOR, new String[] { "target" });
        addParser(new PlayerSessionParser(), "target");
        addValidator(new PlayerValidator());
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args)
    {
        Session target = (Session) args[0];
        LANG.sendRenderMessage(target, "info.header-footer");
        LANG.sendRenderMessage(target, "info.name", true, "{name}", target.getName());
        LANG.sendRenderMessage(target, "info.uuid", true, "{uuid}", target.getUniqueId().toString());
        LANG.sendRenderMessage(target, "info.ip", true, "{ip}", target.getPlayer().getAddress().getAddress().toString());
        LANG.sendRenderMessage(target, "info.tokens", true, "{tokens}", target.getTokens() + "");
        LANG.sendRenderMessage(target, "info.status", true, "{status}", getStatus(target));
        LANG.sendRenderMessage(target, "info.header-footer");
    }

    private String getStatus(Session player)
    {
        if (player.isBanned() != null) { return LANG.renderMessage("info.banned", "{time}", longToTime(player.isBanned().getExpirationTime())); }
        if (player.isMuted() != null) { return LANG.renderMessage("info.muted", "{time}", longToTime(player.isBanned().getExpirationTime())); }
        return LANG.renderMessage("info.normal");
    }

    private String longToTime(long time)
    {
        return DateUtils.formatDateDiff(time);
    }
}
