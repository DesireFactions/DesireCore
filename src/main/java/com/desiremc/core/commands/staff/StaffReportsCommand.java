package com.desiremc.core.commands.staff;

import com.desiremc.core.api.command.ValidCommand;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.StaffHandler;
import com.desiremc.core.validators.PlayerValidator;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class StaffReportsCommand extends ValidCommand
{

    public StaffReportsCommand()
    {
        super("reports", "Open reports GUI", Rank.HELPER, new String[]{});
        addValidator(new PlayerValidator());
    }

    public void validRun(CommandSender sender, String label, Object... args)
    {
        Player p = (Player) sender;

        StaffHandler.getInstance().openReportsGUI(p);
    }
}
