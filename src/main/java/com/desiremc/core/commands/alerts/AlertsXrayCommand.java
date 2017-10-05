package com.desiremc.core.commands.alerts;

import com.desiremc.core.api.command.ValidCommand;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;
import com.desiremc.core.session.SessionHandler;
import com.desiremc.core.validators.PlayerValidator;
import org.bukkit.command.CommandSender;

public class AlertsXrayCommand extends ValidCommand
{

    public AlertsXrayCommand()
    {
        super("xray", "toggle xrays", Rank.ADMIN, new String[]{});
        addValidator(new PlayerValidator());
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args)
    {
        Session session = SessionHandler.getSession(sender);

        if (session.getXrayStatus())
        {
            session.setXrayStatus(false);
            LANG.sendRenderMessage(session, "alerts.xray.off");
        }
        else
        {
            session.setXrayStatus(true);
            LANG.sendRenderMessage(session, "alerts.xray.on");
        }
    }
}
