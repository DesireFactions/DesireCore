package com.desiremc.core.commands.friends;

import java.util.List;

import com.desiremc.core.api.FriendsAPI;
import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.CommandArgumentBuilder;
import com.desiremc.core.api.newcommands.ValidCommand;
import com.desiremc.core.newparsers.SessionParser;
import com.desiremc.core.newvalidators.friends.SenderIsFriendsValidator;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;

public class FriendRemoveCommand extends ValidCommand
{

    public FriendRemoveCommand()
    {
        super("remove", "Remove a friend.", Rank.GUEST, true, new String[] { "unfriend", "delete" });

        addArgument(CommandArgumentBuilder.createBuilder(Session.class)
                .setName("target")
                .setParser(new SessionParser())
                .addValidator(new SenderIsFriendsValidator())
                .build());
    }

    @Override
    public void validRun(Session sender, String[] label, List<CommandArgument<?>> args)
    {
        Session target = (Session) args.get(0).getValue();

        FriendsAPI.removeFriend(sender, target);
    }

}
