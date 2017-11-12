package com.desiremc.core.commands.staff;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.LangHandler;
import com.desiremc.core.api.command.ValidCommand;
import com.desiremc.core.parsers.StringParser;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;
import com.desiremc.core.session.SessionHandler;
import com.desiremc.core.staff.StaffHandler;
import com.desiremc.core.validators.PlayerValidator;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StaffChatCommand extends ValidCommand
{

    private static final LangHandler LANG = DesireCore.getLangHandler();

    public StaffChatCommand(String name, String... aliases)
    {
        super(name, "Join or leave staff chat.", Rank.JRMOD, ARITY_REQUIRED_VARIADIC, new String[] {"message"},
                aliases);
        addValidator(new PlayerValidator());

        addParser(new StringParser(), "message");
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args)
    {
        Player p = (Player) sender;

        if (args.length == 0)
        {
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
        else
        {
            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < args.length; i++)
            {
                sb.append(args[i] + " ");
            }

            String message = sb.toString().trim();
            String parsed = DesireCore.getLangHandler().renderMessageNoPrefix("staff.staff-chat-format", "{name}", p
                    .getName(), "{message}", ChatColor.translateAlternateColorCodes('&', message));
            for (Session target : SessionHandler.getInstance().getStaff())
            {
                target.getPlayer().sendMessage(parsed);
            }
        }
    }
}
