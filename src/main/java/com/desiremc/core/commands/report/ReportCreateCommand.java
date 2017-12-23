package com.desiremc.core.commands.report;

import java.util.List;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.CommandArgumentBuilder;
import com.desiremc.core.api.newcommands.ValidCommand;
import com.desiremc.core.parsers.SessionParser;
import com.desiremc.core.parsers.StringParser;
import com.desiremc.core.report.ReportHandler;
import com.desiremc.core.session.Session;

public class ReportCreateCommand extends ValidCommand
{

    public ReportCreateCommand()
    {
        super("create", "Create a new report.");

        addArgument(CommandArgumentBuilder.createBuilder(Session.class)
                .setName("target")
                .setParser(new SessionParser())
                .build());

        addArgument(CommandArgumentBuilder.createBuilder(String.class)
                .setName("reason")
                .setParser(new StringParser())
                .build());
    }

    @Override
    public void validRun(Session sender, String label[], List<CommandArgument<?>> args)
    {
        Session target = (Session) args.get(0).getValue();
        String reason = (String) args.get(1).getValue();

        ReportHandler.getInstance().submitReport(target.getUniqueId(), sender.getUniqueId(), reason);

        DesireCore.getLangHandler().sendRenderMessage(sender, "report.reported",
                "{player}", target.getName(),
                "{reason}", reason);
    }
}
