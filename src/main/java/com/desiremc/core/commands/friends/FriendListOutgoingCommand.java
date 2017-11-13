package com.desiremc.core.commands.friends;

import com.desiremc.core.api.FriendsAPI;
import com.desiremc.core.api.command.ValidCommand;
import com.desiremc.core.session.Rank;
import com.desiremc.core.validators.PlayerValidator;
import com.desiremc.core.validators.SenderHasOutgoingFriendRequestsValidator;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FriendListOutgoingCommand extends ValidCommand
{

    public FriendListOutgoingCommand()
    {
        super("outgoing", "Lists outgoing friend requests.", Rank.GUEST, new String[] {});
        addValidator(new PlayerValidator());
        addValidator(new SenderHasOutgoingFriendRequestsValidator());
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args)
    {
        FriendsAPI.listOutgoing((Player) sender);
    }

}