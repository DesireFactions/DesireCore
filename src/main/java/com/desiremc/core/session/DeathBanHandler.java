package com.desiremc.core.session;

import org.mongodb.morphia.dao.BasicDAO;

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
    
    public static DeathBan createDeathBan(HCFSession session)
    {
        DeathBan ban = new DeathBan(getNextId(), session);
        
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
