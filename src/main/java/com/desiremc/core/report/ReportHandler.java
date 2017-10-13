package com.desiremc.core.report;

import java.util.List;
import java.util.UUID;

import org.mongodb.morphia.dao.BasicDAO;

import com.desiremc.core.DesireCore;

public class ReportHandler extends BasicDAO<Report, Integer>
{
    private static ReportHandler instance;

    public ReportHandler()
    {
        super(Report.class, DesireCore.getInstance().getMongoWrapper().getDatastore());
    }

    public static void initialize()
    {
        instance = new ReportHandler();
    }

    public void submitReport(UUID target, UUID issuer, String reason)
    {
        Report report = new Report();

        report.setIssued(System.currentTimeMillis());
        report.setIssuer(issuer);
        report.setReported(target);
        report.setReason(reason);
        save(report);
    }

    public static ReportHandler getInstance()
    {
        return instance;
    }

    public List<Report> getAllReports(boolean removeResolved)
    {
        List<Report> reports = find().asList();

        if (removeResolved)
        {
            reports.removeIf(report -> report.isResolved());
        }

        return reports;
    }

    public void clearReports(UUID target)
    {
        for (Report report : getAllReports(false))
        {
            if (report.getReported().equals(target))
            {
                report.setResolved(true);
            }
        }
    }
}
