package com.desiremc.core.newvalidators;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.newcommands.Validator;
import com.desiremc.core.session.Session;

/**
 * Used to make sure the target is not the same person sending the command.
 * 
 * @author Michael Ziluck
 */
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
