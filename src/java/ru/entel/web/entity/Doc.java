/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.entel.web.entity;

import java.io.Serializable;

/**
 *
 * @author Farades
 */
public class Doc implements Serializable {
    private Long id;
    private String descr;

    public Doc(Long id, String descr) {
        this.id = id;
        this.descr = descr;
    }

    public Long getId() {
        return id;
    }

    public String getDescr() {
        return descr;
    }
}
