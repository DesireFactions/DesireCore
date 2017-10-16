package com.desiremc.core.api;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.desiremc.core.DesireCore;
import com.desiremc.core.session.HCFSession;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;
import com.desiremc.core.session.SessionHandler;
import com.desiremc.core.utils.PlayerUtils;

/**
 * @author Ryan Radomski
 *
 */
public class RankAPI
{

    private static final LangHandler LANG = DesireCore.getLangHandler();

    /**
     * Change a player {@link HCFSession}'s {@link Rank}
     * 
     * @param sender
     * @param label
     * @param name
     * @param rank
     */
    public static void setRank(CommandSender sender, String label, Session target, Rank rank, boolean display)
    {
        if (target.getRank().isStaff() && !rank.isStaff())
        {
            SessionHandler.getInstance().removeStaff(target.getUniqueId());
        }
        target.setRank(rank);

        SessionHandler.getInstance().save(target);
        LANG.sendRenderMessage(sender, "rank.set", "{player}", target.getName(), "{rank}", target.getRank().getDisplayName());

        if (Bukkit.getPlayer(target.getUniqueId()) != null && display)
        {
            PlayerUtils.setPrefix(target.getRank().getPrefix(), Bukkit.getPlayer(target.getUniqueId()));
            LANG.sendRenderMessage(Bukkit.getPlayer(target.getUniqueId()), "rank.inform", "{rank}", target.getRank().getDisplayName());
        }
    }

    /**
     * Change a player {@link HCFSession}'s {@link Rank}
     * 
     * @param sender
     * @param label
     * @param name
     * @param rank
     */
    public static void setRank(CommandSender sender, String label, Session target, Rank rank)
    {
        setRank(sender, label, target, rank, false);
    }

    /**
     * Send a player a list of every {@link Rank}
     * 
     * @param sender
     */
    public static void listRanks(CommandSender sender)
    {
        for (Rank r : Rank.values())
        {
            LANG.sendRenderMessage(sender, "rank.list", "{color}", r.getColor().toString(), "{rank}", r.getDisplayName());
        }
    }

    /**
     * Send a player their current {@link Rank}
     * 
     * @param sender
     * @param label
     */
    public static void checkRank(CommandSender sender, String label)
    {
        Session s = SessionHandler.getSession(((Player) sender).getUniqueId());

        LANG.sendRenderMessage(sender, "rank.check", "{color}", s.getRank().getColor().toString(), "{rank}", s.getRank().getDisplayName());
    }

}