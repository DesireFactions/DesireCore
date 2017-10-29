package com.desiremc.core.report;

import com.desiremc.core.session.SessionHandler;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Indexed;

import java.util.UUID;

@Entity(noClassnameStored = true, value = "reports")
public class Report
{

    @Id
    private int id;

    @Indexed
    private UUID reported;

    @Indexed
    private UUID issuer;

    private long issued;

    private boolean resolved;

    private String reason;

    public UUID getReported()
    {
        return reported;
    }

    public void setReported(UUID reported)
    {
        this.reported = reported;
        save();
    }

    public UUID getIssuer()
    {
        return issuer;
    }

    public void setIssuer(UUID issuer)
    {
        this.issuer = issuer;
        save();
    }

    public long getIssued()
    {
        return issued;
    }

    public void setIssued(long issued)
    {
        this.issued = issued;
        save();
    }

    public void setResolved(boolean resolved)
    {
        this.resolved = resolved;
        save();
    }

    public boolean isResolved()
    {
        return resolved;
    }

    public String getReason()
    {
        return reason;
    }

    public void setReason(String reason)
    {
        this.reason = reason;
        save();
    }

    private void save()
    {
        ReportHandler.getInstance().save(this);
    }

}
