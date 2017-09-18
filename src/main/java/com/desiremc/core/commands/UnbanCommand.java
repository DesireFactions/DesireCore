package com.desiremc.core.commands;

import org.bukkit.command.CommandSender;

import com.desiremc.core.api.command.ValidCommand;
import com.desiremc.core.parsers.PlayerSessionParser;
import com.desiremc.core.punishment.PunishmentHandler;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;
import com.desiremc.core.validators.PlayerIsBannedValidator;

public class UnbanCommand extends ValidCommand
{

    public UnbanCommand()
    {
        super("unban", "Unban a user from the server.", Rank.MODERATOR, new String[] { "target" });
        addParser(new PlayerSessionParser(), "target");
        addValidator(new PlayerIsBannedValidator());
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args)
    {
        Session target = (Session) args[0];

        LANG.sendRenderMessage(sender, "ban.unban_message", "{player}", target.getName());
        target.isBanned().setRepealed(true);
        PunishmentHandler.getInstance().save(target.isBanned());
    }

}
