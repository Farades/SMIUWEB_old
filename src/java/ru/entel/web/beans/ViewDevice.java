/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.entel.web.beans;

import java.util.HashMap;
import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ApplicationScoped;
import ru.entel.devices.Device;
import ru.entel.protocols.registers.AbstractRegister;
import ru.entel.web.servlets.DataDealerRunner;

@ManagedBean
@ApplicationScoped
public class ViewDevice {
    private String image;
    private Device device;
    
    public ViewDevice() {
    }

    public ViewDevice(Device device) {
        this.device = device;
    }
}
