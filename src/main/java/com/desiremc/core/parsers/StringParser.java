package com.desiremc.core.parsers;

import java.util.List;

import com.desiremc.core.api.newcommands.Parser;
import com.desiremc.core.session.Session;

public class StringParser implements Parser<String>
{

    @Override
    public String parseArgument(Session sender, String[] label, String argument)
    {
        return argument;
    }

    @Override
    public List<String> getRecommendations(Session sender, String lastWord)
    {
        return null;
    }

}
