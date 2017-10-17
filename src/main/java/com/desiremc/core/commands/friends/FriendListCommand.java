package com.desiremc.core.commands.friends;

import org.bukkit.command.CommandSender;

import com.desiremc.core.api.FriendsAPI;
import com.desiremc.core.api.command.ValidCommand;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.SessionHandler;
import com.desiremc.core.validators.PlayerValidator;
import com.desiremc.core.validators.SenderHasFriendsValidator;

public class FriendListCommand extends ValidCommand
{

    public FriendListCommand()
    {
        super("list", "List all of your friends", Rank.GUEST, new String[] {}, "show");

        addValidator(new PlayerValidator());
        addValidator(new SenderHasFriendsValidator());
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args)
    {
        FriendsAPI.list(sender, SessionHandler.getSession(sender));
    }

}
