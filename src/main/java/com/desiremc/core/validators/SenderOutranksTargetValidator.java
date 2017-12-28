package com.desiremc.core.validators;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.newcommands.Validator;
import com.desiremc.core.session.Session;

public class SenderOutranksTargetValidator implements Validator<Session>
{

    @Override
    public boolean validateArgument(Session sender, String[] label, Session arg)
    {
        if (sender.getRank().getId() <= arg.getRank().getId())
        {
            DesireCore.getLangHandler().sendRenderMessage(sender, "sender_doesnt_outrank", true, false);
            return false;
        }
        return true;
    }

}
