package com.desiremc.core.commands.settings;

import java.util.List;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.CommandArgumentBuilder;
import com.desiremc.core.api.newcommands.ValidCommand;
import com.desiremc.core.menus.SettingsMenu;
import com.desiremc.core.parsers.SessionSettingParser;
import com.desiremc.core.session.Session;
import com.desiremc.core.session.SessionSetting;

public class SettingsCommand extends ValidCommand
{

    public SettingsCommand()
    {
        super("settings", "Change your settings.", true, new String[] { "options", "prefs", "preferences", "setting" });

        addArgument(CommandArgumentBuilder.createBuilder(SessionSetting.class)
                .setName("setting")
                .setParser(new SessionSettingParser())
                .setOptional()
                .build());
    }

    @Override
    public void validRun(Session sender, String[] label, List<CommandArgument<?>> arguments)
    {
        if (!arguments.get(0).hasValue())
        {
            new SettingsMenu(sender).openMenu(sender.getPlayer());
        }
        else
        {
            SessionSetting setting = (SessionSetting) arguments.get(0).getValue();
            boolean value = sender.toggleSetting(setting);
            sender.save();

            DesireCore.getLangHandler().sendRenderMessage(sender, "settings.toggle", true, false,
                    "{setting}", setting.getDisplayName(),
                    "{status}", (value ? "on" : "off"));
        }
    }

}