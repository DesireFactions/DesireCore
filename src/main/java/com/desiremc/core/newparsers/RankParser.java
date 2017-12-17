package com.desiremc.core.newparsers;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.newcommands.Parser;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;

import java.util.LinkedList;
import java.util.List;

public class RankParser implements Parser<Rank>
{

    @Override
    public Rank parseArgument(Session sender, String[] label, String rawArgument)
    {
        Rank rank = Rank.getRank(rawArgument);

        if (rank == null)
        {
            DesireCore.getLangHandler().sendRenderMessage(sender, "rank.invalid");
            return null;
        }

        return rank;
    }

    @Override
    public List<String> getRecommendations(Session sender, String lastWord)
    {
        List<String> options = new LinkedList<>(Rank.getRanks());
        Parser.pruneSuggestions(options, lastWord);
        return options;
    }

}
