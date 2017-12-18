package com.desiremc.core.newparsers;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.newcommands.Parser;
import com.desiremc.core.session.Session;

import java.util.List;

public class PositiveDoubleParser implements Parser<Double>
{

    @Override
    public Double parseArgument(Session sender, String[] label, String argument)
    {
        double d;
        try
        {
            d = Double.parseDouble(argument);
        } catch (NumberFormatException ex)
        {
            DesireCore.getLangHandler().sendRenderMessage(sender, "arg_not_number");
            return null;
        }
        if (d < 0)
        {
            DesireCore.getLangHandler().sendRenderMessage(sender, "number.too_large");
            return null;
        }
        return d;
    }

    @Override
    public List<String> getRecommendations(Session sender, String lastWord)
    {
        return null;
    }

}
