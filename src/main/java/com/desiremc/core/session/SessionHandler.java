package com.desiremc.core.session;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.ConsoleCommandSender;
import org.mongodb.morphia.dao.BasicDAO;

import com.desiremc.core.DesireCore;
import com.desiremc.core.punishment.PunishmentHandler;

public class SessionHandler extends BasicDAO<Session, UUID>
{

    private static Session console;

    private static SessionHandler instance;

    private List<Session> sessions;

    public SessionHandler()
    {
        super(Session.class, DesireCore.getInstance().getMongoWrapper().getDatastore());

        sessions = new LinkedList<>();
    }

    public static Session getSession(Object o)
    {
        Session session = null;
        if (o instanceof OfflinePlayer || o instanceof UUID)
        {
            for (Session s : instance.sessions)
            {
                if (s.getUniqueId().equals(o instanceof OfflinePlayer ? ((OfflinePlayer) o).getUniqueId() : o))
                {
                    return s;
                }
            }
            session = initializeSession(o, false);
        }
        else if (o instanceof String)
        {
            String name = (String) o;
            for (Session s : instance.sessions)
            {
                if (s.getName().equalsIgnoreCase(name))
                {
                    return s;
                }
            }
            List<Session> results = instance.createQuery().field("name").equal(name).asList();
            if (results.size() == 1)
            {
                return results.get(0);
            }
        }
        else if (o instanceof ConsoleCommandSender)
        {
            session = console;
        }
        return session;
    }

    public static Session initializeSession(Object o, boolean cache)
    {
        Session session = instance.findOne("uuid", o instanceof OfflinePlayer ? ((OfflinePlayer) o).getUniqueId() : o);
        if (session == null)
        {
            session = createSession(o);
        }
        if (cache)
        {
            instance.sessions.add(session);
        }
        session.setActivePunishments(PunishmentHandler.getInstance().createQuery().field("punished").equal(session.getUniqueId()).field("expirationTime").greaterThan(Long.valueOf(System.currentTimeMillis())).asList());
        return session;
    }

    private static Session createSession(Object o)
    {
        OfflinePlayer op;
        if (o instanceof OfflinePlayer)
        {
            op = (OfflinePlayer) o;
        }
        else if (o instanceof UUID)
        {
            op = Bukkit.getOfflinePlayer((UUID) o);
        }
        else
        {
            return null;
        }
        if (op == null)
        {
            return null;
        }

        Session session = new Session();
        session.setUniqueId(op.getUniqueId());
        session.setName(op.getName());
        session.setRank(Rank.GUEST);
        session.setFirstLogin(System.currentTimeMillis());
        session.setLastLogin(System.currentTimeMillis());
        session.setTotalPlayed(0);
        session.setIp("10.0.0.1");
        session.setFriends(new ArrayList<>());

        instance.save(session);

        return session;
    }

    public List<Session> getSessions()
    {
        return sessions;
    }

    public static boolean endSession(Session s)
    {
        instance.save(s);
        return instance.sessions.remove(s);
    }

    public static SessionHandler getInstance()
    {
        return instance;
    }

    public static void initialize()
    {
        instance = new SessionHandler();
    }
    
}
