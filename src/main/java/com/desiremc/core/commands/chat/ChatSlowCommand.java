package com.desiremc.core.commands.chat;

import java.util.List;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.ValidCommand;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;
import com.desiremc.core.staff.StaffHandler;

public class ChatSlowCommand extends ValidCommand
{

    public ChatSlowCommand()
    {
        super("slow", "Toggle slow chat on or off.", Rank.SRMOD);
    }

    public void validRun(Session sender, String[] label, List<CommandArgument<?>> args)
    {
        if (StaffHandler.getInstance().isChatSlowed())
        {
            DesireCore.getLangHandler().sendRenderMessage(sender, "staff.chat_slow_off");
        }
        else
        {
            DesireCore.getLangHandler().sendRenderMessage(sender, "staff.chat_slow_on");
        }
        StaffHandler.getInstance().toggleChatSlowed();
    }
}
