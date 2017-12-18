package com.desiremc.core.utils;

import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;
import com.desiremc.core.session.SessionHandler;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.ConsoleCommandSender;

import java.util.UUID;

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
        else if (o instanceof Session)
        {
            return ((Session) o).getRank();
        }
        else
        {
            return null;
        }
        Session s = SessionHandler.getGeneralSession(uuid);
        return s == null ? null : s.getRank();
    }

}
