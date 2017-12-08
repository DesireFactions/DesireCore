package com.desiremc.core.commands.chat;

import java.util.List;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.ValidCommand;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;
import com.desiremc.core.staff.StaffHandler;

public class ChatToggleCommand extends ValidCommand
{

    public ChatToggleCommand()
    {
        super("toggle", "Toggle chat on or off.", Rank.SRMOD);
    }

    public void validRun(Session sender, String[] label, List<CommandArgument<?>> args)
    {
        if (StaffHandler.getInstance().chatDisabled())
        {
            DesireCore.getLangHandler().sendRenderMessage(sender, "staff.chat_on");
        }
        else
        {
            DesireCore.getLangHandler().sendRenderMessage(sender, "staff.chat_off");
        }
        StaffHandler.getInstance().toggleChat();
    }
}
