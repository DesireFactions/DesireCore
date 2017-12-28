package com.desiremc.core.api.newcommands;

import com.desiremc.core.DesireCore;
import com.desiremc.core.session.Session;
import com.desiremc.core.utils.ReflectionUtils;

public class HelpPageValidator implements Validator<Integer>
{

    private ValidBaseCommand baseCommand;

    HelpPageValidator(ValidBaseCommand baseCommand)
    {
        this.baseCommand = baseCommand;
    }

    @Override
    public boolean validateArgument(Session sender, String[] label, Integer arg)
    {
        int pages = (baseCommand.getUsableCommands(sender).size() + 5) / 6;
        if (arg > pages)
        {
            DesireCore.getLangHandler().sendRenderMessage(sender, "pages_size", true, false,
                    "{be}", pages == 1 ? "is" : "are",
                    "{max}", pages,
                    "{s}", pages == 1 ? "" : "s");
            return false;
        }

        if (arg == 0)
        {
            ReflectionUtils.setIntegerValue(arg, 1);
        }

        return true;
    }

}
