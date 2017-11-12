package com.desiremc.core.session;

import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Property;

@Embedded
public class SessionSettings
{

    @Property("mentions_enabled")
    private boolean mentionsEnabled;

    @Property("xray_enabled")
    private boolean findOreNotifications;

    @Property("tablist_classic")
    private boolean tablistClassic;

    public boolean hasMentionsEnabled()
    {
        return mentionsEnabled;
    }

    public boolean hasFindOreNotifications()
    {
        return findOreNotifications;
    }

    public void toggleMentions()
    {
        mentionsEnabled = !mentionsEnabled;
    }

    public void toggleFindOreNotifications()
    {
        findOreNotifications = !findOreNotifications;
    }
    
    public boolean hasClassicTablist()
    {
        return tablistClassic;
    }
    
    public void toggleClassicTablist()
    {
        tablistClassic = !tablistClassic;
    }

}
