/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.entel.web.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedProperty;
import javax.json.JsonObject;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import ru.entel.db.Database;
import ru.entel.web.beans.WebEngine;

/**
 *
 * @author Артем
 */
@ManagedBean(eager = true)
@ApplicationScoped
public class ConfiguratorController {
    @ManagedProperty(value = "#{webEngine}")
    private WebEngine webEngine;
    
    @ManagedProperty(value = "#{deviceController}")
    private DeviceController deviceController;
    
    private String devicesData;
    private String protocolData;

    public ConfiguratorController() {
        InitialContext ic;
        try {
            ic = new InitialContext();
            DataSource ds = (DataSource) ic.lookup("jdbc/smiu");
            Database.setDataSource(ds);
        } catch (NamingException ex) {
            Logger.getLogger(ConfiguratorController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Statement stm = null;
        Connection conn = null;
        ResultSet rst = null;
        conn = Database.getInstance().getConn();
        try {
            
            stm = conn.createStatement();
            rst = stm.executeQuery("SELECT DATA FROM json_config WHERE NAME='devices'");
            while(rst.next()) {
                this.devicesData = rst.getString("DATA");
            }
            rst = stm.executeQuery("SELECT DATA FROM json_config WHERE NAME='protocol'");
            while(rst.next()) {
                this.protocolData = rst.getString("DATA");
            }
        } catch (SQLException ex) {
            Logger.getLogger(ConfiguratorController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(ConfiguratorController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        JsonParser parser = new JsonParser();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonElement el = parser.parse(this.devicesData);
        this.devicesData = gson.toJson(el); // done
        this.devicesData = removeUTFCharacters(devicesData);
        
        el = parser.parse(this.protocolData);
        this.protocolData = gson.toJson(el);
        this.protocolData = removeUTFCharacters(protocolData);
    }
    
    public void updateDevices() {
        //Обновление JSON'a в БД
        PreparedStatement stm = null;
        Connection conn = null;
        try {
            conn = Database.getInstance().getConn();
            stm = conn.prepareStatement("UPDATE json_config SET DATA=? WHERE NAME='devices'");
            stm.setString(1, devicesData);
            stm.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(ConfiguratorController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(ConfiguratorController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        //Переинициализация приложения
        webEngine.dataEnginestop();
        webEngine.init();
        deviceController.init();
    }
    
    public void updateProtocol() {
        //Обновление JSON'a в БД
        PreparedStatement stm = null;
        Connection conn = null;
        try {
            conn = Database.getInstance().getConn();
            stm = conn.prepareStatement("UPDATE json_config SET DATA=? WHERE NAME='protocol'");
            stm.setString(1, protocolData);
            stm.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(ConfiguratorController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(ConfiguratorController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        //Переинициализация приложения
        webEngine.dataEnginestop();
        webEngine.init();
        deviceController.init();
    }

    public static String removeUTFCharacters(String data){
        Pattern p = Pattern.compile("\\\\u(\\p{XDigit}{4})");
        Matcher m = p.matcher(data);
        StringBuffer buf = new StringBuffer(data.length());
        while (m.find()) {
            String ch = String.valueOf((char) Integer.parseInt(m.group(1), 16));
            m.appendReplacement(buf, Matcher.quoteReplacement(ch));
        }
        m.appendTail(buf);
        return buf.toString();
    }
    
    public void saveConfig() {
        
    }
    
    public String getDevicesData() {
        return devicesData;
    }

    public void setDevicesData(String deviceData) {
        this.devicesData = deviceData;
    }

    public String getProtocolData() {
        return protocolData;
    }

    public void setProtocolData(String protocolData) {
        this.protocolData = protocolData;
    }

    public WebEngine getWebEngine() {
        return webEngine;
    }

    public void setWebEngine(WebEngine webEngine) {
        this.webEngine = webEngine;
    }

    public DeviceController getDeviceController() {
        return deviceController;
    }

    public void setDeviceController(DeviceController deviceController) {
        this.deviceController = deviceController;
    }
    
}
