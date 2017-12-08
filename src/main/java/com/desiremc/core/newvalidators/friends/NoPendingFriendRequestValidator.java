package com.desiremc.core.newvalidators.friends;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.newcommands.Validator;
import com.desiremc.core.session.Session;
import com.desiremc.core.utils.FriendUtils;

/**
 * Used to make sure the sender does not have a pending friend request sent to the target.
 * 
 * @author Michael Ziluck
 */
public class NoPendingFriendRequestValidator implements Validator<Session>
{

    @Override
    public boolean validateArgument(Session sender, String[] label, Session arg)
    {
        if (FriendUtils.hasRequest(sender, arg))
        {
            DesireCore.getLangHandler().sendRenderMessage(sender, "friend.already_sent_request");
            return false;
        }
        return true;
    }

}
