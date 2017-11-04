package com.desiremc.core.commands.staff;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.desiremc.core.api.StaffAPI;
import com.desiremc.core.api.command.ValidCommand;
import com.desiremc.core.parsers.PlayerParser;
import com.desiremc.core.session.Rank;

public class StaffCPSCommand extends ValidCommand
{

    public StaffCPSCommand()
    {
        super("cps", "Starts clicks per second test on player", Rank.ADMIN, new String[] { "target" });
        addParser(new PlayerParser(), "target");
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args)
    {
        StaffAPI.clicksPerSecondTest(sender, (Player) args[0]);
    }

}