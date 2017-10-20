package com.desiremc.core.api;

import org.bukkit.command.CommandSender;

import com.desiremc.core.DesireCore;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;
import com.desiremc.core.session.SessionHandler;

/**
 * @author Ryan Radomski
 *
 */
public class RankAPI
{

    private static final LangHandler LANG = DesireCore.getLangHandler();

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
        Session s = SessionHandler.getSession(sender);

        LANG.sendRenderMessage(sender, "rank.check", "{color}", s.getRank().getColor().toString(), "{rank}", s.getRank().getDisplayName());
    }

}