package com.desiremc.core.session;

import org.bukkit.entity.Player;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.query.Query;

import com.desiremc.core.DesireCore;

public class DeathBanHandler extends BasicDAO<DeathBan, Integer>
{

    private static DeathBanHandler instance;

    private static int nextId = 0;

    protected DeathBanHandler()
    {
        super(DeathBan.class, DesireCore.getInstance().getMongoWrapper().getDatastore());
        instance = this;

        DesireCore.getInstance().getMongoWrapper().getMorphia().map(DeathBan.class);

        if (count() > 0)
        {
            nextId = findOne(createQuery().order("-_id")).getId() + 1;
        }
    }

    public static DeathBan getDeathBan(Session s, String server)
    {
        Query<DeathBan> q = getInstance().createQuery()
                .field("player").equal(s.getUniqueId())
                .field("server").equal(server)
                .field("revived").equal(false)
                .field("staffRevive").equal(false)
                .field("startTime").greaterThan(System.currentTimeMillis() - s.getRank().getDeathBanTime());

        long count = q.count();
        if (count == 0)
        {
            return null;
        }
        else if (count == 1)
        {
            return q.get();
        }
        else
        {
            throw new IllegalStateException();
        }
    }

    public static DeathBan createDeathBan(Player player)
    {
        DeathBan ban = new DeathBan(getNextId(), player.getUniqueId());

        getInstance().save(ban);

        return ban;
    }

    public static int getNextId()
    {
        return nextId++;
    }

    public static DeathBanHandler getInstance()
    {
        if (instance == null)
        {
            instance = new DeathBanHandler();
        }

        return instance;
    }

}
