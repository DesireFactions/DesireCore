package com.desiremc.core.commands.chat;

import com.desiremc.core.api.newcommands.ValidBaseCommand;

public class ChatCommand extends ValidBaseCommand
{
    public ChatCommand()
    {
        super("chat", "staff chat tools");
        addSubCommand(new ChatClearCommand());
        addSubCommand(new ChatToggleCommand());
        addSubCommand(new ChatSlowCommand());
    }
}
