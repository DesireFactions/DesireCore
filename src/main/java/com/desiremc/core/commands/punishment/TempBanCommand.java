package com.desiremc.core.commands.punishment;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.LangHandler;
import com.desiremc.core.api.command.ValidCommand;
import com.desiremc.core.parsers.PlayerSessionParser;
import com.desiremc.core.parsers.StringParser;
import com.desiremc.core.parsers.TimeParser;
import com.desiremc.core.punishment.Punishment;
import com.desiremc.core.punishment.Punishment.Type;
import com.desiremc.core.punishment.PunishmentHandler;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;
import com.desiremc.core.session.SessionHandler;
import com.desiremc.core.utils.DateUtils;
import com.desiremc.core.validators.PlayerValidator;
import com.desiremc.core.validators.PunishmentTimeValidator;
import com.desiremc.core.validators.SenderNotTargetValidator;
import com.desiremc.core.validators.SenderOutranksTargetValidator;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public class TempBanCommand extends ValidCommand
{

    private static final LangHandler LANG = DesireCore.getLangHandler();

    public TempBanCommand()
    {
        super("tempban", "Temporarily ban a user from the server.", Rank.JRMOD, ValidCommand
                .ARITY_REQUIRED_VARIADIC, new String[] {"target", "time", "reason"});

        addParser(new PlayerSessionParser(), "target");
        addParser(new TimeParser(), "time");
        addParser(new StringParser(), "reason");

        addValidator(new PlayerValidator());
        addValidator(new SenderNotTargetValidator(), "target");
        addValidator(new SenderOutranksTargetValidator(), "target");
        addValidator(new PunishmentTimeValidator(), "time");
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args)
    {
        Session session = SessionHandler.getSession(sender);
        Session target = (Session) args[0];
        long time = (long) args[1];

        Punishment punishment = new Punishment();
        punishment.setIssued(System.currentTimeMillis());
        punishment.setType(Type.BAN);
        punishment.setPunished(target.getUniqueId());
        punishment.setExpirationTime(time);
        punishment.setIssuer(session != null ? session.getUniqueId() : DesireCore.getConsoleUUID());
        punishment.setReason((String) args[2]);
        PunishmentHandler.getInstance().save(punishment);

        PunishmentHandler.getInstance().refreshPunishments(target);

        Bukkit.broadcastMessage(LANG.renderMessage("ban.tempban_message", "{duration}", DateUtils.formatDateDiff(time), "{player}", sender.getName(), "{target}", target.getName(), "{reason}", args[2]));

        if (target.getOfflinePlayer() != null && target.getOfflinePlayer().isOnline())
        {
            target.getPlayer().kickPlayer(("\n" + "&c&lYou are banned from the network!\n" + "&cReason: &7{reason}\n" + "&cUntil: &7{until}\n" + "&cBanned By: &7{issuer}\n" + "&7Visit &ehttps://desirehcf.com/rules&7 for our terms and rules").replace("{reason}", (String) args[2]).replace("{until}", DateUtils.formatDateDiff(time)).replace("{issuer}",
                    session.getName()).replace("&", "§"));
        }
    }

}
