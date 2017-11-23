package com.desiremc.core.commands.punishment;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.command.ValidCommand;
import com.desiremc.core.parsers.PlayerParser;
import com.desiremc.core.parsers.StringParser;
import com.desiremc.core.session.Rank;
import com.desiremc.core.validators.SenderOutranksTargetValidator;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KickCommand extends ValidCommand
{

    public KickCommand()
    {
        super("kick", "Kick a user from the server.", Rank.JRMOD, ValidCommand.ARITY_REQUIRED_VARIADIC, new String[] {"target", "reason"});

        addParser(new PlayerParser(), "target");
        addParser(new StringParser(), "reason");

        addValidator(new SenderOutranksTargetValidator(), "target");
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args)
    {
        Player player = (Player) sender;
        Player target = (Player) args[0];

        if (((String) args[1]).contains("-s"))
        {
            args[1] = ((String) args[1]).replace("-s", "");
        }
        else
        {
            Bukkit.broadcastMessage(DesireCore.getLangHandler().renderMessage("kick.kick_message", "{target}", target.getName(), "{reason}", args[1], "{player}", sender.getName()));
        }

        target.kickPlayer(DesireCore.getLangHandler().renderMessage("kick.kick_message_target", "{player}", player.getName(), "{reason}", args[1]));
    }
}
