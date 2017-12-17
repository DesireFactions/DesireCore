package com.desiremc.core.commands.staff;

import com.desiremc.core.api.StaffAPI;
import com.desiremc.core.api.command.ValidCommand;
import com.desiremc.core.session.Rank;
import com.desiremc.core.validators.PlayerValidator;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StaffModeCommand extends ValidCommand
{

    public StaffModeCommand(String name, String... aliases)
    {
        super(name, "Toggle staff mode", Rank.HELPER, new String[] {}, aliases);
        addValidator(new PlayerValidator());
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args)
    {
        StaffAPI.toggleStaffMode((Player) sender);
    }

}
