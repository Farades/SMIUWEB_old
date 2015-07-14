/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.entel.web.controllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ApplicationScoped;
import javax.faces.context.FacesContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import ru.entel.db.Database;
import ru.entel.db.HistoryDeviceException;
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
    private LazyDataModel logsModel;

    public LogsController() {
        logsModel = new LazyDataModel() {
            @Override
            public List load(int first, int pageSize, String sortField, SortOrder sortOrder, Map filters) {
                this.setRowCount(LogSaverDB.getLogsSize(logsDate));
                return getDataLogs(first, pageSize);
            }
        };
    }
    
    @PostConstruct
    public void init() {
        this.logsDate = new Date();
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
    
    public ArrayList<LogRow> getDataLogs(int first, int pageSize) {
//        System.out.println("getDataLogs size: " +  LogSaverDB.getDataLogsByDate(this.logsDate, first, pageSize).size());
        return LogSaverDB.getDataLogsByDate(this.logsDate, first, pageSize);
    }
    
    public int getDataLogsSize() {
        return LogSaverDB.getLogsSize(logsDate);
    }
    
    public String getDataLogsFileName() {
        SimpleDateFormat sf = new SimpleDateFormat("dd_MM_yyyy");
        String date = sf.format(this.logsDate);
        return "data_logs_" + date;
    }

    public LazyDataModel getLogsModel() {
        return logsModel;
    }
    
    public void destroyLogs() {
        Connection conn = Database.getInstance().getConn();
        try {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("TRUNCATE ALARM_LOG");
            stmt.executeUpdate("TRUNCATE DATA_LOG");
            } catch(SQLException ex) {
                ex.printStackTrace();
            } finally {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    Logger.getLogger(LogsController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
    }
}
