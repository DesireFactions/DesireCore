package com.desiremc.core.commands.friends;

import org.bukkit.command.CommandSender;

import com.desiremc.core.api.FriendsAPI;
import com.desiremc.core.api.command.ValidCommand;
import com.desiremc.core.parsers.PlayerSessionParser;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;
import com.desiremc.core.session.SessionHandler;
import com.desiremc.core.validators.PlayerValidator;
import com.desiremc.core.validators.SenderIsFriendsValidator;

public class FriendRemoveCommand extends ValidCommand
{

    public FriendRemoveCommand()
    {
        super("remove", "Remove a friend.", Rank.GUEST, new String[] { "target" }, new String[] { "unfriend", "delete" });
        addParser(new PlayerSessionParser(), "target");

        addValidator(new PlayerValidator());
        addValidator(new SenderIsFriendsValidator(), "target");
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args)
    {
        Session target = (Session) args[0];

        FriendsAPI.removeFriend(SessionHandler.getSession(sender), target);
    }

}
