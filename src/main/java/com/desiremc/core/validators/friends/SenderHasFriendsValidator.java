package com.desiremc.core.validators.friends;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.newcommands.SenderValidator;
import com.desiremc.core.session.Session;

public class SenderHasFriendsValidator implements SenderValidator
{

    @Override
    public boolean validate(Session sender)
    {
        if (sender.getFriends().size() > 0)
        {
            return true;
        }
        else
        {
            DesireCore.getLangHandler().sendRenderMessage(sender, "friend.no_friends", true, false);
            return false;
        }
    }

}
