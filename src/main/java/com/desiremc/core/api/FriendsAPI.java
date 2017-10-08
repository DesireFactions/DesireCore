package com.desiremc.core.api;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.desiremc.core.DesireCore;
import com.desiremc.core.session.Session;
import com.desiremc.core.session.SessionHandler;
import com.desiremc.core.utils.FriendUtils;

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
        FriendUtils.removeFriend(target, sender);

        LANG.sendRenderMessage(sender, "friend.no_longer_friend", "{player}", sender.getName());
        LANG.sendRenderMessage(sender, "friend.no_longer_friend", "{player}", target.getName());
    }

    public static void addFriend(Session sender, Session target)
    {
        FriendUtils.addFriendRequest(target, sender);

        LANG.sendRenderMessage(sender, "friend.are_now_friend", "{player}", sender.getName());
        LANG.sendRenderMessage(sender, "friend.are_now_friend", "{player}", target.getName());
    }

    public static void denyFriend(Session sender, Session target)
    {
        FriendUtils.denyFriendRequest(target, sender);

        LANG.sendRenderMessage(sender, "friend.denied_friend_request", "{player}", sender.getName());
    }

    public static void list(Player sender, Session target)
    {
        listPlayers(sender, target.getFriends());
    }

    public static void listIncomming(Player sender)
    {
        listPlayers(sender, SessionHandler.getSession(sender).getIncomingFriendRequests());
    }

    public static void listOutgoing(Player sender)
    {
        listPlayers(sender, SessionHandler.getSession(sender).getIncomingFriendRequests());
    }

    private static void listPlayers(CommandSender sender, List<Session> sessions)
    {
        LANG.sendString(sender, "list-header");

        for (Session s : sessions)
        {
            LANG.sendRenderMessage(sender, "player", "{player}", s.getName());
        }
    }

}
