package com.desiremc.core.menus;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.material.MaterialData;

import com.desiremc.core.DesireCore;
import com.desiremc.core.gui.Menu;
import com.desiremc.core.gui.MenuItem;
import com.desiremc.core.session.Session;
import com.desiremc.core.session.SessionSetting;

public class SettingsMenu extends Menu
{

    public SettingsMenu(Session session)
    {
        super("§cSettings", 6);

        int count = 0;
        for (SessionSetting setting : SessionSetting.enabledValues())
        {
            if (setting.getRank().getId() < session.getRank().getId())
            {
                addMenuItem(new MenuItem(setting.getDisplayName(), new MaterialData(Material.WOOL), (short) (session.getSetting(setting) ? 5 : 14))
                {

                    @Override
                    public void onClick(Player player)
                    {
                        if (setting.getRank().getId() < session.getRank().getId())
                        {
                            closeMenu(player);
                            boolean value = session.toggleSetting(setting);
                            session.save();

                            DesireCore.getLangHandler().sendRenderMessage(session, "settings.toggle", true, false,
                                    "{setting}", setting.getDisplayName(),
                                    "{status}", (value ? "on" : "off"));
                        }
                    }
                }, 10 + count);
                count++;
            }
        }
    }

}
