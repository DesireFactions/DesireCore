package com.desiremc.core.commands;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.command.ValidCommand;
import com.desiremc.core.session.Rank;
import org.bukkit.command.CommandSender;

public class TeamSpeakCommand extends ValidCommand
{
    public TeamSpeakCommand(String name, String... aliases)
    {
        super(name, "List our TeamSpeak IP.", Rank.GUEST, new String[] {}, aliases);
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args)
    {
        DesireCore.getLangHandler().sendRenderMessage(sender, "teamspeak");
    }
}
