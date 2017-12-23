package com.desiremc.core.commands.rank;

import com.desiremc.core.api.newcommands.ValidBaseCommand;
import com.desiremc.core.session.Rank;

public class RankCommand extends ValidBaseCommand
{

    public RankCommand()
    {
        super("rank", "View your rank or manage others.", Rank.GUEST);
        addSubCommand(new RankCheckCommand());
        addSubCommand(new RankSetCommand());
        addSubCommand(new RankListCommand());
    }

}
