package com.desiremc.core.listeners;

import com.desiremc.core.DesireCore;
import com.desiremc.core.commands.achievement.AchievementCommand;
import com.desiremc.core.commands.punishment.HistoryCommand;
import com.desiremc.core.commands.staff.StaffReportsCommand;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class GUIListener implements Listener
{

    @EventHandler
    public void onAchievementClickButton(InventoryClickEvent event)
    {
        if (event.getInventory() == null || event.getClickedInventory() == null || event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR)
        {
            return;
        }
        Inventory inv = event.getClickedInventory();

        if (!DesireCore.getLangHandler().renderString(inv.getTitle()).equalsIgnoreCase(DesireCore.getLangHandler().renderString("agui.inventory.title")))
        {
            return;
        }

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

    @EventHandler
    public void onPunishmentHistoryClick(InventoryClickEvent event)
    {
        if (event.getInventory() == null || event.getClickedInventory() == null || event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR)
        {
            return;
        }
        Inventory inv = event.getClickedInventory();

        if (!DesireCore.getLangHandler().renderString(inv.getTitle()).equalsIgnoreCase(DesireCore.getLangHandler().renderString("history.inventory.title")))
        {
            return;
        }

        event.setCancelled(true);

        Player p = (Player) event.getView().getPlayer();

        if (event.getSlot() == 53)
        {
            p.closeInventory();
            HistoryCommand.addPage(p.getUniqueId());
        }

        if (event.getSlot() == 45)
        {
            p.closeInventory();
            HistoryCommand.minusPage(p.getUniqueId());
        }
    }

    @EventHandler
    public void onReportClickButton(InventoryClickEvent event)
    {
        if (event.getInventory() == null || event.getClickedInventory() == null || event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR)
        {
            return;
        }
        Inventory inv = event.getClickedInventory();

        if (!DesireCore.getLangHandler().renderString(inv.getTitle()).equalsIgnoreCase(DesireCore.getLangHandler().renderString("report.inventory.title")))
        {
            return;
        }

        event.setCancelled(true);

        Player p = (Player) event.getView().getPlayer();

        if (event.getSlot() == 53)
        {
            p.closeInventory();
            StaffReportsCommand.addPage(p);
        }

        if (event.getSlot() == 45)
        {
            p.closeInventory();
            StaffReportsCommand.minusPage(p);
        }
    }
}
