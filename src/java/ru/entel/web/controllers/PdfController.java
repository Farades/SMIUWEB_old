/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.entel.web.controllers;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.UploadedFile;
import ru.entel.db.Database;
import ru.entel.web.entity.Doc;

/**
 *
 * @author Farades
 */
@ManagedBean
@ApplicationScoped
public class PdfController implements Serializable {
    private ArrayList<Doc> docs = new ArrayList<Doc>();
    private Doc selectedDoc;
    
    public PdfController() {
    }
    
    @PostConstruct
    public void init() {
        updateDocs();
    }    
    public void handleFileUpload(FileUploadEvent event) {
        UploadedFile file = event.getFile();
        if(file != null && file.getSize() > 10) {
            Connection conn = Database.getInstance().getConn();
            try {
                
                PreparedStatement stmt = conn.prepareStatement("INSERT INTO DOCS (descr, file) VALUES(?, ?)");
                stmt.setString(1, file.getFileName());
                stmt.setObject(2, file.getContents());
                stmt.executeUpdate(); 
                FacesMessage message = new FacesMessage(file.getFileName() + " загружен");
                FacesContext.getCurrentInstance().addMessage(null, message);
                updateDocs();
            } catch(SQLException ex) {
                ex.printStackTrace();
            } finally {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    Logger.getLogger(PdfController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            file = null;
        } else {
            FacesMessage message = new FacesMessage("Файл не выбран или слишком мал");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }
    
    public void updateDocs() {
        docs.clear();
        Connection conn = Database.getInstance().getConn();
        try {
            
            Statement stmt = conn.createStatement();
            ResultSet rst = stmt.executeQuery("SELECT id, descr FROM docs ORDER by id DESC");
            while (rst.next()) {
                Long id = rst.getLong("id");
                String descr = rst.getString("descr");
                Doc doc = new Doc(id, descr);
                docs.add(doc);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(PdfController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void deleteDoc() {
        System.out.println("Delete doc");
        Long id = Long.valueOf(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("doc_id"));
        Connection conn = Database.getInstance().getConn();
        try {
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM DOCS WHERE id=?");
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(PdfController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(PdfController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        updateDocs();
    }

    public Doc getSelectedDoc() {
        return selectedDoc;
    }

    public void setSelectedDoc(Doc selectedDoc) {
        System.out.println(selectedDoc.getDescr());
        this.selectedDoc = selectedDoc;
    }
    
    public ArrayList<Doc> getDocs() {
        return docs;
    }
}
