package com.desiremc.core.commands.report;

import org.bukkit.command.CommandSender;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.command.ValidCommand;
import com.desiremc.core.report.ReportHandler;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;
import com.desiremc.core.session.SessionHandler;

public class ReportCreateCommand extends ValidCommand
{
    public ReportCreateCommand()
    {
        super("create", "Create a new report.", Rank.GUEST, new String[] { "target", "reason" });
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args)
    {
        Session session = SessionHandler.getSession(sender);
        Session target = (Session) args[0];
        StringBuilder sb = new StringBuilder();

        if (args.length >= 2)
        {
            for (int i = 2; i < args.length; i++)
            {
                sb.append(args[i] + " ");
            }
        }

        ReportHandler.getInstance().submitReport(target.getUniqueId(), session != null ? session.getUniqueId() : DesireCore.getConsoleUUID(), sb.toString().trim());

        LANG.sendRenderMessage(session, "report.reported",
                "{player}", target.getName(),
                "{reason}", sb.toString().trim());
    }
}
