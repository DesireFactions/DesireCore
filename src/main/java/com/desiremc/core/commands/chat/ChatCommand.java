package com.desiremc.core.commands.chat;

import com.desiremc.core.api.command.ValidBaseCommand;
import com.desiremc.core.session.Rank;

public class ChatCommand extends ValidBaseCommand
{
    public ChatCommand()
    {
        super("chat", "staff chat tools", Rank.ADMIN);
        addSubCommand(new ChatClearCommand());
        addSubCommand(new ChatToggleCommand());
    }
}
