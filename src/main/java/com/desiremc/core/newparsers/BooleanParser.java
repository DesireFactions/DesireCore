package com.desiremc.core.newparsers;

import java.util.Arrays;
import java.util.List;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.newcommands.Parser;
import com.desiremc.core.session.Session;
import com.desiremc.core.utils.StringUtils;

public class BooleanParser implements Parser<Boolean>
{

    @Override
    public Boolean parseArgument(Session sender, String[] label, String rawArgument)
    {
        rawArgument = rawArgument.toLowerCase();

        if (StringUtils.containsAny(rawArgument, "true", "on", "enable", "1"))
        {
            return Boolean.TRUE;
        }
        else if (StringUtils.containsAny(rawArgument, "false", "off", "disable", "0"))
        {
            return Boolean.FALSE;
        }

        DesireCore.getLangHandler().sendRenderMessage(sender, "boolean.invalid");
        return null;
    }

    @Override
    public List<String> getRecommendations(Session sender, String lastWord)
    {
        List<String> options = Arrays.asList("true", "false", "on", "off", "enable", "disable");
        Parser.pruneSuggestions(options, lastWord);
        return options;
    }

}
