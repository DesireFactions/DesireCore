package com.desiremc.core.validators;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.command.CommandValidator;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;
import com.desiremc.core.session.SessionHandler;
import com.desiremc.core.utils.SessionUtils;
import org.bukkit.command.CommandSender;

public class SenderOutranksTargetValidator extends CommandValidator
{

    @Override
    public boolean validateArgument(CommandSender sender, String label, Object arg)
    {
        Rank senderRank = SessionHandler.getSession(sender).getRank();
        Rank targetRank = SessionUtils.getRank(arg);

        Session session = (Session) arg;

        if (sender.getName().equalsIgnoreCase(session.getName()))
        {
            DesireCore.getLangHandler().sendRenderMessage(sender, "cant_to_self");
            return false;
        }

        if (senderRank.compareTo(targetRank) > 0)
        {
            return true;
        }
        else
        {
            DesireCore.getLangHandler().sendRenderMessage(sender, "sender_doesnt_outrank");
            return false;
        }
    }

}
