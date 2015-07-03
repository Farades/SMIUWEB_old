/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.entel.web.controllers;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import org.primefaces.event.FileUploadEvent;
import ru.entel.db.Database;

/**
 *
 * @author Farades
 */
@ManagedBean
@ApplicationScoped
public class PdfController implements Serializable {
    private final int maxSize = 16777215;
    private byte[] uploadedPdf;
    
    public PdfController() {
    }
    
    public void handleFileUpload(FileUploadEvent event) {
        this.uploadedPdf = event.getFile().getContents().clone();
    }
    
    public void savePdf() {
        try {
            Connection conn = Database.getInstance().getConn();
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO DOCS (descr, file) VALUES(?, ?)");
            stmt.setString(1, "Тест");
            stmt.setObject(2, uploadedPdf);
            stmt.executeUpdate();
            this.uploadedPdf = null;
        } catch (SQLException ex) {
            Logger.getLogger(PdfController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            Database.getInstance().closeConnection();
        }
    }
    
}
