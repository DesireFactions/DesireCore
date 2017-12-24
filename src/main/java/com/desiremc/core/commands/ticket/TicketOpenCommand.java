package com.desiremc.core.commands.ticket;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.CommandArgumentBuilder;
import com.desiremc.core.api.newcommands.ValidCommand;
import com.desiremc.core.parsers.StringParser;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;
import com.desiremc.core.tickets.TicketHandler;

import java.util.List;

public class TicketOpenCommand extends ValidCommand
{

    public TicketOpenCommand()
    {
        super("open", "Opens a new ticket.", Rank.GUEST, new String[] { "create", "new" });

        addArgument(CommandArgumentBuilder.createBuilder(String.class).setName("description").setParser(new StringParser()).build());
    }

    @Override
    public void validRun(Session sender, String[] label, List<CommandArgument<?>> args)
    {
        String text = (String) args.get(0).getValue();

        TicketHandler.openTicket(sender, text);
        DesireCore.getLangHandler().sendRenderMessage(sender, "ticket.open", true, false);
    }

}
