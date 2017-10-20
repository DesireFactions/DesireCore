package com.desiremc.core.session;

import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Property;

@Embedded
public class SessionSettings
{

    @Property("mentions_enabled")
    private boolean mentionsEnabled;

    @Property("xray_enabled")
    private boolean xrayNotifications;
    
    public boolean hasMentionsEnabled()
    {
        return mentionsEnabled;
    }

    public boolean hasXrayNotification()
    {
        return xrayNotifications;
    }

    public void toggleMentions()
    {
        mentionsEnabled = !mentionsEnabled;
    }

    public void toggleXrayNotifications()
    {
        xrayNotifications = !xrayNotifications;
    }

}
