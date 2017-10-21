package com.desiremc.core.session;

import java.util.UUID;

import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.mongodb.morphia.dao.BasicDAO;

import com.desiremc.core.DesireCore;
import com.desiremc.core.utils.RedBlackTree;

public class HCFSessionHandler extends BasicDAO<HCFSession, UUID>
{

    private static HCFSession console;

    private static HCFSessionHandler instance;

    private RedBlackTree<UUID, HCFSession> sessions;

    public HCFSessionHandler()
    {
        super(HCFSession.class, DesireCore.getInstance().getMongoWrapper().getDatastore());

        DesireCore.getInstance().getMongoWrapper().getMorphia().map(HCFSession.class);

        sessions = new RedBlackTree<>();
        console = new HCFSession();
    }

    public static void initialize()
    {
        instance = new HCFSessionHandler();
    }

    public static Iterable<HCFSession> getSessions()
    {
        return getInstance().sessions.values();
    }

    /**
     * Gets the session of a user and initializes it if it does not yet exist.
     * 
     * @param o
     * @return
     */
    public static HCFSession getHCFSession(UUID uuid)
    {
        if (DesireCore.DEBUG)
        {
            System.out.println("getHCFSession(UUID) called with " + (uuid == null ? "null" : uuid.toString()) + ".");
        }
        if (uuid == null)
        {
            return null;
        }
        HCFSession session = getInstance().sessions.get(uuid);
        if (session != null)
        {
            if (DesireCore.DEBUG)
            {
                System.out.println("getHCFSession(UUID) found a logged in user.");
            }
            return session;
        }
        else
        {
            if (DesireCore.DEBUG)
            {
                System.out.println("getHCFSession(UUID) no user found.");
            }
            return initializeHCFSession(uuid, false);
        }

    }

    public static HCFSession getHCFSession(CommandSender sender)
    {
        if (sender instanceof Player)
        {
            return getHCFSession(((Player) sender).getUniqueId());
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

    public static HCFSession initializeHCFSession(UUID uuid, boolean cache)
    {
        if (DesireCore.DEBUG)
        {
            System.out.println("initializeHCFSession(UUID, boolean) called with values " + uuid.toString() + " and " + cache + ".");
        }
        HCFSession session = getInstance().findOne("_id", uuid);
        if (session == null)
        {
            if (DesireCore.DEBUG)
            {
                System.out.println("initializeHCFSession(UUID, boolean) HCFSession not found.");
            }
            session = createHCFSession(uuid);
        }
        if (DesireCore.DEBUG)
        {
            System.out.println("initializeHCFSession(UUID, boolean) setting base session.");
        }
        session.setSession(SessionHandler.getSession(uuid));
        if (cache)
        {
            if (DesireCore.DEBUG)
            {
                System.out.println("initializeHCFSession(UUID, boolean) caching HCFSession.");
            }
            getInstance().sessions.put(uuid, session);
        }
        return session;
    }

    public static boolean endSession(HCFSession s)
    {
        getInstance().save(s);
        getInstance().sessions.delete(s.getUniqueId());

        // TODO change this over
        return true;
    }

    private static HCFSession createHCFSession(UUID uuid)
    {
        HCFSession session = new HCFSession();
        session.assignDefault(uuid);
        getInstance().save(session);

        return session;
    }

    public static HCFSessionHandler getInstance()
    {
        if (instance == null)
        {
            initialize();
        }
        return instance;
    }

}
