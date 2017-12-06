package com.desiremc.core.commands.ticket;

import java.util.List;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.ValidCommand;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;
import com.desiremc.core.tickets.Ticket;
import com.desiremc.core.tickets.TicketHandler;
import com.desiremc.core.utils.PlayerUtils;

public class TicketListCommand extends ValidCommand
{

    public TicketListCommand()
    {
        super("list", "List all open tickets.", Rank.MODERATOR);
    }

    @Override
    public void validRun(Session sender, String[] label, List<CommandArgument<?>> args)
    {
        for (Ticket ticket : TicketHandler.getAllTickets())
        {
            DesireCore.getLangHandler().sendRenderMessage(sender, "ticket.list",
                    "{id}", ticket.getId(),
                    "{player}", PlayerUtils.getName(ticket.getPlayer()),
                    "{reason}", ticket.getText());

        }
    }
}
