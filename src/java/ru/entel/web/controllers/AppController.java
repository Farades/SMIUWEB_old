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
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedProperty;
import ru.entel.db.Database;
import ru.entel.web.beans.WebEngine;

/**
 *
 * @author Farades
 */
@ManagedBean
@ApplicationScoped
public class AppController {
    private String objName;
    
    @ManagedProperty(value = "#{webEngine}")
    private WebEngine webEngine;
    
    public AppController() {
        try {
            Statement stm = null;
            Connection conn = null;
            ResultSet rst = null;
            conn = Database.getInstance().getConn();
            stm = conn.createStatement();
            rst = stm.executeQuery("SELECT `value` FROM `properties` WHERE name='obj_name'");
            while(rst.next()) {
                this.objName = rst.getString("value");
            }
        } catch (SQLException ex) {
            Logger.getLogger(AppController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public String getTime() {
        Date currentTime = new Date();
        Locale locale = new Locale("ru", "RU");
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yy HH:mm:ss");
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
        
        return this.objName;
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
