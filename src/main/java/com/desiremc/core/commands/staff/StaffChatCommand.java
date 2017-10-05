package com.desiremc.core.commands.staff;

import com.desiremc.core.api.command.ValidCommand;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.StaffHandler;
import com.desiremc.core.validators.PlayerValidator;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StaffChatCommand extends ValidCommand
{

    public StaffChatCommand()
    {
        super("chat", "Join or leave Staff chat", Rank.HELPER, new String[]{});
        addValidator(new PlayerValidator());
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args)
    {
        Player p = (Player) sender;

        if(StaffHandler.getInstance().inStaffChat(p))
        {
            LANG.sendRenderMessage(p, "staff-chat-off");
        }
        else{
            LANG.sendRenderMessage(p, "staff-chat-on");
        }
        StaffHandler.getInstance().toggleStaffChat(p);
    }
}
