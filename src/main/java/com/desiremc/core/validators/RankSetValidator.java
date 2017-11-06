package com.desiremc.core.validators;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.command.CommandValidator;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;
import com.desiremc.core.session.SessionHandler;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

public class RankSetValidator extends CommandValidator
{
    @Override
    public boolean validateArgument(CommandSender sender, String label, Object arg)
    {
        Session player = SessionHandler.getSession(sender);
        Rank rank = (Rank) arg;

        if(player.getRank().getId() > rank.getId() || sender instanceof ConsoleCommandSender)
        {
            return true;
        }
        else
        {
            DesireCore.getLangHandler().sendRenderMessage(sender, "rank_too_high");
            return false;
        }
    }
}
