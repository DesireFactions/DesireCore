package com.desiremc.core.commands.alerts;

import org.bukkit.command.CommandSender;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.command.ValidCommand;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;
import com.desiremc.core.session.SessionHandler;
import com.desiremc.core.validators.PlayerValidator;

public class AlertsXrayCommand extends ValidCommand
{

    public AlertsXrayCommand()
    {
        super("Xray", "Toggle ore break notifications on and off.", Rank.JRMOD, new String[] {});
        addValidator(new PlayerValidator());
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args)
    {
        Session session = SessionHandler.getSession(sender);

        session.getSettings().toggleXrayNotifications();

        DesireCore.getLangHandler().sendString(sender, "alerts.xray." + (session.getSettings().hasXrayNotification() ? "on" : "off"));
    }
}
