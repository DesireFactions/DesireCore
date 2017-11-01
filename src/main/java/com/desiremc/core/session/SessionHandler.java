package com.desiremc.core.session;

import java.util.List;
import java.util.UUID;

import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.mongodb.morphia.dao.BasicDAO;

import com.desiremc.core.DesireCore;
import com.desiremc.core.punishment.Punishment;
import com.desiremc.core.punishment.Punishment.Type;
import com.desiremc.core.punishment.PunishmentHandler;
import com.desiremc.core.utils.PlayerUtils;
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

        DesireCore.getInstance().getMongoWrapper().getMorphia().map(Session.class);
        
        sessions = new RedBlackTree<>();
        staff = new RedBlackTree<>();

        startConsoleSession();
    }

    private static void startConsoleSession()
    {
        console = new Session();
        console.assignConsole();
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
        if (DesireCore.DEBUG)
        {
            System.out.println("getSession(UUID) called with value " + uuid.toString() + ".");
        }
        Session session = instance.sessions.get(uuid);

        if (session != null)
        {
            if (DesireCore.DEBUG)
            {
                System.out.println("getSession(UUID) found a logged in user.");
            }
            return session;
        }
        if (DesireCore.DEBUG)
        {
            System.out.println("getSession(UUID) did not find a logged in user.");
        }
        return initializeSession(uuid, false);
    }

    public static Session initializeSession(UUID uuid, boolean cache)
    {
        if (DesireCore.DEBUG)
        {
            System.out.println("initializeSession(UUID, boolean) called with values " + uuid.toString() + " and " + cache + ".");
        }
        Session session = instance.findOne("_id", uuid);
        if (session == null)
        {
            if (DesireCore.DEBUG)
            {
                System.out.println("initializeSession(UUID, boolean) non-existing player.");
            }
            session = createSession(uuid);
        }
        if (cache)
        {
            if (DesireCore.DEBUG)
            {
                System.out.println("initializeSession(UUID, boolean) cached player in sessions.");
            }
            instance.sessions.put(uuid, session);
            if (session.getRank().isStaff())
            {
                if (DesireCore.DEBUG)
                {
                    System.out.println("initializeSession(UUID, boolean) is staff member.");
                }
                instance.staff.put(uuid, session);
            }
        }
        List<Punishment> punishments = PunishmentHandler.getInstance().createQuery()
                .field("punished").equal(uuid)
                .field("expirationTime").greaterThan(Long.valueOf(System.currentTimeMillis()))
                .asList();
        session.setActivePunishments(punishments);
        if (DesireCore.DEBUG)
        {
            System.out.println("initializeSession(UUID, boolean) set punishments and returned.");
        }
        return session;
    }
    
    public static Punishment getBan(UUID uuid)
    {
        List<Punishment> punishments = PunishmentHandler.getInstance().createQuery()
                .field("punished").equal(uuid)
                .field("expirationTime").greaterThan(Long.valueOf(System.currentTimeMillis()))
                .field("type").equal(Type.BAN).asList();
        if (punishments == null || punishments.size() == 0)
        {
            return null;
        }
        return punishments.get(0);
    }

    public static Session findOfflinePlayerByName(String name)
    {
        Session session = instance.findOne("name", name);
        if (session == null)
        {
            return null;
        }

        List<Punishment> punishments = PunishmentHandler.getInstance().createQuery()
                .field("punished").equal(session.getUniqueId())
                .field("expirationTime").greaterThan(Long.valueOf(System.currentTimeMillis()))
                .asList();

        session.setActivePunishments(punishments);

        return session;
    }

    private static Session createSession(UUID uuid)
    {
        if (DesireCore.DEBUG)
        {
            System.out.println("createSession(UUID) called with value " + uuid.toString() + ".");
        }
        Player p = PlayerUtils.getPlayer(uuid);
        if (p == null)
        {
            if (DesireCore.DEBUG)
            {
                System.out.println("createSession(UUID) could not find player.");
            }
            return null;
        }

        if (DesireCore.DEBUG)
        {
            System.out.println("createSession(UUID) set defaults.");
        }
        Session session = new Session();
        session.assignDefaults(uuid, p.getName(), p.getAddress().getAddress().getHostAddress());

        if (DesireCore.DEBUG)
        {
            System.out.println("createSession(UUID) saved to database.");
        }
        instance.save(session);

        if (DesireCore.DEBUG)
        {
            System.out.println("createSession(UUID) returned.");
        }
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
        s.setTotalPlayed(s.getTotalPlayed() + System.currentTimeMillis() - s.getLastLogin());
        s.setLastLogin(System.currentTimeMillis());
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
