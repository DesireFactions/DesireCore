package com.desiremc.core.commands.rank;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

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

        SessionHandler.getInstance().save(target);

        if (DesireCore.DEBUG)
        {
            System.out.println("validRun() query after save: " + SessionHandler.getSession(target.getUniqueId()).getRank().getDisplayName());
        }

        PlayerUtils.setPrefix(target.getRank().getPrefix(), Bukkit.getPlayer(target.getUniqueId()));

        DesireCore.getLangHandler().sendRenderMessage(sender, "rank.set", "{player}", target.getName(), "{rank}", target.getRank().getDisplayName());

        if (Bukkit.getPlayer(target.getUniqueId()) != null)
        {
            DesireCore.getLangHandler().sendRenderMessage(Bukkit.getPlayer(target.getUniqueId()), "rank.inform", "{rank}", target.getRank().getDisplayName());
        }
    }
}
