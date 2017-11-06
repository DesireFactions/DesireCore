package com.desiremc.core.validators;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.command.CommandValidator;
import com.desiremc.core.session.Session;
import com.desiremc.core.session.SessionHandler;

public class NotIgnoringValidator extends CommandValidator
{

    @Override
    public boolean validateArgument(CommandSender sender, String label, Object arg)
    {
        Player p = (Player) arg;

        Session senderSession = SessionHandler.getSession(sender);
        Session receiverSession = SessionHandler.getSession(p);

        if (senderSession.isIgnoring(receiverSession.getUniqueId()))
        {
            DesireCore.getLangHandler().sendRenderMessage(sender, "chat.ignoring", "{target}", receiverSession.getName());
            return false;
        }
        else if (receiverSession.isIgnoring(senderSession.getUniqueId()))
        {
            DesireCore.getLangHandler().sendRenderMessage(sender, "chat.ignored", "{target}", receiverSession.getName());
            return false;
        }
        else
        {
            return true;
        }
    }

}
