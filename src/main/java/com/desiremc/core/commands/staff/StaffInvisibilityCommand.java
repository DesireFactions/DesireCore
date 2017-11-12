package com.desiremc.core.commands.staff;

import com.desiremc.core.api.StaffAPI;
import com.desiremc.core.api.command.ValidCommand;
import com.desiremc.core.parsers.PlayerParser;
import com.desiremc.core.session.Rank;
import com.desiremc.core.validators.PlayerValidator;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StaffInvisibilityCommand extends ValidCommand
{

    public StaffInvisibilityCommand()
    {
        super("invisibility", "Toggle invisibility for player.", Rank.JRMOD, ARITY_STRICT, new String[]
                {"target"}, "invisible", "invis");

        addValidator(new PlayerValidator());

        addParser(new PlayerParser(), "target");
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args)
    {
        Player p = (Player) sender;

        if (args.length == 1)
        {
            p = (Player) args[0];
        }
        StaffAPI.toggleInvisibility(p, false);
    }

}
