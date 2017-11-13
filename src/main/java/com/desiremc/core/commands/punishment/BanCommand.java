package com.desiremc.core.commands.punishment;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.LangHandler;
import com.desiremc.core.api.command.ValidCommand;
import com.desiremc.core.parsers.PlayerSessionParser;
import com.desiremc.core.parsers.StringParser;
import com.desiremc.core.punishment.Punishment.Type;
import com.desiremc.core.punishment.PunishmentHandler;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;
import com.desiremc.core.session.SessionHandler;
import com.desiremc.core.validators.SenderNotTargetValidator;
import com.desiremc.core.validators.SenderOutranksTargetValidator;
import org.bukkit.command.CommandSender;

public class BanCommand extends ValidCommand
{

    private static final LangHandler LANG = DesireCore.getLangHandler();

    public BanCommand()
    {
        super("ban", "Permanently ban a user from the server.", Rank.MODERATOR, ValidCommand.ARITY_REQUIRED_VARIADIC,
                new String[] {"target", "reason"});
        addParser(new PlayerSessionParser(), "target");
        addParser(new StringParser(), "reason");

        addValidator(new SenderNotTargetValidator(), "target");
        addValidator(new SenderOutranksTargetValidator(), "target");
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args)
    {
        Session session = SessionHandler.getSession(sender);
        Session target = (Session) args[0];

        PunishmentHandler.getInstance().issuePunishment(Type.BAN, target.getUniqueId(), session != null ? session
                .getUniqueId() : DesireCore.getConsoleUUID(), (String) args[1]);

        LANG.sendRenderMessage(sender, "ban.permban_message",
                "{player}", target.getName(),
                "{reason}", args[1]);

        if (target.getOfflinePlayer() != null && target.getOfflinePlayer().isOnline())
        {
            target.getPlayer().kickPlayer((DesireCore.getLangHandler().getPrefix() + "\n" + "\n" + "&c&lYou are permanently banned from" +
                    " the network!\n"
                    + "\n" + "&cReason: &7{reason}\n" + "&cBanned By: &7{issuer}\n"
                    + "\n" + "&7Visit &ehttps://desirehcf.net/rules&7 for our terms and rules")
                    .replace("{reason}", (String) args[1])
                    .replace("{issuer}", session.getName())
                    .replace("&", "§"));
        }
    }
}
