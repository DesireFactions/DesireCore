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

        LANG.sendRenderMessage(sender, "friend.accepted_friend_request", "{player}", target.getName());

        if (target.getOfflinePlayer().isOnline())
        {
            LANG.sendRenderMessage(target, "friend.are_now_friend", "{player}", sender.getName());
        }
    }

    public static void removeFriend(Session sender, Session target)
    {
        FriendUtils.removeFriend(sender, target);

        LANG.sendRenderMessage(sender, "friend.no_longer_friend", "{player}", target.getName());
        if (target.getOfflinePlayer().isOnline())
        {
            LANG.sendRenderMessage(target, "friend.no_longer_friend", "{player}", sender.getName());
        }
    }

    public static void addFriend(Session sender, Session target)
    {
        FriendUtils.addFriendRequest(sender, target);

        if (FriendUtils.hasRequest(sender, target))
        {
            FriendUtils.acceptFriendRequest(sender, target);

            LANG.sendRenderMessage(sender, "friend.accepted_friend_request", "{player}", target.getName());

            if (target.getOfflinePlayer().isOnline())
            {
                LANG.sendRenderMessage(target, "friend.are_now_friend", "{player}", sender.getName());
            }
        }
        else
        {
            LANG.sendRenderMessage(sender, "friend.sent_request", "{player}", target.getName());

            if (target.getOfflinePlayer().isOnline())
            {
                LANG.sendRenderMessage(target, "friend.received_request", "{player}", sender.getName());
            }
        }
    }

    public static void denyFriend(Session sender, Session target)
    {
        FriendUtils.denyFriendRequest(sender, target);

        LANG.sendRenderMessage(sender, "friend.denied_friend_request", "{player}", target.getName());
    }

    public static void list(CommandSender sender)
    {
        List<UUID> friends = SessionHandler.getSession(sender).getFriends();
        LANG.sendRenderMessageNoPrefix(sender, "list-header");
        StringBuilder sb = new StringBuilder();
        sb.append(LANG.renderMessageNoPrefix("friend.friends"));

        for (int i = 0; i < friends.size(); i++)
        {
            if (i == (friends.size() - 1))
            {
                sb.append(PlayerUtils.getName(friends.get(i)));
            }
            else
            {
                sb.append(PlayerUtils.getName(friends.get(i)) + "&8, &r");
            }
        }
        LANG.sendRenderMessageNoPrefix(sender, sb.toString().trim());
        LANG.sendRenderMessageNoPrefix(sender, "list-header");
    }

    public static void listIncomming(Player sender)
    {
        List<UUID> friends = SessionHandler.getSession(sender).getIncomingFriendRequests();
        LANG.sendRenderMessageNoPrefix(sender, "list-header");
        StringBuilder sb = new StringBuilder();
        sb.append(LANG.renderMessageNoPrefix("friend.incoming_friends"));

        for (int i = 0; i < friends.size(); i++)
        {
            if (i == (friends.size() - 1))
            {
                sb.append(PlayerUtils.getName(friends.get(i)));
            }
            else
            {
                sb.append(PlayerUtils.getName(friends.get(i)) + "&8, &r");
            }
        }
        LANG.sendRenderMessageNoPrefix(sender, sb.toString().trim());
        LANG.sendRenderMessageNoPrefix(sender, "list-header");
    }

    public static void listOutgoing(Player sender)
    {
        List<UUID> friends = SessionHandler.getSession(sender).getOutgoingFriendRequests();
        LANG.sendRenderMessageNoPrefix(sender, "list-header");
        StringBuilder sb = new StringBuilder();
        sb.append(LANG.renderMessageNoPrefix("friend.outgoing_friends"));

        for (int i = 0; i < friends.size(); i++)
        {
            if (i == (friends.size() - 1))
            {
                sb.append(PlayerUtils.getName(friends.get(i)));
            }
            else
            {
                sb.append(PlayerUtils.getName(friends.get(i)) + "&8, &r");
            }
        }
        LANG.sendRenderMessageNoPrefix(sender, sb.toString().trim());
        LANG.sendRenderMessageNoPrefix(sender, "list-header");
    }
}
