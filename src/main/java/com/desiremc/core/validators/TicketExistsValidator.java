package com.desiremc.core.validators;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.command.CommandValidator;
import com.desiremc.core.tickets.TicketHandler;
import org.bukkit.command.CommandSender;

public class TicketExistsValidator extends CommandValidator
{
    @Override
    public boolean validateArgument(CommandSender sender, String label, Object arg)
    {
        int id = (Integer) arg;

        if (TicketHandler.getTicket(id) != null)
        {
            return true;
        }
        else
        {
            DesireCore.getLangHandler().sendRenderMessage(sender, "ticket.doesnt-exist");
            return false;
        }
    }
}
