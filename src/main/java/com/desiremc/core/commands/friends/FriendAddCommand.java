package com.desiremc.core.commands.friends;

import java.util.List;

import com.desiremc.core.api.FriendsAPI;
import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.CommandArgumentBuilder;
import com.desiremc.core.api.newcommands.ValidCommand;
import com.desiremc.core.newparsers.SessionParser;
import com.desiremc.core.newvalidators.SenderNotTargetValidator;
import com.desiremc.core.newvalidators.friends.NoPendingFriendRequestValidator;
import com.desiremc.core.newvalidators.friends.SenderNotFriendsValidator;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;

public class FriendAddCommand extends ValidCommand
{

    public FriendAddCommand()
    {
        super("add", "Add a friend.", Rank.GUEST, true, new String[] { "invite", "befriend" });

        addArgument(CommandArgumentBuilder.createBuilder(Session.class)
                .setName("target")
                .setParser(new SessionParser())
                .addValidator(new SenderNotFriendsValidator())
                .addValidator(new SenderNotTargetValidator())
                .addValidator(new NoPendingFriendRequestValidator())
                .build());
    }

    @Override
    public void validRun(Session sender, String[] label, List<CommandArgument<?>> args)
    {
        Session target = (Session) args.get(0).getValue();

        FriendsAPI.addFriend(sender, target);
    }

}
