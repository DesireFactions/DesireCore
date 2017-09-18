package com.desiremc.core.utils;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;
import com.desiremc.core.session.SessionHandler;

public class SessionUtils {

    public static Rank getRank(Object o) {
        OfflinePlayer op;
        if (o instanceof Player) {
            op = (Player) o;
        } else if (o instanceof UUID) {
            op = Bukkit.getOfflinePlayer((UUID) o);
        } else if (o instanceof ConsoleCommandSender) {
            return Rank.OWNER;
        } else {
            return null;
        }
        Session s = SessionHandler.getSession(op);
        return s == null ? null : s.getRank();
    }

}
