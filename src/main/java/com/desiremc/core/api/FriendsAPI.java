package com.desiremc.core.api;

import com.desiremc.core.DesireCore;
import com.desiremc.core.session.Session;
import com.desiremc.core.session.SessionHandler;
import com.desiremc.core.utils.FriendUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

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
        LANG.sendRenderMessage(target, "friend.no_longer_friend", "{player}", sender.getName());
    }

    public static void addFriend(Session sender, Session target)
    {
        FriendUtils.addFriendRequest(sender, target);

        LANG.sendRenderMessage(sender, "friend.are_now_friend", "{player}", target.getName());
        LANG.sendRenderMessage(target, "friend.are_now_friend", "{player}", sender.getName());
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

    private static void listPlayers(CommandSender sender, List<Session> sessions)
    {
        LANG.sendString(sender, "list-header");

        for (Session s : sessions)
        {
            LANG.sendRenderMessage(sender, "player", "{player}", s.getName());
        }
    }

}
