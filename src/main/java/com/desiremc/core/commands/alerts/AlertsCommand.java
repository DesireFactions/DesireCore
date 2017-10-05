package com.desiremc.core.commands.alerts;

import com.desiremc.core.api.command.ValidBaseCommand;
import com.desiremc.core.session.Rank;

public class AlertsCommand extends ValidBaseCommand
{

    public AlertsCommand()
    {
        super("alerts", "alert tools", Rank.ADMIN);
        addSubCommand(new AlertsMentionCommand());
        addSubCommand(new AlertsXrayCommand());
    }

}