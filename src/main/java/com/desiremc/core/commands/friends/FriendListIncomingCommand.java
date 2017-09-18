package com.desiremc.core.commands.friends;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.desiremc.core.api.FriendsAPI;
import com.desiremc.core.api.command.ValidCommand;
import com.desiremc.core.session.Rank;
import com.desiremc.core.validators.PlayerValidator;
import com.desiremc.core.validators.SenderIncommingFriendRequestsValidator;

public class FriendListIncomingCommand extends ValidCommand
{

    public FriendListIncomingCommand()
    {
        super("incoming", "Lists incoming friend requests.", Rank.GUEST, new String[]{});
        addValidator(new PlayerValidator());
        addValidator(new SenderIncommingFriendRequestsValidator());
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args)
    {
        FriendsAPI.listIncomming((Player) sender);
    }

}
