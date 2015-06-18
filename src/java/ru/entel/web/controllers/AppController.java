/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.entel.web.controllers;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ApplicationScoped;

/**
 *
 * @author Farades
 */
@ManagedBean
@ApplicationScoped
public class AppController {

    /**
     * Creates a new instance of AppController
     */
    public AppController() {
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
}
