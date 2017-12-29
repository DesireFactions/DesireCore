package com.desiremc.core.validators;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.newcommands.SenderValidator;
import com.desiremc.core.session.Session;

public class SenderDonorValidator implements SenderValidator
{

    @Override
    public boolean validate(Session sender)
    {
        if (sender.getRank().isDonor())
        {
            return true;
        }

        DesireCore.getLangHandler().sendRenderMessage(sender, "not_donor", true, false);
        return false;
    }

}
