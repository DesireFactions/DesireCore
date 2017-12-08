package com.desiremc.core.commands.friends;

import java.util.List;

import com.desiremc.core.api.FriendsAPI;
import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.ValidCommand;
import com.desiremc.core.newvalidators.friends.SenderHasFriendsValidator;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;

public class FriendListCommand extends ValidCommand
{

    public FriendListCommand()
    {
        super("list", "List all of your friends", Rank.GUEST, true, new String[] { "show" });

        addSenderValidator(new SenderHasFriendsValidator());
    }

    @Override
    public void validRun(Session sender, String[] label, List<CommandArgument<?>> args)
    {
        FriendsAPI.list(sender);
    }

}
