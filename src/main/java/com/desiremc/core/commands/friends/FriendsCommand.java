package com.desiremc.core.commands.friends;

import com.desiremc.core.api.newcommands.ValidBaseCommand;
import com.desiremc.core.session.Rank;

public class FriendsCommand extends ValidBaseCommand
{

    public FriendsCommand()
    {
        super("friends", "Control friends list.", Rank.GUEST, new String[] { "friend" });

        addSubCommand(new FriendAcceptCommand());
        addSubCommand(new FriendDeclineCommand());
        addSubCommand(new FriendAddCommand());
        addSubCommand(new FriendRemoveCommand());
        addSubCommand(new FriendListCommand());
        addSubCommand(new FriendListIncomingCommand());
        addSubCommand(new FriendListOutgoingCommand());
    }

}
