package com.desiremc.core.newvalidators;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.newcommands.Validator;
import com.desiremc.core.session.Session;

public class SenderNotTargetValidator implements Validator<Session>
{

    @Override
    public boolean validateArgument(Session sender, String[] label, Session arg)
    {
        if (sender == arg)
        {
            DesireCore.getLangHandler().sendRenderMessage(sender, "cant_to_self");
            return false;
        }
        return true;
    }

}
