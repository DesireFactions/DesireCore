package com.desiremc.core.newparsers;

import java.util.List;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.newcommands.Parser;
import com.desiremc.core.session.Session;
import com.desiremc.core.utils.DateUtils;

public class TimeParser implements Parser<Long>
{

    @Override
    public Long parseArgument(Session sender, String[] label, String rawArgument)
    {
        try
        {
            return DateUtils.parseDateDiff(rawArgument, true);
        }
        catch (Exception e)
        {
            DesireCore.getLangHandler().sendRenderMessage(sender, "not_time");
            return null;
        }
    }

    @Override
    public List<String> getRecommendations(Session sender, String lastWord)
    {
        return null;
    }

}
