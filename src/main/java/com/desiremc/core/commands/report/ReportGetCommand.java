package com.desiremc.core.commands.report;

import java.util.List;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.CommandArgumentBuilder;
import com.desiremc.core.api.newcommands.ValidCommand;
import com.desiremc.core.parsers.SessionParser;
import com.desiremc.core.report.Report;
import com.desiremc.core.report.ReportHandler;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;
import com.desiremc.core.session.SessionHandler;
import com.desiremc.core.utils.DateUtils;

public class ReportGetCommand extends ValidCommand
{

    public ReportGetCommand()
    {
        super("get", "Get a players reports.", Rank.HELPER);

        addArgument(CommandArgumentBuilder.createBuilder(Session.class)
                .setName("target")
                .setParser(new SessionParser())
                .build());
    }

    @Override
    public void validRun(Session sender, String label[], List<CommandArgument<?>> args)
    {
        Session target = (Session) args.get(0).getValue();

        for (Report report : ReportHandler.getInstance().getAllReports(true))
        {
            if (!report.getReported().equals(target.getUniqueId()))
            {
                continue;
            }

            for (String msg : DesireCore.getLangHandler().getStringList("report.getreport"))
            {
                sender.sendMessage(DesireCore.getLangHandler().renderString(msg,
                        "{date}", DateUtils.formatDateDiff(report.getIssued()),
                        "{player}", SessionHandler.getGeneralSession(report.getIssuer()).getName(),
                        "{reason}", report.getReason()));
            }
        }
    }
}
