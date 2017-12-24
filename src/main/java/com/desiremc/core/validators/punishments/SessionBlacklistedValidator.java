package com.desiremc.core.validators.punishments;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.newcommands.Validator;
import com.desiremc.core.punishment.Punishment;
import com.desiremc.core.session.Session;

public class SessionBlacklistedValidator implements Validator<Session>
{

    @Override
    public boolean validateArgument(Session sender, String[] label, Session arg)
    {
        for (Punishment punishment : arg.getActivePunishments())
        {
            if (punishment.isBlacklisted())
            {
                DesireCore.getLangHandler().sendRenderMessage(sender, "not_blacklisted", true, false);
                return false;
            }
        }

        return true;
    }

}
