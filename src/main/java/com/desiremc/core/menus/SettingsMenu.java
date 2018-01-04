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
            int slot = 10 + count;

            if (slot == 16)
            {
                slot += 2;
            }
            else if (slot == 24)
            {
                slot += 2;
            }
            else if (slot == 32)
            {
                slot += 2;
            }
            else if (slot == 40)
            {
                slot += 2;
            }

            if (setting.getRank().getId() <= session.getRank().getId())
            {
                addMenuItem(new MenuItem((session.getSetting(setting) ? "§a" : "§c") + setting.getDisplayName(), new MaterialData(Material.WOOL), (short) (session.getSetting(setting) ? 5 : 14))
                {

                    @Override
                    public void onClick(Player player)
                    {
                        if (setting.getRank().getId() <= session.getRank().getId())
                        {
                            closeMenu(player);
                            boolean value = session.toggleSetting(setting);
                            session.save();

                            DesireCore.getLangHandler().sendRenderMessage(session, "settings.toggle", true, false,
                                    "{setting}", setting.getDisplayName(),
                                    "{status}", (value ? "on" : "off"));
                        }
                    }
                }, slot);
                count++;
            }
        }
        for (int i = 0; i < 6 * 9; i++)
        {
            if (items[i] == null)
            {
                addMenuItem(new MenuItem(" ", new MaterialData(Material.STAINED_GLASS_PANE), (short) 15)
                {

                    @Override
                    public void onClick(Player player)
                    {
                    }
                }, i);
            }
        }
    }

}
