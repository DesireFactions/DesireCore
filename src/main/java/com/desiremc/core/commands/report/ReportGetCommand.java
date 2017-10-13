package com.desiremc.core.commands.report;

import com.desiremc.core.api.command.ValidCommand;
import com.desiremc.core.parsers.PlayerSessionParser;
import com.desiremc.core.report.Report;
import com.desiremc.core.report.ReportHandler;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;
import com.desiremc.core.session.SessionHandler;
import com.desiremc.core.utils.DateUtils;
import org.bukkit.command.CommandSender;

public class ReportGetCommand extends ValidCommand
{
    public ReportGetCommand()
    {
        super("get", "Get a players reports.", Rank.GUEST, new String[] { "target" });
        addParser(new PlayerSessionParser(), "target");
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args)
    {
        Session session = SessionHandler.getSession(sender);
        Session target = SessionHandler.getSession(args[0]);

        for (Report report : ReportHandler.getInstance().getAllReports(true))
        {
            if (!report.getReported().equals(target.getUniqueId()))
                continue;

            for (String msg : LANG.getStringList("report.getreport"))
            {
                session.getPlayer().sendMessage(LANG.renderString(msg,
                        "{date}", DateUtils.formatDateDiff(report.getIssued()),
                        "{player}", SessionHandler.getSession(report.getIssuer()).getName(),
                        "{reason}", report.getReason()));
            }
        }
    }
}
