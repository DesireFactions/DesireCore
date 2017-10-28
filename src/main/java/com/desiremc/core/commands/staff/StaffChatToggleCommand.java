package com.desiremc.core.commands.staff;

import org.bukkit.command.CommandSender;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.LangHandler;
import com.desiremc.core.api.command.ValidCommand;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.StaffHandler;

public class StaffChatToggleCommand extends ValidCommand
{

    private static final LangHandler LANG = DesireCore.getLangHandler();

    public StaffChatToggleCommand()
    {
        super("toggle", "Toggle chat on or off.", Rank.ADMIN, new String[] {});
    }

    public void validRun(CommandSender sender, String label, Object... args)
    {
        if (StaffHandler.getInstance().chatDisabled())
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
