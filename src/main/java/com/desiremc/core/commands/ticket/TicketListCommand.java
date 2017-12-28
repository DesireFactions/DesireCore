package com.desiremc.core.commands.ticket;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.ValidCommand;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;
import com.desiremc.core.tickets.Ticket;
import com.desiremc.core.tickets.TicketHandler;
import com.desiremc.core.utils.PlayerUtils;

import java.util.List;

public class TicketListCommand extends ValidCommand
{

    public TicketListCommand()
    {
        super("list", "List all open tickets.", Rank.MODERATOR);
    }

    @Override
    public void validRun(Session sender, String[] label, List<CommandArgument<?>> args)
    {
        DesireCore.getLangHandler().sendRenderMessage(sender, "ticket.header_footer", false, false);
        for (Ticket ticket : TicketHandler.getAllTickets())
        {
            DesireCore.getLangHandler().sendRenderMessage(sender, "ticket.list", false, false,
                    "{id}", ticket.getId(),
                    "{player}", PlayerUtils.getName(ticket.getPlayer()),
                    "{reason}", ticket.getText());

        }
        DesireCore.getLangHandler().sendRenderMessage(sender, "ticket.header_footer", false, false);
    }
}
