package com.desiremc.core.commands.settings;

import com.desiremc.core.api.newcommands.ValidBaseCommand;
import com.desiremc.core.session.SessionSetting;

public class SettingsCommand extends ValidBaseCommand
{

    public SettingsCommand()
    {
        super("settings", "Change your settings.", new String[] {"options", "prefs", "preferences", "setting"});

        for (SessionSetting setting : SessionSetting.values())
        {
            addSubCommand(new SettingsAbstractCommand(setting));
        }
    }

}