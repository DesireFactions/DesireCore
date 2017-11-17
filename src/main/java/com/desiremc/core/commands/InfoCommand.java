package com.desiremc.core.commands;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.LangHandler;
import com.desiremc.core.api.command.ValidCommand;
import com.desiremc.core.parsers.PlayerSessionParser;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;
import com.desiremc.core.utils.DateUtils;
import com.desiremc.core.validators.PlayerValidator;
import org.bukkit.command.CommandSender;

/**
 * @author Christian Tooley
 */

public class InfoCommand extends ValidCommand
{

    private static final LangHandler LANG = DesireCore.getLangHandler();

    public InfoCommand()
    {
        super("info", "Get information about a player.", Rank.ADMIN, new String[] {"target"});
        addParser(new PlayerSessionParser(), "target");
        addValidator(new PlayerValidator());
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args)
    {
        Session target = (Session) args[0];
        LANG.sendRenderMessageNoPrefix(sender, "info.header-footer");
        LANG.sendRenderMessageCenteredeNoPrefix(sender, "info.name", "{player}", target.getName());
        LANG.sendRenderMessageCenteredeNoPrefix(sender, "info.uuid", "{uuid}", target.getUniqueId().toString());
        LANG.sendRenderMessageCenteredeNoPrefix(sender, "info.ip", "{ip}", target.getIp());
        LANG.sendRenderMessageCenteredeNoPrefix(sender, "info.tokens", "{tokens}", target.getTokens() + "");
        LANG.sendRenderMessageCenteredeNoPrefix(sender, "info.status", "{status}", getStatus(target));
        LANG.sendRenderMessageNoPrefix(sender, "info.header-footer");
    }

    private String getStatus(Session player)
    {
        if (player.isBanned() != null)
        {
            return LANG.renderMessageNoPrefix("info.banned", "{time}", longToTime(player.isBanned().getExpirationTime()));
        }
        if (player.isMuted() != null)
        {
            return LANG.renderMessageNoPrefix("info.muted", "{time}", longToTime(player.isBanned().getExpirationTime()));
        }
        return LANG.renderMessageNoPrefix("info.normal");
    }

    private String longToTime(long time)
    {
        return DateUtils.formatDateDiff(time);
    }
}
