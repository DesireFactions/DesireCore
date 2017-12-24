package com.desiremc.core.commands.chat;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.ValidCommand;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;
import com.desiremc.core.staff.StaffHandler;

import java.util.List;

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
            DesireCore.getLangHandler().sendRenderMessage(sender, "staff.chat_slow_off", true, false);
        }
        else
        {
            DesireCore.getLangHandler().sendRenderMessage(sender, "staff.chat_slow_on", true, false);
        }
        StaffHandler.getInstance().toggleChatSlowed();
    }
}
