package com.desiremc.core.commands.punishment;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.LangHandler;
import com.desiremc.core.api.command.ValidCommand;
import com.desiremc.core.parsers.PlayerSessionParser;
import com.desiremc.core.punishment.Punishment;
import com.desiremc.core.punishment.PunishmentHandler;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;
import com.desiremc.core.validators.PlayerIsMutedValidator;
import com.desiremc.core.validators.PlayerIsNotBlacklistedValidator;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public class UnmuteCommand extends ValidCommand
{

    private static final LangHandler LANG = DesireCore.getLangHandler();

    public UnmuteCommand()
    {
        super("unmute", "Unmute a user on the server.", Rank.MODERATOR, new String[] {"target"});
        addParser(new PlayerSessionParser(), "target");

        addValidator(new PlayerIsMutedValidator(), "target");
        addValidator(new PlayerIsNotBlacklistedValidator(), "target");
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args)
    {
        Session target = (Session) args[0];

        Punishment p = target.isMuted();
        p.setRepealed(true);
        PunishmentHandler.getInstance().save(p);
        PunishmentHandler.getInstance().refreshPunishments(target);

        Bukkit.broadcastMessage(LANG.renderMessage("mute.unmute_message", "{target}", target.getName(), "{player}", sender.getName()));

        if (target.getOfflinePlayer() != null && target.getOfflinePlayer().isOnline())
        {
            LANG.sendRenderMessage(target, "mute.unmute_message_target", "{player}", sender.getName());
        }
    }

}
