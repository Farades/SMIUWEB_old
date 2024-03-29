/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.entel.web.beans;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import ru.entel.engine.Engine;
import ru.entel.web.controllers.DeviceController;

@ManagedBean(name = "webEngine", eager = true)
@ApplicationScoped
public class WebEngine {
    private Engine dataEngine;
    private boolean dataEngineStatus = false;
    
    public WebEngine() {
        System.out.println("Web engine constructor");
        this.init();
    }
    
    public void init() {
        try {
            System.out.println("Web engine init");
            InitialContext ic = new InitialContext();
            DataSource ds = (DataSource) ic.lookup("jdbc/smiu");
            dataEngine = new Engine(ds);
            dataEngine.init();
//            dataEngine.run();
        } catch (NamingException ex) {
            Logger.getLogger(WebEngine.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void dataEngineRun() {
        if (dataEngineStatus != true) {
            dataEngine.run();
            dataEngineStatus = true;
        }
    }

    public void dataEnginestop() {
        if (dataEngineStatus == true) {
            dataEngine.stop();
            dataEngineStatus = false;
        }
    }
    
    public void changeDataEngineStatus() {
        if (dataEngineStatus == true) {
            dataEngine.run();
        } else {
            dataEngine.stop();
        }
    }
   
    public boolean isDataEngineStatus() {
        return dataEngineStatus;
    }

    public void setDataEngineStatus(boolean dataEngineStatus) {
        this.dataEngineStatus = dataEngineStatus;
    }
    
    public Engine getDataEngine() {
        return dataEngine;
    }
    
    
}
