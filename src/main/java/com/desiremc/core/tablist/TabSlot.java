package com.desiremc.core.tablist;

public class TabSlot
{

    private TabList list;

    private String prefix, name, suffix;

    private int ping;

    public TabSlot(TabList list, String name)
    {
        this.list = list;

        this.name = name;

        this.ping = list.defaultPing;
    }

    /**
     * @return the list
     */
    public TabList getList()
    {
        return list;
    }

    /**
     * @param list the list to set
     */
    public void setList(TabList list)
    {
        this.list = list;
    }

    /**
     * @return the prefix
     */
    public String getPrefix()
    {
        return prefix;
    }

    /**
     * @param prefix the prefix to set
     */
    public void setPrefix(String prefix)
    {
        this.prefix = prefix;
    }

    /**
     * @return the name
     */
    public String getName()
    {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * @return the suffix
     */
    public String getSuffix()
    {
        return suffix;
    }

    /**
     * @param suffix the suffix to set
     */
    public void setSuffix(String suffix)
    {
        this.suffix = suffix;
    }

    /**
     * @return the ping
     */
    public int getPing()
    {
        return ping;
    }

    /**
     * @param ping the ping to set
     */
    public void setPing(int ping)
    {
        this.ping = ping;
    }

}