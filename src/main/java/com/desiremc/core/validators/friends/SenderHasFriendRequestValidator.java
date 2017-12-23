package com.desiremc.core.validators.friends;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.newcommands.Validator;
import com.desiremc.core.session.Session;
import com.desiremc.core.utils.FriendUtils;

public class SenderHasFriendRequestValidator implements Validator<Session>
{

    @Override
    public boolean validateArgument(Session sender, String[] label, Session arg)
    {
        if (FriendUtils.hasRequest(sender, arg))
        {
            return true;
        }
        
        DesireCore.getLangHandler().sendRenderMessage(sender, "friend.no_request",
                "{player}", arg.getName());
        
        return false;
    }

}
