package com.desiremc.core.commands.friends;

import org.bukkit.command.CommandSender;

import com.desiremc.core.api.FriendsAPI;
import com.desiremc.core.api.command.ValidCommand;
import com.desiremc.core.parsers.PlayerSessionParser;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;
import com.desiremc.core.session.SessionHandler;
import com.desiremc.core.validators.PlayerValidator;
import com.desiremc.core.validators.SenderHasFriendRequestValidator;

public class FriendAcceptCommand extends ValidCommand
{

    public FriendAcceptCommand()
    {
        super("accept", "Accept a friend request.", Rank.GUEST, new String[] { "target" }, "confirm");
        addParser(new PlayerSessionParser(), "target");

        addValidator(new PlayerValidator());
        addValidator(new SenderHasFriendRequestValidator(), "target");
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args)
    {
        Session target = (Session) args[0];
        Session session = SessionHandler.getSession(sender);

        FriendsAPI.acceptRequest(session, target);
    }

}
