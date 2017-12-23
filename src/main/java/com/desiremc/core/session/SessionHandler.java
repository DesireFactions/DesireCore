package com.desiremc.core.session;

import com.desiremc.core.DesireCore;
import com.desiremc.core.punishment.Punishment;
import com.desiremc.core.punishment.PunishmentHandler;
import com.desiremc.core.utils.PlayerUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.mongodb.morphia.dao.BasicDAO;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class SessionHandler extends BasicDAO<Session, UUID>
{

    private static Session console;

    private static SessionHandler instance;

    private static HashMap<UUID, Session> sessions;

    private static HashMap<UUID, Session> onlineSessions;

    private static HashMap<UUID, Session> onlineStaff;

    public SessionHandler()
    {
        super(Session.class, DesireCore.getInstance().getMongoWrapper().getDatastore());

        DesireCore.getInstance().getMongoWrapper().getMorphia().map(Session.class);

        sessions = new HashMap<>();
        onlineSessions = new HashMap<>();
        onlineStaff = new HashMap<>();
    }

    private static boolean applyExternalData(Session session)
    {
        boolean needSave = false;
        if (session.getSettings() == null || session.getSettings().size() == 0)
        {
            session.assignDefaultSettings();
            needSave = true;
        }
        List<Punishment> punishments = PunishmentHandler.getInstance().createQuery()
                .field("punished").equal(session.getUniqueId())
                .field("repealed").notEqual(true)
                .field("expirationTime").greaterThan(System.currentTimeMillis())
                .asList();
        session.setActivePunishments(punishments);
        return needSave;
    }

    public static boolean updateSession(Session session)
    {
        Session database = getInstance().findOne("_id", session.getUniqueId());
        session.applyValues(database);
        return applyExternalData(session);
    }

    private static void startConsoleSession()
    {
        console = new Session();
        console.assignConsole();
    }

    /**
     * Checks if the given session is the console.
     * 
     * @param session the console to check.
     * @return {@code true} if the session is the console. Otherwise returns {@code false}.
     */
    public static boolean isConsole(Session session)
    {
        return session == console;
    }

    /**
     * @return the console's session instance.
     */
    public static Session getConsoleSession()
    {
        return console;
    }

    /**
     * Get the session by the given CommandSender. Will return null if the sender is neither a player or the console.
     * 
     * @param sender the command sender.
     * @return the session.
     */
    public static Session getSession(CommandSender sender)
    {
        if (sender instanceof Player)
        {
            return getOnlineSession(((Player) sender).getUniqueId());
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

    /**
     * @param uuid the uuid of the player
     * @return the online session.
     */
    public static Session getOnlineSession(UUID uuid)
    {
        return onlineSessions.get(uuid);
    }

    public static Session getGeneralSession(UUID uuid)
    {
        return sessions.get(uuid);
    }

    public static Session initializeSession(Player player)
    {
        UUID uuid = player.getUniqueId();
        Session session = sessions.get(uuid);

        boolean needSave = false;

        if (session == null)
        {
            session = createSession(uuid);
        }
        else
        {
            needSave = updateSession(session);
        }

        String ip = player.getAddress().getAddress().getHostAddress();
        if (!session.getIp().equalsIgnoreCase(ip))
        {
            session.getIpList().add(ip);
            session.setIp(ip);
            needSave = true;
        }

        if (!session.getName().equalsIgnoreCase(player.getName()))
        {
            session.getNameList().add(player.getName());
            session.setName(player.getName());
            needSave = true;
        }

        if (needSave)
        {
            SessionHandler.getInstance().save(session);
        }

        if (session.getRank().isStaff())
        {
            onlineStaff.put(session.getUniqueId(), session);
        }

        onlineSessions.put(session.getUniqueId(), session);

        return session;
    }

    public static Session findOfflinePlayerByName(String name)
    {
        for (Session session : sessions.values())
        {
            if (session.getName().equals(name))
            {
                return session;
            }
        }
        return null;
    }

    private static Session createSession(UUID uuid)
    {
        Player p = PlayerUtils.getPlayer(uuid);
        if (p == null)
        {
            return null;
        }

        Session session = new Session();
        session.assignDefaults(uuid, p.getName(), p.getAddress().getAddress().getHostAddress());
        session.save();

        sessions.put(uuid, session);

        return session;
    }

    /**
     * @return all connected sessions.
     */
    public static Iterable<Session> getSessions()
    {
        return sessions.values().stream().filter(x -> x.isOnline())::iterator;
    }

    /**
     * @return all online staff sessions excluding console.
     */
    public static Collection<Session> getOnlineStaff()
    {
        return onlineStaff.values();
    }

    /**
     * @return all online non-staff sessions.
     */
    public static Iterable<Session> getOnlineNonStaff()
    {
        LinkedList<Session> nonStaff = new LinkedList<>(onlineSessions.values());
        nonStaff.removeAll(onlineStaff.values());
        return nonStaff;
    }

    public static void removeStaff(UUID uuid)
    {
        onlineStaff.remove(uuid);
    }

    public static boolean endSession(Session session)
    {
        session.setTotalPlayed(session.getTotalPlayed() + System.currentTimeMillis() - session.getLastLogin());
        session.setLastLogin(System.currentTimeMillis());
        session.save();

        onlineStaff.remove(session.getUniqueId());
        onlineSessions.remove(session.getUniqueId());
        // TODO change return type
        return true;
    }

    public static SessionHandler getInstance()
    {
        return instance;
    }

    public static void initialize()
    {
        instance = new SessionHandler();

        for (Session session : instance.find())
        {
            updateSession(session);
            sessions.put(session.getUniqueId(), session);
            if (session.isOnline())
            {
                onlineSessions.put(session.getUniqueId(), session);
                if (session.getRank().isStaff())
                {
                    onlineStaff.put(session.getUniqueId(), session);
                }
            }
        }

        System.out.println("Session size: " + sessions.size());
        for (UUID uuid : sessions.keySet())
        {
            System.out.println(uuid);
        }
        System.out.println("=================");

        startConsoleSession();
    }

}
