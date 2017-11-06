package com.desiremc.core.commands;

import com.desiremc.core.validators.PlayerIsOnlineValidator;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.command.ValidCommand;
import com.desiremc.core.parsers.PlayerSessionParser;
import com.desiremc.core.parsers.StringParser;
import com.desiremc.core.session.Rank;
import com.desiremc.core.validators.PlayerValidator;
import com.desiremc.core.validators.SenderOutranksTargetValidator;

public class KickCommand extends ValidCommand
{

    public KickCommand()
    {
        super("kick", "Kick a user from the server.", Rank.JRMOD, new String[]{"target", "reason"});
        addParser(new PlayerSessionParser(), "target");
        addParser(new StringParser(), "reason");
        addValidator(new PlayerValidator());
        addValidator(new SenderOutranksTargetValidator(), "target");
        addValidator(new PlayerIsOnlineValidator());
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args)
    {
        Player player = (Player) sender;
        Player target = (Player) args[0];

        StringBuilder sb = new StringBuilder();

        if (args.length >= 2)
        {
            for (int i = 2; i < args.length; i++)
            {
                sb.append(args[i] + " ");
            }
        }

        target.kickPlayer(DesireCore.getLangHandler().renderMessage("staff.kick-message", "{player}", player.getName
                (), "{reason}", sb.toString().trim()));
    }
}
