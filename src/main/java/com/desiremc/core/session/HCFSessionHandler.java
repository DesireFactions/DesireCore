package com.desiremc.core.session;

import java.util.UUID;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
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

        sessions = new RedBlackTree<>();
        console = new HCFSession();
    }

    public static void initialize()
    {
        instance = new HCFSessionHandler();
    }

    public static Iterable<HCFSession> getSessions()
    {
        return instance.sessions.values();
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
        if ((session = instance.sessions.get(uuid)) != null)
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
        if (sender instanceof OfflinePlayer)
        {
            return getHCFSession(((OfflinePlayer) sender).getUniqueId());
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
        HCFSession session = instance.findOne("uuid", uuid);
        if (session == null)
        {
            session = createHCFSession(uuid);
        }
        session.setSession(SessionHandler.getSession(uuid));
        if (cache)
        {
            instance.sessions.put(uuid, session);
        }
        return session;
    }

    public static boolean endSession(HCFSession s)
    {
        instance.save(s);
        instance.sessions.delete(s.getUniqueId());

        // TODO change this over
        return true;
    }

    private static HCFSession createHCFSession(UUID uuid)
    {
        HCFSession session = new HCFSession();
        session.setUniqueId(uuid);
        session.setSafeTimeLeft(DesireCore.getConfigHandler().getInteger("timers.pvp.time"));

        instance.save(session);

        return session;
    }

    public static HCFSessionHandler getInstance()
    {
        return instance;
    }

}
