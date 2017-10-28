package com.desiremc.core.tablist;

public class TabListOptions
{

    private boolean sendCreationMessage;

    public static TabListOptions getDefaultOptions()
    {
        return (new TabListOptions()).sendCreationMessage(true);
    }

    public boolean sendCreationMessage()
    {
        return this.sendCreationMessage;
    }

    public TabListOptions sendCreationMessage(boolean sendCreationMessage)
    {
        this.sendCreationMessage = sendCreationMessage;
        return this;
    }
}
