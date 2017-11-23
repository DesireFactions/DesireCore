package com.desiremc.core.commands.punishment;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.LangHandler;
import com.desiremc.core.api.command.ValidCommand;
import com.desiremc.core.parsers.PlayerSessionParser;
import com.desiremc.core.punishment.Punishment;
import com.desiremc.core.punishment.PunishmentHandler;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;
import com.desiremc.core.validators.PlayerIsBannedValidator;
import com.desiremc.core.validators.PlayerIsBlacklistedValidator;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;


public class UnblacklistCommand extends ValidCommand
{
    private static final LangHandler LANG = DesireCore.getLangHandler();

    public UnblacklistCommand()
    {
        super("unblacklist", "Unblacklist a user from the server.", Rank.DEVELOPER, new String[] {"target"});
        addParser(new PlayerSessionParser(), "target");
        addValidator(new PlayerIsBannedValidator(), "target");
        addValidator(new PlayerIsBlacklistedValidator(), "target");
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args)
    {
        Session target = (Session) args[0];

        Bukkit.broadcastMessage(LANG.renderMessage("blacklist.unblacklist_message", "{target}", target.getName(), "{player}", sender.getName()));

        for (Punishment punishment : target.getActivePunishments())
        {
            punishment.setBlacklisted(false);
        }

        PunishmentHandler.getInstance().save(target.isBanned());
        PunishmentHandler.getInstance().refreshPunishments(target);
    }
}
