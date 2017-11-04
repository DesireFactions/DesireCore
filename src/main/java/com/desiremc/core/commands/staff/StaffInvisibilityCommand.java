package com.desiremc.core.commands.staff;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.desiremc.core.api.StaffAPI;
import com.desiremc.core.api.command.ValidCommand;
import com.desiremc.core.parsers.PlayerParser;
import com.desiremc.core.session.Rank;

public class StaffInvisibilityCommand extends ValidCommand
{

    public StaffInvisibilityCommand()
    {
        super("invisibility", "Toggle invisibility for player.", Rank.ADMIN, ARITY_OPTIONAL, new String[] { "target" }, "invisible", "invis");

        addParser(new PlayerParser(), "target");
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args)
    {
        Player p = null;
        if (args.length == 1)
        {
            p = (Player) args[0];
        }
        StaffAPI.toggleInvisibility(p);
    }

}
