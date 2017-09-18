package com.desiremc.core.commands.friends;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.desiremc.core.api.FriendsAPI;
import com.desiremc.core.api.command.ValidCommand;
import com.desiremc.core.parsers.PlayerSessionParser;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;
import com.desiremc.core.validators.PlayerValidator;
import com.desiremc.core.validators.SenderNotFriendsValidator;

public class FriendAddCommand extends ValidCommand
{

    public FriendAddCommand()
    {
        super("add", "Add a friend.", Rank.GUEST, new String[]{"target"}, "invite", "befriend");
        addParser(new PlayerSessionParser(), "target");
        addValidator(new PlayerValidator());
        addValidator(new SenderNotFriendsValidator(), "target");
    }

    public void validRun(CommandSender sender, String label, Object... args)
    {
        Session target = (Session) args[0];

        FriendsAPI.addFriend((Player) sender, target);
    }

}
