package com.desiremc.core.commands.report;

import org.bukkit.command.CommandSender;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.LangHandler;
import com.desiremc.core.api.command.ValidCommand;
import com.desiremc.core.parsers.PlayerSessionParser;
import com.desiremc.core.parsers.StringParser;
import com.desiremc.core.report.ReportHandler;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;
import com.desiremc.core.session.SessionHandler;
import com.desiremc.core.validators.PlayerValidator;

public class ReportCreateCommand extends ValidCommand
{

    private static final LangHandler LANG = DesireCore.getLangHandler();

    public ReportCreateCommand()
    {
        super("create", "Create a new report.", Rank.GUEST, new String[] {"target", "reason"});
        addParser(new PlayerSessionParser(), "target");
        addParser(new StringParser(), "reason");
        addValidator(new PlayerValidator());
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args)
    {
        Session session = SessionHandler.getSession(sender);
        Session target = (Session) args[0];
        StringBuilder sb = new StringBuilder();

        if (args.length >= 1)
        {
            for (int i = 1; i < args.length; i++)
            {
                sb.append(args[i] + " ");
            }
        }

        ReportHandler.getInstance().submitReport(target.getUniqueId(), session != null ? session.getUniqueId() :
                DesireCore.getConsoleUUID(), sb.toString().trim());

        LANG.sendRenderMessage(session, "report.reported",
                "{player}", target.getName(),
                "{reason}", sb.toString().trim());
    }
}
