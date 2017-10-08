package com.desiremc.core.commands.staff;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.desiremc.core.api.command.ValidCommand;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.StaffHandler;
import com.desiremc.core.validators.PlayerValidator;

public class StaffReportsCommand extends ValidCommand
{

    public StaffReportsCommand()
    {
        super("reports", "Open reports GUI", Rank.HELPER, new String[]{});
        addValidator(new PlayerValidator());
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args)
    {
        Player p = (Player) sender;

        StaffHandler.getInstance().openReportsGUI(p);
    }
}
