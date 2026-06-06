/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cims.model.domain;

/**
 *
 * @author gggob
 */
public class Alert {
    
     public String equipmentName;
     public String alertMessage;

    public Alert(String equipmentName, String alertMessage) {
        this.equipmentName = equipmentName;
        this.alertMessage = alertMessage;
    }

    @Override
    public String toString() {
        return equipmentName + " : " + alertMessage;
    }
}
