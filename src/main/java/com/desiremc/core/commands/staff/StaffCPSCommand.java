package com.desiremc.core.commands.staff;

import com.desiremc.core.api.StaffAPI;
import com.desiremc.core.api.command.ValidCommand;
import com.desiremc.core.parsers.PlayerParser;
import com.desiremc.core.session.Rank;
import com.desiremc.core.validators.PlayerSessionIsOnlineValidator;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StaffCPSCommand extends ValidCommand
{

    public StaffCPSCommand()
    {
        super("cps", "Starts clicks per second test on player", Rank.JRMOD, new String[] { "target" });
        addParser(new PlayerParser(), "target");
        addValidator(new PlayerSessionIsOnlineValidator());
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args)
    {
        StaffAPI.clicksPerSecondTest(sender, (Player) args[0]);
    }

}