package com.desiremc.core.parsers;

import java.util.Arrays;
import java.util.List;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.newcommands.Parser;
import com.desiremc.core.session.Session;
import com.desiremc.core.tickets.Ticket;
import com.desiremc.core.tickets.TicketHandler;

public class TicketParser implements Parser<Ticket>
{

    @Override
    public Ticket parseArgument(Session sender, String[] label, String argument)
    {
        int num;
        try
        {
            num = Integer.parseInt(argument);
        }
        catch (NumberFormatException ex)
        {
            DesireCore.getLangHandler().sendRenderMessage(sender, "ticket.only_id");
            return null;
        }

        Ticket ticket = TicketHandler.getTicket(num);

        if (ticket == null)
        {
            DesireCore.getLangHandler().sendRenderMessage(sender, "ticket.not_found",
                    "{id}", num);
            return null;
        }

        return ticket;
    }

    @Override
    public List<String> getRecommendations(Session sender, String lastWord)
    {
        return Arrays.asList();
    }

}
