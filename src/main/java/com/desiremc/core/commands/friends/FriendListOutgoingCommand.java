package com.desiremc.core.commands.friends;

import java.util.List;

import com.desiremc.core.api.FriendsAPI;
import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.ValidCommand;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;
import com.desiremc.core.validators.friends.SenderHasOutgoingFriendRequestsValidator;

public class FriendListOutgoingCommand extends ValidCommand
{

    public FriendListOutgoingCommand()
    {
        super("outgoing", "Lists outgoing friend requests.", Rank.GUEST, true);

        addSenderValidator(new SenderHasOutgoingFriendRequestsValidator());
    }

    @Override
    public void validRun(Session sender, String[] label, List<CommandArgument<?>> args)
    {
        FriendsAPI.listOutgoing(sender);
    }

}