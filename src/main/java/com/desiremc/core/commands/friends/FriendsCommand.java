package com.desiremc.core.commands.friends;

import com.desiremc.core.api.command.ValidBaseCommand;
import com.desiremc.core.session.Rank;

public class FriendsCommand extends ValidBaseCommand
{

    public FriendsCommand()
    {
        super("friends", "control friends list", Rank.GUEST, "friend");
        addSubCommand(new FriendAcceptCommand());
        addSubCommand(new FriendDeclineCommand());
        addSubCommand(new FriendAddCommand());
        addSubCommand(new FriendRemoveCommand());
        addSubCommand(new FriendListCommand());
        addSubCommand(new FriendListIncomingCommand());
        addSubCommand(new FriendListOutgoingCommand());
    }

}
