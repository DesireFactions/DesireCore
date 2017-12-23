package com.desiremc.core.validators.punishments;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.newcommands.Validator;
import com.desiremc.core.session.Session;

public class SessionBannedValidator implements Validator<Session>
{

    @Override
    public boolean validateArgument(Session sender, String[] label, Session arg)
    {

        if (arg.isBanned() == null)
        {
            DesireCore.getLangHandler().sendRenderMessage(sender, "not_banned",
                    "{player}", arg.getName());
            return false;
        }

        return true;
    }

}
