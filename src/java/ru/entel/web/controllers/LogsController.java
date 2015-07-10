/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.entel.web.controllers;

import java.util.ArrayList;
import java.util.Date;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ApplicationScoped;
import org.primefaces.event.SelectEvent;
import ru.entel.db.LogRow;
import ru.entel.db.LogSaverDB;

/**
 *
 * @author Farades
 */
@ManagedBean
@ApplicationScoped
public class LogsController {
    private Date logsDate;
    
    /**
     * Creates a new instance of LogsController
     */
    public LogsController() {
    }

    public Date getLogsDate() {
        return logsDate;
    }

    public void dateSelect(SelectEvent event) {
        System.out.println("onDateSelect");
    }
    
    public void setLogsDate(Date logsDate) {
        System.out.println("setDataLogs");
        this.logsDate = logsDate;
    }
    
    public ArrayList<LogRow> getDataLogs() {
        System.out.println("getDataLogs");
        if (this.logsDate == null) {
            return LogSaverDB.getDataLogsByCurrentDate();
        }
        else {
            return LogSaverDB.getDataLogsByDate(this.logsDate);
        }
    }
    
    
}
