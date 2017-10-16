package com.desiremc.core.commands.alerts;

import org.bukkit.command.CommandSender;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.command.ValidCommand;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;
import com.desiremc.core.session.SessionHandler;
import com.desiremc.core.validators.PlayerValidator;

public class AlertsMentionCommand extends ValidCommand
{

    public AlertsMentionCommand()
    {
        super("Mention", "Toggle mentions on and off.", Rank.GUEST, new String[] {});
        addValidator(new PlayerValidator());
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args)
    {
        Session session = SessionHandler.getSession(sender);

        session.getSettings().toggleMentions();

        DesireCore.getLangHandler().sendString(sender, "alerts.mention." + (session.getSettings().hasXrayNotification() ? "on" : "off"));
    }
}
