package com.desiremc.core.handler;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.desiremc.core.DesireCore;
import com.desiremc.core.session.Session;
import com.desiremc.core.session.SessionHandler;
import com.desiremc.core.staff.StaffHandler;
import com.desiremc.core.utils.DateUtils;
import com.desiremc.core.utils.cache.Cache;
import com.desiremc.core.utils.cache.RemovalListener;
import com.desiremc.core.utils.cache.RemovalNotification;

public class SlowChatHandler implements Listener
{

    private static int TIMER;

    private Cache<UUID, Long> history;

    public SlowChatHandler()
    {
        TIMER = DesireCore.getConfigHandler().getInteger("staff.chat_slow");
        history = new Cache<>(TIMER, TimeUnit.SECONDS, new RemovalListener<UUID, Long>()
        {
            @Override
            public void onRemoval(RemovalNotification<UUID, Long> entry)
            {
            }
        }, DesireCore.getInstance());
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onInteract(AsyncPlayerChatEvent event)
    {
        if (!StaffHandler.getInstance().isChatSlowed())
        {
            return;
        }

        Player player = event.getPlayer();
        Session session = SessionHandler.getOnlineSession(player.getUniqueId());

        if (session.getRank().isStaff())
        {
            return;
        }

        if (history.containsKey(player.getUniqueId()))
        {
            event.setCancelled(true);
            DesireCore.getLangHandler().sendRenderMessage(session, "staff.chat_slowed", true, false,
                    "{time}", DateUtils.formatDateDiff(history.get(player.getUniqueId())));
        }
        history.put(player.getUniqueId(), System.currentTimeMillis() + (TIMER * 1000));
    }

}
