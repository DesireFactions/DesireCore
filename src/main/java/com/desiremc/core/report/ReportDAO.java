package com.desiremc.core.report;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;

import java.util.UUID;

public class ReportDAO extends BasicDAO<Report, UUID>
{
    public ReportDAO(Class<Report> entityClass, Datastore ds)
    {
        super(entityClass, ds);
    }
}