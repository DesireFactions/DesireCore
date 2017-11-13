package com.desiremc.core.commands.alerts;

import org.bukkit.command.CommandSender;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.command.ValidCommand;
import com.desiremc.core.session.Session;
import com.desiremc.core.session.SessionHandler;
import com.desiremc.core.session.SessionSetting;
import com.desiremc.core.validators.PlayerValidator;

public class SettingsAbstractCommand extends ValidCommand
{

    private SessionSetting setting;

    public SettingsAbstractCommand(SessionSetting setting)
    {
        super(setting.name().toLowerCase(), "Toggle " + setting.getDisplayName() + " on and off", setting.getRank(), new String[] {}, setting.getAliases());

        addValidator(new PlayerValidator());
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args)
    {
        Session session = SessionHandler.getSession(sender);

        boolean value = session.toggleSetting(setting);
        SessionHandler.getInstance().save(session);

        DesireCore.getLangHandler().sendRenderMessage(sender, "settings.toggle",
                "{setting}", setting.getDisplayName(),
                "{status}", (value ? "on" : "off"));
    }

}
