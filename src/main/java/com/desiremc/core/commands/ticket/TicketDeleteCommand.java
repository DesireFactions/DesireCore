package com.desiremc.core.commands.ticket;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.CommandArgumentBuilder;
import com.desiremc.core.api.newcommands.ValidCommand;
import com.desiremc.core.parsers.StringParser;
import com.desiremc.core.parsers.TicketParser;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;
import com.desiremc.core.tickets.Ticket;
import com.desiremc.core.tickets.TicketHandler;

import java.util.List;

public class TicketDeleteCommand extends ValidCommand
{

    public TicketDeleteCommand()
    {
        super("close", "Delete a ticket with a comment.", Rank.MODERATOR);

        addArgument(CommandArgumentBuilder.createBuilder(Ticket.class)
                .setName("id")
                .setParser(new TicketParser()).build());

        addArgument(CommandArgumentBuilder.createBuilder(String.class)
                .setName("response")
                .setParser(new StringParser()).build());
    }

    @Override
    public void validRun(Session sender, String[] label, List<CommandArgument<?>> args)
    {
        TicketHandler.deleteTicket(sender, (Ticket) args.get(0).getValue(), (String) args.get(1).getValue());

        DesireCore.getLangHandler().sendRenderMessage(sender, "ticket.delete", true, false,
                "{id}", ((Ticket) args.get(0).getValue()).getId());
    }
}
