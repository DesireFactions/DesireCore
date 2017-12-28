package com.desiremc.core.commands.chat;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.ValidCommand;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;
import com.desiremc.core.session.SessionHandler;

import java.util.List;

public class ChatClearCommand extends ValidCommand
{

    public ChatClearCommand()
    {
        super("clear", "Clear all chat", Rank.MODERATOR);
    }

    public void validRun(Session sender, String[] label, List<CommandArgument<?>> args)
    {
        for (Session session : SessionHandler.getOnlineSessions())
        {
            if (!session.getRank().isStaff())
            {
                for (int i = 0; i < 120; i++)
                {
                    session.getSender().sendMessage("");
                }
            }
            DesireCore.getLangHandler().sendRenderMessage(session, "staff.chat_cleared_broadcast", true, false, "{player}", sender.getName());
        }
        DesireCore.getLangHandler().sendRenderMessage(sender, "staff.chat_cleared", true, false);
    }
}
