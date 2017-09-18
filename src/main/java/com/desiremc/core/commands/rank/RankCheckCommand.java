package com.desiremc.core.commands.rank;

import org.bukkit.command.CommandSender;

import com.desiremc.core.api.RankAPI;
import com.desiremc.core.api.command.ValidCommand;
import com.desiremc.core.session.Rank;
import com.desiremc.core.validators.PlayerValidator;

public class RankCheckCommand extends ValidCommand
{

    public RankCheckCommand()
    {
        super("check", "Check your rank.", Rank.GUEST, new String[] {}, "show");
        addValidator(new PlayerValidator());
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args)
    {
        RankAPI.checkRank(sender, label);
    }

}
