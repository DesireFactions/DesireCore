package com.desiremc.core.validators;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.newcommands.Validator;
import com.desiremc.core.session.Session;

public class SessionOnlineValidator implements Validator<Session>
{

    @Override
    public boolean validateArgument(Session sender, String[] label, Session arg)
    {
        if (!arg.getPlayer().isOnline())
        {
            DesireCore.getLangHandler().sendRenderMessage(sender, "not_online");
            return false;
        }

        return true;
    }

}
