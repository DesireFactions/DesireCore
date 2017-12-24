package com.desiremc.core.api;

import com.desiremc.core.DesireCore;
import com.desiremc.core.session.Session;
import com.desiremc.core.session.SessionHandler;

import java.util.UUID;

public class EconomyAPI
{

    public static int getTokens(UUID uuid)
    {
        Session session = SessionHandler.getGeneralSession(uuid);
        return session.getTokens();
    }

    public static void removeTokens(UUID uuid, int amount, boolean inform)
    {
        Session session = SessionHandler.getGeneralSession(uuid);
        setTokens(uuid, session.getTokens() - amount);
        if (inform)
        {
            DesireCore.getLangHandler().sendRenderMessage(session, "tokens.remove", true, false, "{tokens}", amount + "");
        }
    }

    public static void giveTokens(UUID uuid, int amount, boolean inform)
    {
        Session session = SessionHandler.getGeneralSession(uuid);
        setTokens(uuid, session.getTokens() + amount);
        if (inform)
        {
            DesireCore.getLangHandler().sendRenderMessage(session, "tokens.give", true, false, "{tokens}", amount + "");
        }
    }

    public static void setTokens(UUID uuid, int amount)
    {
        Session session = SessionHandler.getGeneralSession(uuid);
        session.setTokens(amount);
    }

    public static void setTokens(UUID uuid, int amount, boolean inform)
    {
        Session session = SessionHandler.getGeneralSession(uuid);
        session.setTokens(amount);
        if (inform)
        {
            DesireCore.getLangHandler().sendRenderMessage(session, "tokens.set", true, false, "{tokens}", amount + "");
        }
    }

}
