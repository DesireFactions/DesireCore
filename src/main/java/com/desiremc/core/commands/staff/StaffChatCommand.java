package com.desiremc.core.commands.staff;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.LangHandler;
import com.desiremc.core.api.command.ValidCommand;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.StaffHandler;
import com.desiremc.core.validators.PlayerValidator;

public class StaffChatCommand extends ValidCommand
{

    private static final LangHandler LANG = DesireCore.getLangHandler();

    public StaffChatCommand()
    {
        super("chat", "Join or leave Staff chat", Rank.JRMOD, new String[] {});
        addValidator(new PlayerValidator());
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args)
    {
        Player p = (Player) sender;

        if (StaffHandler.getInstance().inStaffChat(p))
        {
            LANG.sendRenderMessage(p, "staff.staff-chat-off");
        }
        else
        {
            LANG.sendRenderMessage(p, "staff.staff-chat-on");
        }
        StaffHandler.getInstance().toggleStaffChat(p);
    }
}
