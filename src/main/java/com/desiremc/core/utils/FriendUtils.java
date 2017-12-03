package com.desiremc.core.utils;

import com.desiremc.core.session.Achievement;
import com.desiremc.core.session.Session;
import com.desiremc.core.session.SessionHandler;

public class FriendUtils
{

    /**
     * Remove a player's friend
     * 
     * @param player
     * @param target
     */
    public static void removeFriend(Session player, Session target)
    {
        player.getFriends().remove(target.getUniqueId());
        target.getFriends().remove(player.getUniqueId());

        saveRequests(player, target);
    }

    /**
     * Send a new friend request to a player.
     * 
     * @param player
     *            the player sending the request.
     * @param target
     *            the player recieving the request.
     */
    public static void addFriendRequest(Session player, Session target)
    {
        if (target.hasOutgoingFriendRequest(target.getUniqueId()))
        {
            acceptFriendRequest(player, target);
        }
        else
        {
            target.getIncomingFriendRequests().add(player.getUniqueId());
            player.getOutgoingFriendRequests().add(target.getUniqueId());
            saveRequests(player, target);
        }
    }

    /**
     * Accept a friend request from the target player.
     * 
     * @param player
     *            the player accepting the request.
     * @param target
     *            the person who originally sent the request.
     */
    public static void acceptFriendRequest(Session player, Session target)
    {
        player.getIncomingFriendRequests().remove(target.getUniqueId());
        target.getOutgoingFriendRequests().remove(player.getUniqueId());

        player.getOutgoingFriendRequests().remove(target.getUniqueId());
        target.getIncomingFriendRequests().remove(player.getUniqueId());

        addFriend(player, target);

        if (!target.hasAchievement(Achievement.FIRST_FRIEND))
        {
            target.awardAchievement(Achievement.FIRST_FRIEND, true);
        }

        if (!player.hasAchievement(Achievement.FIRST_FRIEND))
        {
            player.awardAchievement(Achievement.FIRST_FRIEND, true);
        }

        saveRequests(player, target);
    }

    /**
     * Deny a friend request from the target player.
     * 
     * @param player
     *            the player denying the request.
     * @param target
     *            the person who originally sent the request.
     */
    public static void denyFriendRequest(Session player, Session target)
    {
        player.getIncomingFriendRequests().remove(target.getUniqueId());
        target.getOutgoingFriendRequests().remove(player.getUniqueId());

        saveRequests(player, target);
    }

    /**
     * Checks if a player has a friend request incoming from another player.
     * 
     * @param player
     *            the person to check.
     * @param target
     *            the person who originally sent the request.
     * @return whether or not there is a pending request between the two.
     */
    public static boolean hasRequest(Session player, Session target)
    {
        boolean receiving = player.getIncomingFriendRequests().contains(target.getUniqueId());
        boolean sending = target.getOutgoingFriendRequests().contains(player.getUniqueId());

        if (receiving && sending)
        {
            return true;
        }
        else if (receiving ^ sending)
        {
            throw new IllegalStateException("Friend requests are out of sync for " + player.getName() + " and " + target.getName() + ".");
        }
        else
        {
            return false;
        }
    }

    /**
     * Checks if two players are friends with one another.
     * 
     * @param first
     *            the first player.
     * @param second
     *            the second player.
     * @return whether or not they are friends.
     */
    public static boolean areFriends(Session first, Session second)
    {
        boolean firstCheck = first.getFriends().contains(second.getUniqueId());
        boolean secondCheck = second.getFriends().contains(first.getUniqueId());

        if (firstCheck && secondCheck)
        {
            return true;
        }
        else if (firstCheck ^ secondCheck)
        {
            throw new IllegalStateException("Friends are out of sync for " + first.getName() + " and " + second.getName());
        }
        else
        {
            return false;
        }
    }

    private static void saveRequests(Session player, Session target)
    {
        SessionHandler.getInstance().save(player);
        SessionHandler.getInstance().save(target);
    }

    private static void addFriend(Session player, Session target)
    {
        player.getFriends().add(target.getUniqueId());
        target.getFriends().add(player.getUniqueId());

        if (!player.hasAchievement(Achievement.FIRST_FRIEND))
        {
            player.awardAchievement(Achievement.FIRST_FRIEND, true);
        }

        if (!target.hasAchievement(Achievement.FIRST_FRIEND))
        {
            target.awardAchievement(Achievement.FIRST_FRIEND, true);
        }
    }

}
