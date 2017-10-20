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
        if (uuid == null)
        {
            return null;
        }
        HCFSession session = null;
        if ((session = getInstance().sessions.get(uuid)) != null)
        {
            return session;
        }
        else
        {
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
        HCFSession session = getInstance().findOne("uuid", uuid);
        if (session == null)
        {
            session = createHCFSession(uuid);
        }
        session.setSession(SessionHandler.getSession(uuid));
        if (cache)
        {
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
