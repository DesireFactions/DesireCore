package com.desiremc.core.session;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.mongodb.morphia.dao.BasicDAO;

import com.desiremc.core.DesireCore;
import com.desiremc.core.punishment.PunishmentHandler;
import com.desiremc.core.utils.RedBlackTree;

public class SessionHandler extends BasicDAO<Session, UUID>
{

    private static Session console;

    private static SessionHandler instance;

    private RedBlackTree<UUID, Session> sessions;

    private RedBlackTree<UUID, Session> staff;

    public SessionHandler()
    {
        super(Session.class, DesireCore.getInstance().getMongoWrapper().getDatastore());

        sessions = new RedBlackTree<>();
        staff = new RedBlackTree<>();
    }

    public static Session getSession(CommandSender sender)
    {
        if (sender instanceof Player)
        {
            return getSession(((Player) sender).getUniqueId());
        }
        else if (sender instanceof ConsoleCommandSender)
        {
            return console;
        }
        else
        {
            return null;
        }
    }

    public static Session getSession(UUID uuid)
    {
        Session session = null;

        if ((session = instance.sessions.get(uuid)) != null)
        {
            return session;
        }
        return initializeSession(uuid, false);
    }

    public static Session initializeSession(UUID uuid, boolean cache)
    {
        Session session = instance.findOne("uuid", uuid);
        if (session == null)
        {
            session = createSession(uuid);
        }
        if (cache)
        {
            instance.sessions.put(uuid, session);
            if (session.getRank().isStaff())
            {
                instance.staff.put(uuid, session);
            }
        }
        session.setActivePunishments(PunishmentHandler.getInstance().createQuery().field("punished").equal(session.getUniqueId()).field("expirationTime").greaterThan(Long.valueOf(System.currentTimeMillis())).asList());
        return session;
    }

    public static Session findOfflinePlayerByName(String name)
    {
        Session session = instance.findOne("name", name);
        if (session == null)
        {
            return null;
        }

        session.setActivePunishments(PunishmentHandler.getInstance().createQuery().field("punished").equal(session.getUniqueId()).field("expirationTime").greaterThan(Long.valueOf(System.currentTimeMillis())).asList());
        return session;
    }

    private static Session createSession(Object o)
    {
        Player p;
        if (o instanceof Player)
        {
            p = (Player) o;
        }
        else if (o instanceof UUID)
        {
            p = Bukkit.getPlayer((UUID) o);
        }
        else
        {
            return null;
        }
        if (p == null)
        {
            return null;
        }

        Session session = new Session();
        session.setUniqueId(p.getUniqueId());
        session.setName(p.getName());
        session.setRank(Rank.GUEST);
        session.setFirstLogin(System.currentTimeMillis());
        session.setLastLogin(System.currentTimeMillis());
        session.setTotalPlayed(0);
        session.setIp(p.getAddress().getAddress().getHostAddress());

        instance.save(session);

        return session;
    }

    public Iterable<Session> getSessions()
    {
        return sessions.values();
    }

    public Iterable<Session> getStaff()
    {
        return staff.values();
    }

    public void removeStaff(UUID uuid)
    {
        staff.delete(uuid);
    }

    public static boolean endSession(Session s)
    {
        instance.save(s);
        instance.sessions.delete(s.getUniqueId());
        // TODO change this return type
        return true;
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
