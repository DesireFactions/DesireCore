package com.desiremc.core.commands.chat;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.LangHandler;
import com.desiremc.core.api.command.ValidCommand;
import com.desiremc.core.session.Rank;
import com.desiremc.core.staff.StaffHandler;
import org.bukkit.command.CommandSender;

public class ChatSlowCommand extends ValidCommand
{

    private static final LangHandler LANG = DesireCore.getLangHandler();

    public ChatSlowCommand()
    {
        super("slow", "Toggle slow chat on or off.", Rank.SRMOD, new String[] {});
    }

    public void validRun(CommandSender sender, String label, Object... args)
    {
        if (StaffHandler.getInstance().isChatSlowed())
        {
            LANG.sendRenderMessage(sender, "staff.chat-slow-off");
        }
        else
        {
            LANG.sendRenderMessage(sender, "staff.chat-slow-on");
        }
        StaffHandler.getInstance().toggleChatSlowed();
    }
}
