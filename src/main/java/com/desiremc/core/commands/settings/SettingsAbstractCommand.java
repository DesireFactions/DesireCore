package com.desiremc.core.commands.settings;

import java.util.List;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.ValidCommand;
import com.desiremc.core.session.Session;
import com.desiremc.core.session.SessionHandler;
import com.desiremc.core.session.SessionSetting;

public class SettingsAbstractCommand extends ValidCommand
{

    private SessionSetting setting;

    public SettingsAbstractCommand(SessionSetting setting)
    {
        super(setting.name().toLowerCase(),                           // name
                "Turn " + setting.getDisplayName() + " on and off",   // description
                setting.getRank(),                                    // rank                                        
                true,                                                 // players only
                setting.getAliases());                                // aliases

        this.setting = setting;
    }

    @Override
    public void validRun(Session sender, String label[], List<CommandArgument<?>> args)
    {
        boolean value = sender.toggleSetting(setting);
        SessionHandler.getInstance().save(sender);

        DesireCore.getLangHandler().sendRenderMessage(sender, "settings.toggle",
                "{setting}", setting.getDisplayName(),
                "{status}", (value ? "on" : "off"));
    }

}
