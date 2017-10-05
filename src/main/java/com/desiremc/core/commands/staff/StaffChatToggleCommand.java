package com.desiremc.core.commands.staff;

import com.desiremc.core.api.command.ValidCommand;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.StaffHandler;
import org.bukkit.command.CommandSender;

public class StaffChatToggleCommand extends ValidCommand
{
    public StaffChatToggleCommand()
    {
        super("toggle", "Toggle chat on or off.", Rank.ADMIN, new String[]{});
    }

    public void validRun(CommandSender sender, String label, Object... args)
    {
        if (StaffHandler.getInstance().isChatEnabled())
        {
            LANG.sendRenderMessage(sender, "staff.chat-off");
        }
        else
        {
            LANG.sendRenderMessage(sender, "staff.chat-on");
        }
        StaffHandler.getInstance().toggleChat();
    }
}
