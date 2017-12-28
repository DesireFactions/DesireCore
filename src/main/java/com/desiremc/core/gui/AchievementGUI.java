package com.desiremc.core.gui;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.material.MaterialData;

import com.desiremc.core.session.Achievement;
import com.desiremc.core.session.Session;

public class AchievementGUI extends Menu
{

    private Session session;

    public AchievementGUI(Session session)
    {
        super("Achievements", (Achievement.values().length + 8) / 9 * 9);
        this.session = session;
        addItems();
    }

    @SuppressWarnings("deprecation")
    private void addItems()
    {
        MenuItem item;
        int i = 0;
        for (Achievement a : Achievement.values())
        {

            if (session.getAchievements().contains(a))
            {
                item = new MenuItem("§4§lLOCKED: §4" + a.getName(), new MaterialData(Material.STAINED_GLASS_PANE, DyeColor.RED.getDyeData()))
                {
                    @Override
                    public void onClick(Player player)
                    {

                    }
                };
            }
            else
            {
                item = new MenuItem("FILLER")
                {

                    @Override
                    public void onClick(Player player)
                    {
                        // TODO Auto-generated method stub

                    }
                };
            }
            addMenuItem(item, i);
            i++;
        }
    }

}
