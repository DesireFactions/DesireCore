package com.desiremc.core.listeners;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.LangHandler;
import com.desiremc.core.report.Report;
import com.desiremc.core.report.ReportHandler;
import com.desiremc.core.session.Session;
import com.desiremc.core.session.SessionHandler;
import com.desiremc.core.session.StaffHandler;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryListener implements Listener
{
    private LangHandler lang;

    public InventoryListener()
    {
        lang = DesireCore.getLangHandler();
    }

    @EventHandler
    public void onReportsClick(InventoryClickEvent event)
    {
        if (event.getInventory() == null || event.getClickedInventory() == null || event.getCurrentItem() == null
                || event.getCurrentItem().getType() == Material.AIR) return;
        Inventory inv = event.getClickedInventory();

        if (!lang.renderString(inv.getTitle()).equalsIgnoreCase(lang.renderString("report.inventory.title"))) return;

        if (event.getClick().equals(ClickType.DOUBLE_CLICK))
        {
            ItemStack item = event.getCurrentItem();
            for (Report report : ReportHandler.getInstance().getAllReports(false))
            {
                Session reported = SessionHandler.getSession(report.getReported());
                Session issuer = SessionHandler.getSession(report.getIssuer());

                if (!reported.getName().equalsIgnoreCase(item.getItemMeta().getDisplayName())) continue;
                if (!item.getItemMeta().getLore().contains(report.getReason())) continue;
                if (!item.getItemMeta().getLore().contains(issuer.getName())) continue;

                report.setResolved(true);
            }
        }
    }

    @EventHandler
    public void onButtonClick(InventoryClickEvent event)
    {
        if (event.getInventory() == null || event.getClickedInventory() == null || event.getCurrentItem() == null
                || event.getCurrentItem().getType() == Material.AIR) return;
        Inventory inv = event.getClickedInventory();

        if (!lang.renderString(inv.getTitle()).equalsIgnoreCase(lang.renderString("report.inventory.title"))) return;

        Player p = (Player) event.getView().getPlayer();

        if (event.getSlot() == 53)
        {
            p.closeInventory();
            StaffHandler.getInstance().addPage(p);
        }

        if (event.getSlot() == 45)
        {
            p.closeInventory();
            StaffHandler.getInstance().minusPage(p);
        }
    }
}