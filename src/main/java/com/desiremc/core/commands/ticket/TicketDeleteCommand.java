package com.desiremc.core.commands.ticket;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.command.ValidCommand;
import com.desiremc.core.parsers.IntegerParser;
import com.desiremc.core.parsers.StringParser;
import com.desiremc.core.session.Rank;
import com.desiremc.core.tickets.TicketHandler;
import com.desiremc.core.validators.TicketExistsValidator;
import org.bukkit.command.CommandSender;

public class TicketDeleteCommand extends ValidCommand
{

    public TicketDeleteCommand()
    {
        super("delete", "Delete a ticket with a comment.", Rank.MODERATOR, new String[]{"ticket", "response"}, new String[]{});
        addValidator(new TicketExistsValidator(), "ticket");
        addParser(new IntegerParser(), "ticket");
        addParser(new StringParser(), "response");
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args)
    {
        StringBuilder sb = new StringBuilder();

        if (args.length >= 2)
        {
            for (int i = 1; i < args.length; i++)
            {
                sb.append(args[i] + " ");
            }
        }

        TicketHandler.deleteTicket(sender, TicketHandler.getTicket((int) args[0]), sb.toString().trim());

        DesireCore.getLangHandler().sendRenderMessage(sender, "ticket.delete", "{id}", args[0] + "");
    }
}
