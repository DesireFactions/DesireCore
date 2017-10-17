package com.desiremc.core.commands.alerts;

import com.desiremc.core.api.command.ValidBaseCommand;
import com.desiremc.core.session.Rank;

public class AlertsCommand extends ValidBaseCommand
{

    public AlertsCommand()
    {
        super("alerts", "Alert tools", Rank.GUEST);
        addSubCommand(new AlertsMentionCommand());
        addSubCommand(new AlertsXrayCommand());
    }

}