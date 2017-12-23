package com.desiremc.core.commands.ticket;

import java.util.List;

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

public class TicketCloseCommand extends ValidCommand
{

    public TicketCloseCommand()
    {
        super("close", "Close a ticket with a comment.", Rank.MODERATOR);

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
        TicketHandler.closeTicket(sender, (Ticket) args.get(0).getValue(), (String) args.get(1).getValue());

        DesireCore.getLangHandler().sendRenderMessage(sender, "ticket.close",
                "{id}", ((Ticket) args.get(0).getValue()).getId());
    }

}
