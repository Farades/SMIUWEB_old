/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.entel.web.controllers;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedProperty;
import org.primefaces.event.CellEditEvent;
import ru.entel.db.Database;
import ru.entel.web.beans.WebEngine;
import ru.entel.web.entity.AppProperty;

/**
 *
 * @author Farades
 */
@ManagedBean
@ApplicationScoped
public class AppController {
    private HashMap<String, AppProperty> properties;
    
    @ManagedProperty(value = "#{webEngine}")
    private WebEngine webEngine;
    
    public AppController() {
    }
    
    @PostConstruct
    public void init() {
        initProperties();
    }
    
    public void initProperties() {
        Connection conn = null;
        this.properties = new HashMap<>();
        try {
            conn = Database.getInstance().getConn();
            Statement stmt = conn.createStatement();
            ResultSet rst = stmt.executeQuery("SELECT * FROM properties");
            while(rst.next()) {
                Long id = rst.getLong("id");
                String name = rst.getString("name");
                String value = rst.getString("value");
                String desc = rst.getString("desc");
                this.properties.put(name, new AppProperty(id, name, value, desc));
            }
        } catch (SQLException ex) {
            Logger.getLogger(AppController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(ConfiguratorController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public HashMap<String, AppProperty> getProperties() {
        initProperties();
        return properties;
    }
    
    public String getTime() {
        Date currentTime = new Date();
        Locale locale = new Locale("ru", "RU");
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yy HH:mm");
        String res = dateFormat.format(currentTime);
        return res;
    }
    
    public String getIP() {
        String myLANIP = "";
        try {
            InetAddress addr = InetAddress.getLocalHost();
            myLANIP = addr.getHostAddress();
        } catch (UnknownHostException ex) {
            Logger.getLogger(AppController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return myLANIP;
    }
    
    public String getObjName() {
        return this.properties.get("obj_name").getValue();
    }
    
    public String getStrEngineStatus() {
        String res = "off.jpg";
        if (webEngine.isDataEngineStatus()) {
            res = "on.jpg";
        }
        return res;
    }
    
    public boolean isEngineState() {
        return webEngine.isDataEngineStatus();
    }

    public WebEngine getWebEngine() {
        return webEngine;
    }

    public void setWebEngine(WebEngine webEngine) {
        this.webEngine = webEngine;
    }
    
    
}
