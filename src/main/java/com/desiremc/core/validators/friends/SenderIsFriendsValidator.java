package com.desiremc.core.validators.friends;

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
            DesireCore.getLangHandler().sendRenderMessage(sender, "friend.not_friends", true, false,
                    "{player}", arg.getName());
            return false;
        }

        return true;
    }

}
