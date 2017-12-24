package com.desiremc.core.commands.rank;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.ValidCommand;
import com.desiremc.core.session.Session;

import java.util.List;

public class RankCheckCommand extends ValidCommand
{

    public RankCheckCommand()
    {
        super("check", "Check your rank.", true, new String[] { "show" });
    }

    @Override
    public void validRun(Session sender, String[] label, List<CommandArgument<?>> args)
    {
        DesireCore.getLangHandler().sendRenderMessage(sender, "rank.check", true, false,
                "{color}", sender.getRank().getColor().toString(),
                "{rank}", sender.getRank().getDisplayName());
    }

}
