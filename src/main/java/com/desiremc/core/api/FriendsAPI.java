package com.desiremc.core.api;

import com.desiremc.core.DesireCore;
import com.desiremc.core.session.Session;
import com.desiremc.core.utils.FriendUtils;
import com.desiremc.core.utils.PlayerUtils;

import java.util.List;
import java.util.UUID;

public class FriendsAPI
{

    public static void acceptRequest(Session sender, Session target)
    {
        FriendUtils.acceptFriendRequest(sender, target);

        DesireCore.getLangHandler().sendRenderMessage(sender, "friend.accepted_friend_request", true, false, "{player}", target.getName());

        if (target.isOnline())
        {
            DesireCore.getLangHandler().sendRenderMessage(target, "friend.are_now_friend", true, false, "{player}", sender.getName());
        }
    }

    public static void removeFriend(Session sender, Session target)
    {
        FriendUtils.removeFriend(sender, target);

        DesireCore.getLangHandler().sendRenderMessage(sender, "friend.no_longer_friend", true, false, "{player}", target.getName());
        if (target.isOnline())
        {
            DesireCore.getLangHandler().sendRenderMessage(target, "friend.no_longer_friend", true, false, "{player}", sender.getName());
        }
    }

    public static void addFriend(Session sender, Session target)
    {
        FriendUtils.addFriendRequest(sender, target);

        if (FriendUtils.hasRequest(sender, target))
        {
            FriendUtils.acceptFriendRequest(sender, target);

            DesireCore.getLangHandler().sendRenderMessage(sender, "friend.accepted_friend_request", true, false, "{player}", target.getName());

            if (target.isOnline())
            {
                DesireCore.getLangHandler().sendRenderMessage(target, "friend.are_now_friend", true, false, "{player}", sender.getName());
            }
        }
        else
        {
            DesireCore.getLangHandler().sendRenderMessage(sender, "friend.sent_request", true, false, "{player}", target.getName());

            if (target.isOnline())
            {
                DesireCore.getLangHandler().sendRenderMessage(target, "friend.received_request", true, false, "{player}", sender.getName());
            }
        }
    }

    public static void denyFriend(Session sender, Session target)
    {
        FriendUtils.denyFriendRequest(sender, target);

        DesireCore.getLangHandler().sendRenderMessage(sender, "friend.denied_friend_request", true, false, "{player}", target.getName());
    }

    public static void list(Session sender)
    {
        List<UUID> friends = sender.getFriends();
        DesireCore.getLangHandler().sendRenderMessage(sender, "list-header", false, false);
        StringBuilder sb = new StringBuilder();
        sb.append(DesireCore.getLangHandler().renderMessage("friend.friends", false, false));

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
        DesireCore.getLangHandler().sendRenderMessage(sender, sb.toString().trim(), false, false);
        DesireCore.getLangHandler().sendRenderMessage(sender, "list-header", false, false);
    }

    public static void listIncomming(Session sender)
    {
        List<UUID> friends = sender.getIncomingFriendRequests();
        DesireCore.getLangHandler().sendRenderMessage(sender, "list-header", false, false);
        StringBuilder sb = new StringBuilder();
        sb.append(DesireCore.getLangHandler().renderMessage("friend.incoming_friends", false, false));

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
        DesireCore.getLangHandler().sendRenderMessage(sender, sb.toString().trim(), false, false);
        DesireCore.getLangHandler().sendRenderMessage(sender, "list-header", false, false);
    }

    public static void listOutgoing(Session sender)
    {
        List<UUID> friends = sender.getOutgoingFriendRequests();
        DesireCore.getLangHandler().sendRenderMessage(sender, "list-header", false, false);
        StringBuilder sb = new StringBuilder();
        sb.append(DesireCore.getLangHandler().renderMessage("friend.outgoing_friends", false, false));

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
        DesireCore.getLangHandler().sendRenderMessage(sender, sb.toString().trim(), false, false);
        DesireCore.getLangHandler().sendRenderMessage(sender, "list-header", false, false);
    }
}
