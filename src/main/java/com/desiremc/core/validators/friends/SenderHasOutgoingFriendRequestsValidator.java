package com.desiremc.core.validators.friends;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.newcommands.SenderValidator;
import com.desiremc.core.session.Session;

public class SenderHasOutgoingFriendRequestsValidator implements SenderValidator
{

    @Override
    public boolean validate(Session sender)
    {
        if (sender.getOutgoingFriendRequests().size() <= 0)
        {
            DesireCore.getLangHandler().sendRenderMessage(sender, "friend.no_outgoing");
            return false;
        }
        return true;
    }

}
