package com.desiremc.core.parsers;

import java.util.List;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.newcommands.Parser;
import com.desiremc.core.session.Session;

public class DoubleParser implements Parser<Double>
{

    @Override
    public Double parseArgument(Session sender, String[] label, String rawArgument)
    {
        double val;
        try
        {
            val = Double.parseDouble(rawArgument);
        } catch (NumberFormatException ex)
        {
            DesireCore.getLangHandler().sendRenderMessage(sender, "arg_not_number");
            return null;
        }
        return val;
    }

    @Override
    public List<String> getRecommendations(Session sender, String lastWord)
    {
        return null;
    }

}
