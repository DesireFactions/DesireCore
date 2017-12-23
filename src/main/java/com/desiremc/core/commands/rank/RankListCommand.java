package com.desiremc.core.commands.rank;

import java.util.List;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.ValidCommand;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;

/**
 * @author Michael Ziluck
 */
public class RankListCommand extends ValidCommand
{

    public RankListCommand()
    {
        super("list", "List all the ranks", Rank.GUEST, new String[] {});
    }

    @Override
    public void validRun(Session sender, String[] label, List<CommandArgument<?>> args)
    {
        for (Rank rank : Rank.values())
        {
            DesireCore.getLangHandler().sendRenderMessage(sender, "rank.list",
                    "{color}", rank.getColor().toString(),
                    "{rank}", rank.getDisplayName());
        }
    }

}
