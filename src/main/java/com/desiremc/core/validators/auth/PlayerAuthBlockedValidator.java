package com.desiremc.core.validators.auth;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.newcommands.SenderValidator;
import com.desiremc.core.listeners.AuthListener;
import com.desiremc.core.session.Session;

public class PlayerAuthBlockedValidator implements SenderValidator
{

    @Override
    public boolean validate(Session sender)
    {
        if (AuthListener.authBlocked.contains(sender.getUniqueId()))
        {
            return true;
        }
        else
        {
            DesireCore.getLangHandler().sendRenderMessage(sender, "auth.already-auth");
            return false;
        }
    }

}
