package com.desiremc.core.newvalidators.friends;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.newcommands.Validator;
import com.desiremc.core.session.Session;
import com.desiremc.core.utils.FriendUtils;

public class SenderIsFriendsValidator implements Validator<Session>
{

    @Override
    public boolean validateArgument(Session sender, String[] label, Session arg)
    {
        if (!FriendUtils.areFriends(sender, arg))
        {
            DesireCore.getLangHandler().sendRenderMessage(sender, "friend.not_friends",
                    "{player}", ((Session) arg).getName());
            return false;
        }

        return true;
    }

}
