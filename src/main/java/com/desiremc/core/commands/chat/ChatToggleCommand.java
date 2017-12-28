package com.desiremc.core.commands.chat;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.ValidCommand;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;
import com.desiremc.core.staff.StaffHandler;
import org.bukkit.Bukkit;

import java.util.List;

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
            DesireCore.getLangHandler().sendRenderMessage(sender, "staff.chat_on", true, false);
            Bukkit.broadcastMessage(DesireCore.getLangHandler().renderMessage("staff.chat_on_broadcast", true, false, "{player}", sender.getName()));
        }
        else
        {
            DesireCore.getLangHandler().sendRenderMessage(sender, "staff.chat_off", true, false);
            Bukkit.broadcastMessage(DesireCore.getLangHandler().renderMessage("staff.chat_off_broadcast", true, false, "{player}", sender.getName()));
        }
        StaffHandler.getInstance().toggleChat();
    }
}
