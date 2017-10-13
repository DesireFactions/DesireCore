package com.desiremc.core.commands.alerts;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.LangHandler;
import com.desiremc.core.api.command.ValidCommand;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;
import com.desiremc.core.session.SessionHandler;
import com.desiremc.core.validators.PlayerValidator;
import org.bukkit.command.CommandSender;

public class AlertsMentionCommand extends ValidCommand
{

    private static final LangHandler LANG = DesireCore.getLangHandler();

    public AlertsMentionCommand()
    {
        super("mention", "toggle mentions", Rank.ADMIN, new String[]{});
        addValidator(new PlayerValidator());
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args)
    {
        Session session = SessionHandler.getSession(sender);

        if (session.getMentionStatus())
        {
            session.setMentionStatus(false);
            LANG.sendRenderMessage(session, "alerts.mention.off");
        }
        else
        {
            session.setMentionStatus(true);
            LANG.sendRenderMessage(session, "alerts.mention.on");
        }
    }
}
