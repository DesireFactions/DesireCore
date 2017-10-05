package com.desiremc.core.commands.staff;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.desiremc.core.api.StaffAPI;
import com.desiremc.core.api.command.ValidCommand;
import com.desiremc.core.parsers.PlayerParser;
import com.desiremc.core.session.Rank;
import com.desiremc.core.validators.PlayerValidator;

public class StaffMountCommand extends ValidCommand
{

    public StaffMountCommand()
    {
        super("follow", "follow a player", Rank.ADMIN, new String[]{"target"}, "mount", "ride", "leash", "lead");
        addParser(new PlayerParser(), "target");
        addValidator(new PlayerValidator());
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args)
    {
        StaffAPI.mount((Player) sender, (Player) args[0]);
    }

}
