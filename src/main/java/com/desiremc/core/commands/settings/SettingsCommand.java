package com.desiremc.core.commands.settings;

import com.desiremc.core.api.command.ValidBaseCommand;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.SessionSetting;

public class SettingsCommand extends ValidBaseCommand
{

    public SettingsCommand()
    {
        super("settings", "Change your settings.", Rank.GUEST, new String[] {"options", "prefs", "preferences"});

        for (SessionSetting setting : SessionSetting.values())
        {
            addSubCommand(new SettingsAbstractCommand(setting));
        }
    }

}