package com.desiremc.core.commands.ticket;

import org.bukkit.command.CommandSender;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.command.ValidCommand;
import com.desiremc.core.session.Rank;
import com.desiremc.core.tickets.Ticket;
import com.desiremc.core.tickets.TicketHandler;
import com.desiremc.core.utils.PlayerUtils;

public class TicketListCommand extends ValidCommand
{

    public TicketListCommand()
    {
        super("list", "List all open tickets.", Rank.MODERATOR, new String[] {});
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args)
    {
        int num = 1;
        for (Ticket ticket : TicketHandler.getAllTickets())
        {
            DesireCore.getLangHandler().sendRenderMessage(sender, "ticket.list", "{num}", num + "", "{player}", PlayerUtils.getPlayer(ticket.getPlayer()).getName(), "{reason}", ticket.getText(), "{id}", ticket.getId() + "");
            num++;
        }
    }
}
