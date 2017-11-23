package com.desiremc.core.tablistfive;

public abstract class TabHandler
{

    public void update()
    {
        clear();
        send();
    }

    public abstract void setSlot(int slot, String name);
        
    public abstract void clear();

    public abstract void send();

    public abstract void setHeader(String header);

    public abstract void setFooter(String footer);

}
