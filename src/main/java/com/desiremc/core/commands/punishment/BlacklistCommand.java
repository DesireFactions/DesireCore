package com.desiremc.core.commands.punishment;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.LangHandler;
import com.desiremc.core.api.command.ValidCommand;
import com.desiremc.core.parsers.PlayerSessionParser;
import com.desiremc.core.parsers.StringParser;
import com.desiremc.core.punishment.Punishment;
import com.desiremc.core.punishment.Punishment.Type;
import com.desiremc.core.punishment.PunishmentHandler;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;
import com.desiremc.core.session.SessionHandler;
import com.desiremc.core.validators.PlayerValidator;
import com.desiremc.core.validators.SenderNotTargetValidator;
import com.desiremc.core.validators.SenderOutranksTargetValidator;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public class BlacklistCommand extends ValidCommand
{

    private static final LangHandler LANG = DesireCore.getLangHandler();

    public BlacklistCommand()
    {
        super("blacklist", "Blacklist a user from the server.", Rank.ADMIN, ARITY_OPTIONAL, new String[] {"target", "reason"});

        addParser(new PlayerSessionParser(), "target");
        addParser(new StringParser(), "reason");

        addValidator(new PlayerValidator());
        addValidator(new SenderNotTargetValidator(), "target");
        addValidator(new SenderOutranksTargetValidator(), "target");
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args)
    {
        Session session = SessionHandler.getSession(sender);
        Session target = (Session) args[0];

        if (((String) args[1]).contains("-s"))
        {
            args[1] = ((String) args[1]).replace("-s", "");
        }
        else
        {
            Bukkit.broadcastMessage(LANG.renderMessage("blacklist.blacklist_message", "{player}", sender.getName(), "target}", target.getName(), "{reason}", args[1]));
        }

        Punishment punishment = new Punishment();
        punishment.setIssued(System.currentTimeMillis());
        punishment.setType(Type.BAN);
        punishment.setPunished(target.getUniqueId());
        punishment.setIssuer(session != null ? session.getUniqueId() : DesireCore.getConsoleUUID());
        punishment.setReason((String) args[1]);
        punishment.setBlacklisted(true);
        PunishmentHandler.getInstance().save(punishment);

        PunishmentHandler.getInstance().refreshPunishments(target);

        if (target.getOfflinePlayer() != null && target.getOfflinePlayer().isOnline())
        {
            target.getPlayer().kickPlayer(("\n" + "&c&lYou are permanently blacklisted from the network!\n" + "&cReason: &7{reason}\n" + "&cBanned By: &7{issuer}\n" + "&7Visit &ehttps://desirehcf.com/rules&7 for our terms and rules").replace("{reason}", (String) args[1]).replace("{issuer}", session.getName()).replace("&", "§"));
        }
    }
}
