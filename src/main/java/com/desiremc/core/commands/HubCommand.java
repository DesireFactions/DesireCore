package com.desiremc.core.commands;

import java.util.List;

import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.ValidCommand;
import com.desiremc.core.session.Session;
import com.desiremc.core.utils.BungeeUtils;

public class HubCommand extends ValidCommand
{

    protected HubCommand()
    {
        super("hub", "Sends you to the hub.", true);
    }

    @Override
    public void validRun(Session sender, String[] label, List<CommandArgument<?>> arguments)
    {
        BungeeUtils.sendToHub(sender.getPlayer());
    }

}
