package com.desiremc.core.validators.friends;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.newcommands.SenderValidator;
import com.desiremc.core.session.Session;

public class SenderHasIncomingFriendRequestsValidator implements SenderValidator
{

    @Override
    public boolean validate(Session sender)
    {
        if (sender.getIncomingFriendRequests().size() <= 0)
        {
            DesireCore.getLangHandler().sendRenderMessage(sender, "friend.no_incoming");
            return false;
        }
        return true;
    }

}
