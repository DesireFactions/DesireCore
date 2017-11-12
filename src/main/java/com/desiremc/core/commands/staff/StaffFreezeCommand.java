package com.desiremc.core.commands.staff;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.desiremc.core.api.StaffAPI;
import com.desiremc.core.api.command.ValidCommand;
import com.desiremc.core.parsers.PlayerParser;
import com.desiremc.core.session.Rank;
import com.desiremc.core.validators.SenderOutranksTargetValidator;

public class StaffFreezeCommand extends ValidCommand
{

    public StaffFreezeCommand()
    {
        super("freeze", "Freeze a target player", Rank.JRMOD, new String[] { "target" }, new String[] { "ss" });

        addParser(new PlayerParser(), "target");

        addValidator(new SenderOutranksTargetValidator(), "target");
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args)
    {
        StaffAPI.freeze(sender, (Player) args[0]);
    }

}
