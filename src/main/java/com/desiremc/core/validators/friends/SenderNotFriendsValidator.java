package com.desiremc.core.validators.friends;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.newcommands.Validator;
import com.desiremc.core.session.Session;
import com.desiremc.core.utils.FriendUtils;

/**
 * Used to make sure the sender is not already friends with the target.
 * 
 * @author Michael Ziluck
 */
public class SenderNotFriendsValidator implements Validator<Session>
{

    @Override
    public boolean validateArgument(Session sender, String[] label, Session arg)
    {
        if (FriendUtils.areFriends(sender, arg))
        {
            DesireCore.getLangHandler().sendRenderMessage(sender, "friend.already_friends",
                    "{player}", ((Session) arg).getName());
            return false;
        }

        return true;
    }

}
