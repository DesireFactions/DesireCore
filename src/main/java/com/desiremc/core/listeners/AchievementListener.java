package com.desiremc.core.listeners;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.LangHandler;
import com.desiremc.core.commands.achievement.AchievementCommand;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class AchievementListener implements Listener
{

    private LangHandler lang;

    public AchievementListener()
    {
        lang = DesireCore.getLangHandler();
    }

    @EventHandler
    public void onAchievementClickButton(InventoryClickEvent event)
    {
        if (event.getInventory() == null || event.getClickedInventory() == null || event.getCurrentItem() == null
                || event.getCurrentItem().getType() == Material.AIR) return;
        Inventory inv = event.getClickedInventory();

        if (!lang.renderString(inv.getTitle()).equalsIgnoreCase(lang.renderString("agui.inventory.title"))) return;

        event.setCancelled(true);

        Player p = (Player) event.getView().getPlayer();

        if (event.getSlot() == 53)
        {
            p.closeInventory();
            AchievementCommand.addPage(p.getUniqueId());
        }

        if (event.getSlot() == 45)
        {
            p.closeInventory();
            AchievementCommand.minusPage(p.getUniqueId());
        }
    }
}
