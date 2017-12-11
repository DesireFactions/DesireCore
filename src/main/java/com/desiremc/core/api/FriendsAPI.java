package com.desiremc.core.api;

import java.util.List;
import java.util.UUID;

import com.desiremc.core.DesireCore;
import com.desiremc.core.session.Session;
import com.desiremc.core.utils.FriendUtils;
import com.desiremc.core.utils.PlayerUtils;

public class FriendsAPI
{

    public static void acceptRequest(Session sender, Session target)
    {
        FriendUtils.acceptFriendRequest(sender, target);

        DesireCore.getLangHandler().sendRenderMessage(sender, "friend.accepted_friend_request", "{player}", target.getName());

        if (target.getOfflinePlayer().isOnline())
        {
            DesireCore.getLangHandler().sendRenderMessage(target, "friend.are_now_friend", "{player}", sender.getName());
        }
    }

    public static void removeFriend(Session sender, Session target)
    {
        FriendUtils.removeFriend(sender, target);

        DesireCore.getLangHandler().sendRenderMessage(sender, "friend.no_longer_friend", "{player}", target.getName());
        if (target.getOfflinePlayer().isOnline())
        {
            DesireCore.getLangHandler().sendRenderMessage(target, "friend.no_longer_friend", "{player}", sender.getName());
        }
    }

    public static void addFriend(Session sender, Session target)
    {
        FriendUtils.addFriendRequest(sender, target);

        if (FriendUtils.hasRequest(sender, target))
        {
            FriendUtils.acceptFriendRequest(sender, target);

            DesireCore.getLangHandler().sendRenderMessage(sender, "friend.accepted_friend_request", "{player}", target.getName());

            if (target.getOfflinePlayer().isOnline())
            {
                DesireCore.getLangHandler().sendRenderMessage(target, "friend.are_now_friend", "{player}", sender.getName());
            }
        }
        else
        {
            DesireCore.getLangHandler().sendRenderMessage(sender, "friend.sent_request", "{player}", target.getName());

            if (target.getOfflinePlayer().isOnline())
            {
                DesireCore.getLangHandler().sendRenderMessage(target, "friend.received_request", "{player}", sender.getName());
            }
        }
    }

    public static void denyFriend(Session sender, Session target)
    {
        FriendUtils.denyFriendRequest(sender, target);

        DesireCore.getLangHandler().sendRenderMessage(sender, "friend.denied_friend_request", "{player}", target.getName());
    }

    public static void list(Session sender)
    {
        List<UUID> friends = sender.getFriends();
        DesireCore.getLangHandler().sendRenderMessageNoPrefix(sender, "list-header");
        StringBuilder sb = new StringBuilder();
        sb.append(DesireCore.getLangHandler().renderMessageNoPrefix("friend.friends"));

        for (int i = 0; i < friends.size(); i++)
        {
            if (i == (friends.size() - 1))
            {
                sb.append(PlayerUtils.getName(friends.get(i)));
            }
            else
            {
                sb.append(PlayerUtils.getName(friends.get(i)) + "§8, §r");
            }
        }
        DesireCore.getLangHandler().sendRenderMessageNoPrefix(sender, sb.toString().trim());
        DesireCore.getLangHandler().sendRenderMessageNoPrefix(sender, "list-header");
    }

    public static void listIncomming(Session sender)
    {
        List<UUID> friends = sender.getIncomingFriendRequests();
        DesireCore.getLangHandler().sendRenderMessageNoPrefix(sender, "list-header");
        StringBuilder sb = new StringBuilder();
        sb.append(DesireCore.getLangHandler().renderMessageNoPrefix("friend.incoming_friends"));

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
        DesireCore.getLangHandler().sendRenderMessageNoPrefix(sender, sb.toString().trim());
        DesireCore.getLangHandler().sendRenderMessageNoPrefix(sender, "list-header");
    }

    public static void listOutgoing(Session sender)
    {
        List<UUID> friends = sender.getOutgoingFriendRequests();
        DesireCore.getLangHandler().sendRenderMessageNoPrefix(sender, "list-header");
        StringBuilder sb = new StringBuilder();
        sb.append(DesireCore.getLangHandler().renderMessageNoPrefix("friend.outgoing_friends"));

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
        DesireCore.getLangHandler().sendRenderMessageNoPrefix(sender, sb.toString().trim());
        DesireCore.getLangHandler().sendRenderMessageNoPrefix(sender, "list-header");
    }
}
