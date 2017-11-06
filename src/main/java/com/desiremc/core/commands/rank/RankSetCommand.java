package com.desiremc.core.commands.rank;

import com.desiremc.core.validators.RankSetValidator;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.command.ValidCommand;
import com.desiremc.core.parsers.PlayerSessionParser;
import com.desiremc.core.parsers.RankParser;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;
import com.desiremc.core.session.SessionHandler;
import com.desiremc.core.utils.PlayerUtils;

public class RankSetCommand extends ValidCommand
{

    public RankSetCommand()
    {
        super("set", "Sets a user's rank.", Rank.ADMIN, new String[] { "target", "rank" }, "update");
        addParser(new PlayerSessionParser(), "target");
        addParser(new RankParser(), "rank");
        addValidator(new RankSetValidator(), "rank");
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args)
    {
        if (DesireCore.DEBUG)
        {
            System.out.println("validRun() called in RankSetCommand.");
        }

        Session target = (Session) args[0];
        Rank rank = (Rank) args[1];

        if (target.getRank().isStaff() && !rank.isStaff())
        {
            SessionHandler.getInstance().removeStaff(target.getUniqueId());
        }

        if (DesireCore.DEBUG)
        {
            System.out.println("validRun() rank before: " + target.getRank().getDisplayName());
        }
        target.setRank(rank);
        if (DesireCore.DEBUG)
        {
            System.out.println("validRun() rank after: " + target.getRank().getDisplayName());
        }

        if (DesireCore.DEBUG)
        {
            System.out.println("validRun() query after save: " + SessionHandler.getSession(target.getUniqueId()).getRank().getDisplayName());
        }

        DesireCore.getLangHandler().sendRenderMessage(sender, "rank.set", "{player}", target.getName(), "{rank}", target.getRank().getDisplayName());

        Player player = PlayerUtils.getPlayer(target.getUniqueId());
        if (player != null)
        {
            DesireCore.getLangHandler().sendRenderMessage(player, "rank.inform", "{rank}", target.getRank().getDisplayName());
        }
    }
}
