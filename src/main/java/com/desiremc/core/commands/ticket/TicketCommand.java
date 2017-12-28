package com.desiremc.core.commands.ticket;

import com.desiremc.core.api.newcommands.ValidBaseCommand;
import com.desiremc.core.session.Rank;

public class TicketCommand extends ValidBaseCommand
{

    public TicketCommand()
    {
        super("ticket", "Manages the ticket system.", Rank.GUEST, new String[] { "tickets", "tick", "pe", "petition" });
        addSubCommand(new TicketOpenCommand());
        addSubCommand(new TicketCloseCommand());
        addSubCommand(new TicketListCommand());
        addSubCommand(new TicketDeleteCommand());
    }

}
