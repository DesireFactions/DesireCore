package com.desiremc.core.commands.rank;

import java.util.List;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.ValidCommand;
import com.desiremc.core.session.Session;

public class RankCheckCommand extends ValidCommand
{

    public RankCheckCommand()
    {
        super("check", "Check your rank.", true, new String[] { "show" });
    }

    @Override
    public void validRun(Session sender, String[] label, List<CommandArgument<?>> args)
    {
        DesireCore.getLangHandler().sendRenderMessage(sender, "rank.check",
                "{color}", sender.getRank().getColor().toString(),
                "{rank}", sender.getRank().getDisplayName());
    }

}
