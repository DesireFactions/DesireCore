package com.desiremc.core.commands.staff;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.LangHandler;
import com.desiremc.core.api.command.ValidCommand;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;
import com.desiremc.core.session.SessionHandler;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StaffChatClearCommand extends ValidCommand
{

    private static final LangHandler LANG = DesireCore.getLangHandler();

    public StaffChatClearCommand()
    {
        super("clear", "Clear all chat", Rank.MODERATOR, new String[] {});
    }

    public void validRun(CommandSender sender, String label, Object... args)
    {
        for (Player p : Bukkit.getOnlinePlayers())
        {
            Session session = SessionHandler.getSession(p.getUniqueId());

            if (session.getRank().isStaff())
            {
                LANG.sendRenderMessage(session, "staff.chat-cleared-all", "{player}", sender.getName());
                continue;
            }

            for (int i = 0; i < 50; i++)
            {
                p.sendMessage("");
            }
            LANG.sendRenderMessage(session, "staff.chat-cleared-all", "{player}", sender.getName());
        }
        LANG.sendRenderMessage(sender, "staff.chat-cleared");
    }
}
