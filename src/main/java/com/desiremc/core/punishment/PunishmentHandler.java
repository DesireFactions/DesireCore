package com.desiremc.core.punishment;

import com.desiremc.core.DesireCore;
import com.desiremc.core.punishment.Punishment.Type;
import com.desiremc.core.session.Session;
import com.desiremc.core.session.SessionHandler;
import com.desiremc.core.utils.DateUtils;
import com.desiremc.core.utils.PlayerUtils;
import com.desiremc.core.utils.StringUtils;
import org.bukkit.ChatColor;
import org.mongodb.morphia.dao.BasicDAO;

import java.util.ArrayList;
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

    public void refreshPunishments(Session session)
    {
        List<Punishment> punishments = PunishmentHandler.getInstance().createQuery()
                .field("punished").equal(session.getUniqueId())
                .field("repealed").equal(false)
                .field("expirationTime").greaterThan(System.currentTimeMillis())
                .asList();
        session.setActivePunishments(punishments);
    }

    public Punishment getPunishment(UUID uuid, Punishment.Type type)
    {
        List<Punishment> punishments = PunishmentHandler.getInstance().createQuery()
                .field("punished").equal(uuid)
                .field("repealed").equal(false)
                .field("type").equal(type).asList();

        if (punishments == null || punishments.size() == 0)
        {
            return null;
        }

        punishments.removeIf(punishment -> ((punishment.getExpirationTime() + punishment.getIssued()) < System.currentTimeMillis()) && !punishment.isPermanent());

        return punishments.get(0);
    }

    public List<Punishment> getPunishments(UUID uuid)
    {
        return PunishmentHandler.getInstance().createQuery().field("punished").equal(uuid).asList();
    }

    public List<String> getAllIpBans()
    {
        List<String> ips = new ArrayList<>();

        List<Punishment> punishments = PunishmentHandler.getInstance().createQuery()
                .field("repealed").equal(false)
                .field("type").equal(Type.IP_BAN).asList();

        for (Punishment p : punishments)
        {
            Session session = SessionHandler.getSession(p.getPunished());
            ips.add(session.getIp());
        }

        return ips;
    }

    public Punishment getPunishment(String ip)
    {
        List<Punishment> punishments = PunishmentHandler.getInstance().createQuery()
                .field("repealed").equal(false)
                .field("type").equal(Type.IP_BAN).asList();

        for (Punishment p : punishments)
        {
            Session session = SessionHandler.getSession(p.getPunished());
            if (session.getIp().equalsIgnoreCase(ip))
            {
                return p;
            }
        }
        return null;
    }

    public List<Punishment> getAllPunishments(UUID issuer, long time)
    {
        List<Punishment> punishments = PunishmentHandler.getInstance().createQuery()
                .field("repealed").equal(false)
                .field("issuer").equal(issuer)
                .asList();

        punishments.removeIf(punishment -> punishment.getIssued() < time);

        return punishments;
    }

    public static PunishmentHandler getInstance()
    {
        return instance;
    }

    public String[] getMouseOverDetails(Session session)
    {
        Punishment punishment;

        if (session.isIPBanned() != null)
        {
            punishment = session.isIPBanned();
        }
        else if (session.isBanned() != null)
        {
            punishment = session.isBanned();
        }
        else if (session.isMuted() != null)
        {
            punishment = session.isMuted();
        }
        else
        {
            punishment = null;
        }


        if (punishment == null)
        {
            return new String[] {
                    ChatColor.DARK_RED + "" + ChatColor.BOLD + "NO PUNISHMENT"
            };
        }
        else
        {
            return new String[] {
                    ChatColor.DARK_RED + "" + ChatColor.BOLD + "Punishment Info",
                    ChatColor.GRAY + "Type: " + ChatColor.YELLOW + StringUtils.capitalize(punishment.getType().name().replace("_", " ").toLowerCase()),
                    ChatColor.GRAY + "Expires in: " + ChatColor.YELLOW + (punishment.isPermanent() ? "Permanent" : DateUtils.formatDateDiff(punishment.getExpirationTime())),
                    ChatColor.GRAY + "Punished by: " + ChatColor.YELLOW + PlayerUtils.getPlayer(punishment.getIssuer()).getName()
            };
        }
    }

}
