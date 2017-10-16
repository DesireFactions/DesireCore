package com.desiremc.core.utils;

import java.util.UUID;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.ConsoleCommandSender;

import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;
import com.desiremc.core.session.SessionHandler;

public class SessionUtils
{

    public static Rank getRank(Object o)
    {
        UUID uuid;
        if (o instanceof OfflinePlayer)
        {
            uuid = ((OfflinePlayer) o).getUniqueId();
        }
        else if (o instanceof UUID)
        {
            uuid = (UUID) o;
        }
        else if (o instanceof ConsoleCommandSender)
        {
            return Rank.OWNER;
        }
        else
        {
            return null;
        }
        Session s = SessionHandler.getSession(uuid);
        return s == null ? null : s.getRank();
    }

}
