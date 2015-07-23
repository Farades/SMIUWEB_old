/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.entel.web.entity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import ru.entel.db.Database;
import ru.entel.web.controllers.ConfiguratorController;

/**
 *
 * @author Farades
 */
public class AppProperty {
    private Long id;
    private String name;
    private String value;
    private String desc;

    public AppProperty(Long id, String name, String value, String desc) {
        this.id = id;
        this.name = name;
        this.value = value;
        this.desc = desc;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setValue(String value) {
        PreparedStatement stm = null;
        Connection conn = null;
        try {
            conn = Database.getInstance().getConn();
            stm = conn.prepareStatement("UPDATE properties SET value=? WHERE id=?");
            stm.setString(1, value);
            stm.setLong(2, this.id);
            stm.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(AppProperty.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(AppProperty.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        this.value = value;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
