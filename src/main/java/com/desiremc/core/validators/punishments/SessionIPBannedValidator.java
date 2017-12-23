package com.desiremc.core.validators.punishments;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.newcommands.Validator;
import com.desiremc.core.session.Session;

public class SessionIPBannedValidator implements Validator<Session>
{

    @Override
    public boolean validateArgument(Session sender, String[] label, Session arg)
    {
        if (arg.isIPBanned() == null)
        {
            DesireCore.getLangHandler().sendRenderMessage(sender, "not_ip_banned",
                    "{player}", arg.getName());
            return false;
        }

        return true;
    }

}
