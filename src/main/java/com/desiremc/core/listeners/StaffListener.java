package com.desiremc.core.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.desiremc.core.staff.StaffHandler;

public class StaffListener implements Listener
{

    @EventHandler
    public void onInteract(PlayerInteractEvent event)
    {
        Player p = event.getPlayer();

        if (StaffHandler.getInstance().isCPSTested(p))
        {
            StaffHandler.getInstance().CPSTest(p);
        }

        if (!event.getAction().equals(Action.RIGHT_CLICK_AIR) && !event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
        {
            return;
        }

        if (!StaffHandler.getInstance().inStaffMode(p))
        {
            return;
        }

        if (handleInteraction(event.getItem(), event))
        {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEntityEvent event)
    {
        Player p = event.getPlayer();

        if (!StaffHandler.getInstance().inStaffMode(p))
        {
            return;
        }

        if (handleEntityInteraction(p.getItemInHand(), event))
        {
            event.setCancelled(true);
        }
    }

    private boolean handleInteraction(ItemStack item, PlayerInteractEvent event)
    {
        switch (item.getType())
        {
            case COMPASS:
                StaffHandler.getInstance().useLaunch(event);
                break;
            case EYE_OF_ENDER:
                StaffHandler.getInstance().useTeleport(event);
                break;
            case CLAY:
                StaffHandler.getInstance().useInvisibility(event);
                break;
            case PAPER:
                StaffHandler.getInstance().openReportsGUI(event.getPlayer());
                break;
            default:
                return false;
        }
        return true;
    }

    private boolean handleEntityInteraction(ItemStack item, PlayerInteractEntityEvent event)
    {
        switch (item.getType())
        {
            case BLAZE_ROD:
                StaffHandler.getInstance().useFreeze(event);
                break;
            case WATCH:
                StaffHandler.getInstance().useClicksPerSecond(event);
                break;
            case CHEST:
                StaffHandler.getInstance().useOpenInventory(event);
                break;
            case LEASH:
                StaffHandler.getInstance().useMount(event);
                break;
            default:
                return false;
        }
        return true;
    }
}
