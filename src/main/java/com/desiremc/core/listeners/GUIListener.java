package com.desiremc.core.listeners;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.desiremc.core.DesireCore;
import com.desiremc.core.commands.achievement.AchievementCommand;
import com.desiremc.core.commands.punishment.HistoryCommand;
import com.desiremc.core.commands.staff.StaffReportsCommand;
import com.desiremc.core.report.Report;
import com.desiremc.core.report.ReportHandler;
import com.desiremc.core.session.Session;
import com.desiremc.core.session.SessionHandler;
import com.desiremc.core.staff.StaffHandler;

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

        if (inv.getTitle().equalsIgnoreCase(DesireCore.getLangHandler().renderMessage("history.inventory.title", false, false)))
        {
            event.setCancelled(true);
            return;
        }

        if (!inv.getTitle().equalsIgnoreCase(DesireCore.getLangHandler().renderMessage("report.inventory.title", false, false)))
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

    @EventHandler
    public void onReportsClick(InventoryClickEvent event)
    {
        if (event.getInventory() == null || event.getClickedInventory() == null || event.getCurrentItem() == null
                || event.getCurrentItem().getType() == Material.AIR)
        {
            return;
        }

        Inventory inv = event.getClickedInventory();

        if (!inv.getTitle().equalsIgnoreCase(DesireCore.getLangHandler().renderMessage("report.inventory.title", false, false)))
        {
            return;
        }

        event.setCancelled(true);

        Player p = (Player) event.getWhoClicked();

        if (event.getClick().equals(ClickType.DOUBLE_CLICK))
        {
            ItemStack item = event.getCurrentItem();
            for (Report report : ReportHandler.getInstance().getAllReports(true))
            {
                Session reported = SessionHandler.getGeneralSession(report.getReported());
                Session issuer = SessionHandler.getGeneralSession(report.getIssuer());

                if (!reported.getName().equalsIgnoreCase(item.getItemMeta().getDisplayName())) continue;
                if (!listContainsString(item.getItemMeta().getLore(), report.getReason())) continue;
                if (!listContainsString(item.getItemMeta().getLore(), issuer.getName())) continue;

                report.setResolved(true);
                p.closeInventory();
                StaffHandler.getInstance().openReportsGUI(p, StaffReportsCommand.getPage(p));
                break;
            }
        }
    }

    private boolean listContainsString(List<String> list, String string)
    {
        for (String s : list)
        {
            if (s.contains(string))
            {
                return true;
            }
        }
        return false;
    }
}
