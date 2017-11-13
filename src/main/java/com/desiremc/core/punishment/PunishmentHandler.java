package com.desiremc.core.punishment;

import com.desiremc.core.DesireCore;
import com.desiremc.core.session.Session;
import org.mongodb.morphia.dao.BasicDAO;

import java.util.List;

public class PunishmentHandler extends BasicDAO<Punishment, Long>
{

    private static PunishmentHandler instance;

    public PunishmentHandler()
    {
        super(Punishment.class, DesireCore.getInstance().getMongoWrapper().getDatastore());
        DesireCore.getInstance().getMongoWrapper().getMorphia().map(Punishment.class);
    }

    public static void initialize()
    {
        instance = new PunishmentHandler();
    }

    public void refreshPunishments(Session session)
    {
        List<Punishment> punishments = PunishmentHandler.getInstance().createQuery()
                .field("punished").equal(session.getUniqueId())
                .field("repealed").notEqual(true)
                .field("expirationTime").greaterThan(System.currentTimeMillis())
                .asList();
        session.setActivePunishments(punishments);
    }

    public static PunishmentHandler getInstance()
    {
        return instance;
    }

}
