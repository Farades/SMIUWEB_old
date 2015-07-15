/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.entel.web.controllers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import ru.entel.db.DeviceExceptionFromDb;
import ru.entel.db.HistoryDeviceException;
import ru.entel.db.LogRow;
import ru.entel.db.LogSaverDB;
import ru.entel.devices.Device;
import ru.entel.devices.DeviceException;
import ru.entel.web.beans.WebEngine;

@ManagedBean
@ApplicationScoped
public class DeviceController {
    @ManagedProperty(value = "#{webEngine}")
    private WebEngine webEngine;
    
    private Device currentDevice;
    private Map<String, Device> allDevices;
    private Map<String, Set<DeviceException>> activeExceptions;
    private LazyDataModel alarmModel;

    public DeviceController() {
        System.out.println("Device Controller constr: " + allDevices);
        alarmModel = new LazyDataModel() {
            @Override
            public List load(int first, int pageSize, String sortField, SortOrder sortOrder, Map filters) {
                this.setRowCount(HistoryDeviceException.getAlarmSize());
                return getHistoryException(first, pageSize);
            }
            
        };
    }
    
    @PostConstruct
    public void init() {
        allDevices = webEngine.getDataEngine().getDevices();
        activeExceptions = new HashMap<String, Set<DeviceException>>();
        System.out.println("Device controller PC: " + allDevices);
    }
    
    public String selectDevice() {
        String res = "";
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        deviceByName(params.get("device_name"));
        switch (currentDevice.getType()) {
            case MFM: {
                res = "mfm";
                break;
            } case VOLTMETER: {
                res = "voltmeter";
                break;
            } case AMPERMETER: {
                res = "ampermeter";
                break;
            }
        }
        return res;
    }
    
    public String getCurrentDeviceValueByName(String name) {
        String res = "Null";
        if (currentDevice.getValues().get(name) != null) {
            res = currentDevice.getValues().get(name).toString();
        }
        return res;
    }
    
    public void updateDevice(){
        System.out.println(currentDevice.getValues());
        System.out.println("update: " + currentDevice.getName());
    }

    public void deviceByName(String name) {
        currentDevice = this.allDevices.get(name);
    }
    
    public Device getCurrentDevice() {
        return currentDevice;
    }
    
    public String getImage(String name) {
        String res = "";
        
        Device d = this.allDevices.get(name);
        switch (d.getType()) {
            case MFM: {
                res = "device_3.png";
                break;
            }
            case VOLTMETER: {
                res = "device_1.png";
                break;
            }
            case AMPERMETER: {
                res = "device_2.png";
                break;
            }
        }
        return res;
    }

    public Map<String, Device> getAllDevices() {
        return allDevices;
    }

    public int getAlarmsSize() {
        return HistoryDeviceException.getAlarmSize();
    }
    
    public String getAlarmsLogsFileName() {
        SimpleDateFormat sf = new SimpleDateFormat("dd_MM_yyyy");
        String date = sf.format(new Date());
        return "alarms_logs_" + date;
    }
    
    public Map<String, Set<DeviceException>> getAllActiveExceptions() {
        this.activeExceptions.clear();
        for (Device device : allDevices.values()) {
            Set<DeviceException> activeExceptionByDevice = device.getActiveExceptions();
            if (activeExceptionByDevice.size() > 0) {
                this.activeExceptions.put(device.getDescription(), activeExceptionByDevice);
            }
        }
        return activeExceptions;
    }
    
    public ArrayList<DeviceExceptionFromDb> getHistoryException(int first, int pageSize) {
//        System.out.println("History Ex size:" + HistoryDeviceException.getHistory(first, pageSize).size());
        return HistoryDeviceException.getHistory(first, pageSize);
    }

    public LazyDataModel getAlarmModel() {
        return alarmModel;
    }
    
    public WebEngine getWebEngine() {
        return webEngine;
    }

    public void setWebEngine(WebEngine webEngine) {
        this.webEngine = webEngine;
    }
}
