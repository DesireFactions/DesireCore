package com.desiremc.core.commands.rank;

import org.bukkit.command.CommandSender;

import com.desiremc.core.api.RankAPI;
import com.desiremc.core.api.command.ValidCommand;
import com.desiremc.core.parsers.PlayerSessionParser;
import com.desiremc.core.parsers.RankParser;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;

public class RankSetCommand extends ValidCommand
{

    public RankSetCommand()
    {
        super("set", "Sets a user's rank.", Rank.ADMIN, new String[] { "target", "rank" }, "update");
        addParser(new PlayerSessionParser(), "target");
        addParser(new RankParser(), "rank");
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args)
    {

        Session target = (Session) args[0];
        Rank rank = (Rank) args[1];

        RankAPI.setRank(sender, label, target, rank);
    }
}
