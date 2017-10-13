package com.desiremc.core.commands.report;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.LangHandler;
import com.desiremc.core.api.command.ValidCommand;
import com.desiremc.core.parsers.PlayerSessionParser;
import com.desiremc.core.report.ReportHandler;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;
import com.desiremc.core.session.SessionHandler;
import org.bukkit.command.CommandSender;

public class ReportClearCommand extends ValidCommand
{

    private static final LangHandler LANG = DesireCore.getLangHandler();

    public ReportClearCommand()
    {
        super("clear", "Clear a players reports", Rank.ADMIN, new String[]{"target"});
        addParser(new PlayerSessionParser(), "target");
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args)
    {
        Session target = SessionHandler.getSession(args[0]);
        Session player = SessionHandler.getSession(sender);

        ReportHandler.getInstance().clearReports(target.getUniqueId());

        LANG.sendRenderMessage(player, "report.clear", "{player}", target.getName());
    }
}
