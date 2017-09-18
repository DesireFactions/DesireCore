package com.desiremc.core.commands.friends;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.desiremc.core.api.FriendsAPI;
import com.desiremc.core.api.command.ValidCommand;
import com.desiremc.core.parsers.PlayerSessionParser;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;
import com.desiremc.core.validators.PlayerValidator;
import com.desiremc.core.validators.SenderFriendRequestValidator;
import com.desiremc.core.validators.SenderIsFriendsValidator;

public class FriendDeclineCommand extends ValidCommand
{

    public FriendDeclineCommand()
    {
        super("decline", "Decline a friend request.", Rank.GUEST, new String[]{"target"}, "deny");
        addParser(new PlayerSessionParser(), "target");
        addValidator(new PlayerValidator());
        addValidator(new SenderFriendRequestValidator(), "target");
        addValidator(new SenderIsFriendsValidator(), "target");
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args)
    {
        Session target = (Session) args[0];

        FriendsAPI.denyFriend((Player) sender, target);
    }

}
