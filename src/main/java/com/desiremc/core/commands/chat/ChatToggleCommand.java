package com.desiremc.core.commands.chat;

import org.bukkit.command.CommandSender;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.LangHandler;
import com.desiremc.core.api.command.ValidCommand;
import com.desiremc.core.session.Rank;
import com.desiremc.core.staff.StaffHandler;

public class ChatToggleCommand extends ValidCommand
{

    private static final LangHandler LANG = DesireCore.getLangHandler();

    public ChatToggleCommand()
    {
        super("toggle", "Toggle chat on or off.", Rank.ADMIN, new String[] {});
    }

    public void validRun(CommandSender sender, String label, Object... args)
    {
        if (StaffHandler.getInstance().chatDisabled())
        {
            LANG.sendRenderMessage(sender, "staff.chat-on");
        }
        else
        {
            LANG.sendRenderMessage(sender, "staff.chat-off");
        }
        StaffHandler.getInstance().toggleChat();
    }
}
