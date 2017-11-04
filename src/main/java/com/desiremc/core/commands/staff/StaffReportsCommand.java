package com.desiremc.core.commands.staff;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.desiremc.core.api.command.ValidCommand;
import com.desiremc.core.session.Rank;
import com.desiremc.core.staff.StaffHandler;
import com.desiremc.core.validators.PlayerValidator;

public class StaffReportsCommand extends ValidCommand
{

    public StaffReportsCommand(String name, String... aliases)
    {
        super(name, "Open reports GUI", Rank.JRMOD, new String[] {}, aliases);
        addValidator(new PlayerValidator());
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args)
    {
        Player p = (Player) sender;

        StaffHandler.getInstance().openReportsGUI(p);
    }
}
