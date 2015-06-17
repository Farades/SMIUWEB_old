/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.entel.web.controllers;

import java.util.Map;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import ru.entel.devices.Device;
import ru.entel.web.servlets.DataDealerRunner;

/**
 *
 * @author Farades
 */
@ManagedBean
@ApplicationScoped
public class DeviceController {
    private Device currentDevice;
    private Map<String, Device> allDevices = DataDealerRunner.engine.getDevices();
    
    public String selectDevice() {
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        deviceByName(params.get("device_name"));
        return "device";
    }
    
    public void updateDevice(){
        System.out.println("update" + currentDevice.getName());
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
                res = "device.png";
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
}
