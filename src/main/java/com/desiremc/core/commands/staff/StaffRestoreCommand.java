package com.desiremc.core.commands.staff;


import com.desiremc.core.api.command.ValidCommand;
import com.desiremc.core.parsers.PlayerSessionParser;
import com.desiremc.core.session.Rank;
import com.desiremc.core.staff.StaffHandler;
import com.desiremc.core.validators.PlayerValidator;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StaffRestoreCommand extends ValidCommand
{
    public StaffRestoreCommand()
    {
        super("restore", "Restore a players inventory.", Rank.JRMOD, new String[] { "target" });
        addValidator(new PlayerValidator());
        addParser(new PlayerSessionParser(), "target");
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args)
    {
        Player player = (Player) sender;
        Player target = (Player) args[0];

        StaffHandler.getInstance().restoreInventory(player, target);
    }
}