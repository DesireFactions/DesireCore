package com.desiremc.core.validators;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.desiremc.core.api.command.CommandValidator;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;
import com.desiremc.core.session.SessionHandler;
import com.desiremc.core.utils.SessionUtils;

public class SenderOutranksTargetValidator extends CommandValidator
{

    @Override
    public boolean validateArgument(CommandSender sender, String label, Object arg)
    {
        Rank senderRank = SessionUtils.getRank(SessionHandler.getSession((Player) sender));
        Rank targetRank = SessionUtils.getRank((Session) arg);

        return senderRank.compareTo(targetRank) > 0;
    }

}
