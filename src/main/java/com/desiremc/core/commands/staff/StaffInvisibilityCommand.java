package com.desiremc.core.commands.staff;

import com.desiremc.core.api.StaffAPI;
import com.desiremc.core.api.command.ValidCommand;
import com.desiremc.core.session.Rank;
import com.desiremc.core.validators.PlayerValidator;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StaffInvisibilityCommand extends ValidCommand
{

    public StaffInvisibilityCommand()
    {
        super("invisibility", "Toggle invisibility.", Rank.JRMOD, new String[] {}, "invisible", "invis");

        addValidator(new PlayerValidator());
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args)
    {
        Player p = (Player) sender;

        StaffAPI.toggleInvisibility(p, false);
    }

}
