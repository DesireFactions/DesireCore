package com.desiremc.core.commands;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.ValidCommand;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;

import java.util.List;

public class TeamSpeakCommand extends ValidCommand
{
    public TeamSpeakCommand()
    {
        super("teamspeak", "List our TeamSpeak IP.", Rank.GUEST, new String[] { "ts", "voip" });
    }

    @Override
    public void validRun(Session sender, String[] label, List<CommandArgument<?>> args)
    {
        DesireCore.getLangHandler().sendRenderMessage(sender, "teamspeak", true, false);
    }
}
