package com.desiremc.core.commands.report;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.CommandArgumentBuilder;
import com.desiremc.core.api.newcommands.ValidCommand;
import com.desiremc.core.parsers.SessionParser;
import com.desiremc.core.report.ReportHandler;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;

import java.util.List;

public class ReportClearCommand extends ValidCommand
{

    public ReportClearCommand()
    {
        super("clear", "Clear a players reports", Rank.ADMIN);

        addArgument(CommandArgumentBuilder.createBuilder(Session.class)
                .setName("target")
                .setParser(new SessionParser())
                .build());
    }

    @Override
    public void validRun(Session sender, String label[], List<CommandArgument<?>> args)
    {
        Session target = (Session) args.get(0).getValue();

        ReportHandler.getInstance().clearReports(target.getUniqueId());

        DesireCore.getLangHandler().sendRenderMessage(sender, "report.clear", true, false,
                "{player}", target.getName());
    }
}
