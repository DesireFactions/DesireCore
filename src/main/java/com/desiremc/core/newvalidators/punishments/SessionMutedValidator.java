package com.desiremc.core.newvalidators.punishments;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.newcommands.Validator;
import com.desiremc.core.session.Session;

public class SessionMutedValidator implements Validator<Session>
{

    @Override
    public boolean validateArgument(Session sender, String[] label, Session arg)
    {
        if (arg.isMuted() != null)
        {
            DesireCore.getLangHandler().sendRenderMessage(sender, "mute.not_muted",
                    "{target}", arg.getName());
            return false;
        }
        return true;
    }

}
