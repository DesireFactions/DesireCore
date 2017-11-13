package com.desiremc.core.punishment;

import com.desiremc.core.DesireCore;
import com.desiremc.core.punishment.Punishment.Type;
import com.desiremc.core.session.Session;
import com.desiremc.core.session.SessionHandler;
import org.mongodb.morphia.dao.BasicDAO;

import java.util.List;
import java.util.UUID;

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

    public Punishment issuePunishment(Type type, UUID punished, UUID issuer, long time, String reason)
    {
        Punishment punishment = new Punishment();
        punishment.setIssued(System.currentTimeMillis());
        punishment.setType(type);
        punishment.setPunished(punished);
        punishment.setIssuer(issuer);
        punishment.setExpirationTime(time);
        punishment.setReason(reason);
        save(punishment);

        Session session = SessionHandler.getSession(punishment.getPunished());

        refreshPunishments(session);

        return punishment;
    }

    public Punishment issuePunishment(Type type, UUID punished, UUID issuer, String reason)
    {
        Punishment punishment = new Punishment();
        punishment.setIssued(System.currentTimeMillis());
        punishment.setType(type);
        punishment.setPunished(punished);
        punishment.setIssuer(issuer);
        punishment.setReason(reason);
        punishment.setPermanent(true);
        save(punishment);

        Session session = SessionHandler.getSession(punishment.getPunished());

        refreshPunishments(session);

        return punishment;
    }

    public Punishment issuePunishment(Type type, UUID punished, UUID issuer, String reason, boolean blacklist)
    {
        Punishment punishment = new Punishment();
        punishment.setIssued(System.currentTimeMillis());
        punishment.setType(type);
        punishment.setPunished(punished);
        punishment.setIssuer(issuer);
        punishment.setReason(reason);
        punishment.setBlacklisted(blacklist);
        save(punishment);

        Session session = SessionHandler.getSession(punishment.getPunished());

        refreshPunishments(session);

        return punishment;
    }

    public void processPunishment(Punishment punishment)
    {
        if (punishment.getType() == Type.BAN)
        {

        }
    }

    public void refreshPunishments(Session session)
    {
        List<Punishment> punishments = PunishmentHandler.getInstance().createQuery()
                .field("punished").equal(session.getUniqueId())
                .field("repealed").notEqual(true)
                .field("expirationTime").greaterThan(Long.valueOf(System.currentTimeMillis()))
                .asList();
        session.setActivePunishments(punishments);
    }

    public Punishment getPunishment(UUID uuid)
    {
        Punishment punishment = findOne("uuid", uuid);
        if (punishment != null)
        {
            System.out.println(DesireCore.getLangHandler().renderMessage("punishment.found", "{player}", uuid + ""));
            System.out.println(DesireCore.getLangHandler().renderMessage("punishment.type", "{reason}", punishment.getReason()));
            return punishment;
        }

        System.out.println(DesireCore.getLangHandler().renderMessage("punishment.notfound", "{player}", uuid + ""));
        return null;
    }

    public static PunishmentHandler getInstance()
    {
        return instance;
    }

}
