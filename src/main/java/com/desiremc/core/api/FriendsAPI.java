package com.desiremc.core.api;

import com.desiremc.core.DesireCore;
import com.desiremc.core.session.Session;
import com.desiremc.core.session.SessionHandler;
import com.desiremc.core.utils.FriendUtils;
import com.desiremc.core.utils.PlayerUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class FriendsAPI
{

    private static final LangHandler LANG = DesireCore.getLangHandler();

    public static void acceptRequest(Session sender, Session target)
    {
        FriendUtils.acceptFriendRequest(sender, target);

        LANG.sendRenderMessage(sender, "accepted_friend_request", "{player}", target.getName());
    }

    public static void removeFriend(Session sender, Session target)
    {
        FriendUtils.removeFriend(sender, target);

        LANG.sendRenderMessage(sender, "friend.no_longer_friend", "{player}", sender.getName());
        if (target.getOfflinePlayer().isOnline())
        {
            LANG.sendRenderMessage(target, "friend.no_longer_friend", "{player}", sender.getName());
        }
    }

    public static void addFriend(Session sender, Session target)
    {
        FriendUtils.addFriendRequest(sender, target);

        LANG.sendRenderMessage(sender, "friend.are_now_friend", "{player}", target.getName());

        if (target.getOfflinePlayer().isOnline())
        {
            LANG.sendRenderMessage(target, "friend.are_now_friend", "{player}", sender.getName());
        }
    }

    public static void denyFriend(Session sender, Session target)
    {
        FriendUtils.denyFriendRequest(sender, target);

        LANG.sendRenderMessage(sender, "friend.denied_friend_request", "{player}", target.getName());
    }

    public static void list(CommandSender sender, Session session)
    {
        listPlayers(sender, session.getFriends());
    }

    public static void listIncomming(Player sender)
    {
        listPlayers(sender, SessionHandler.getSession(sender).getIncomingFriendRequests());
    }

    public static void listOutgoing(Player sender)
    {
        listPlayers(sender, SessionHandler.getSession(sender).getIncomingFriendRequests());
    }

    private static void listPlayers(CommandSender sender, List<UUID> friends)
    {
        LANG.sendString(sender, "list-header");

        for (UUID uuid : friends)
        {
            LANG.sendRenderMessage(sender, "player", "{player}", PlayerUtils.getName(uuid));
        }
    }

}
