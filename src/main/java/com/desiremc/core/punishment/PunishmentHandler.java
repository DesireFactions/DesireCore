package com.desiremc.core.punishment;

import java.util.UUID;

import org.mongodb.morphia.dao.BasicDAO;

import com.desiremc.core.DesireCore;
import com.desiremc.core.punishment.Punishment.Type;
import com.desiremc.core.utils.PlayerUtils;

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

    public void issuePunishment(Type type, UUID punished, UUID issuer, long time, String reason)
    {
        Punishment punishment = new Punishment();
        punishment.setIssued(System.currentTimeMillis());
        punishment.setType(type);
        punishment.setPunished(punished);
        punishment.setIssuer(issuer);
        punishment.setExpirationTime(time + System.currentTimeMillis());
        punishment.setReason(reason);
        save(punishment);

        if (PlayerUtils.getPlayer(punished) != null)
        {
            if (type == Punishment.Type.BAN)
            {
                PlayerUtils.getPlayer(punished).kickPlayer(DesireCore.getLangHandler().renderMessage("punishment.ban"));
            }
            else if (type == Punishment.Type.MUTE)
            {
                PlayerUtils.getPlayer(punished).sendMessage(DesireCore.getLangHandler().renderMessage("punishment.mute"));
            }
        }
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
