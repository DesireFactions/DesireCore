package com.desiremc.core.commands.staff;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.CommandArgumentBuilder;
import com.desiremc.core.api.newcommands.ValidCommand;
import com.desiremc.core.parsers.StringParser;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;
import com.desiremc.core.session.SessionHandler;
import com.desiremc.core.staff.StaffHandler;
import net.md_5.bungee.api.ChatColor;

import java.util.List;

public class StaffChatCommand extends ValidCommand
{

    public StaffChatCommand(String name, String... aliases)
    {
        super(name, "Join or leave staff chat.", Rank.HELPER, true, aliases);

        addArgument(CommandArgumentBuilder.createBuilder(String.class)
                .setName("message")
                .setParser(new StringParser())
                .setOptional()
                .setVariableLength()
                .build());

    }

    @Override
    public void validRun(Session sender, String label[], List<CommandArgument<?>> args)
    {

        if (!args.get(0).hasValue())
        {
            if (StaffHandler.getInstance().inStaffChat(sender.getUniqueId()))
            {
                DesireCore.getLangHandler().sendRenderMessage(sender, "staff.staff-chat-off", true, false);
            }
            else
            {
                DesireCore.getLangHandler().sendRenderMessage(sender, "staff.staff-chat-on", true, false);
            }
            StaffHandler.getInstance().toggleStaffChat(sender.getUniqueId());
            return;
        }

        String message = (String) args.get(0).getValue();

        String parsed = DesireCore.getLangHandler().renderMessage("staff.staff-chat-format", false, false,
                "{name}", sender.getName(),
                "{message}", ChatColor.translateAlternateColorCodes('&', message));

        for (Session target : SessionHandler.getOnlineStaff())
        {
            target.sendMessage(parsed);
        }

    }
}
