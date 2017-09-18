package com.desiremc.core.commands.rank;

import org.bukkit.command.CommandSender;

import com.desiremc.core.api.RankAPI;
import com.desiremc.core.api.command.ValidCommand;
import com.desiremc.core.session.Rank;

/**
 * @author Ryan Radomski
 */
public class RankListCommand extends ValidCommand
{

    public RankListCommand()
    {
        super("list", "list all the ranks", Rank.GUEST, new String[] {});
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args)
    {
        RankAPI.listRanks(sender);
    }

}
