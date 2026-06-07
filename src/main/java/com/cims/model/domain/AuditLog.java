/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cims.model.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author gggob
 */
public class AuditLog {
         private final long id;

    private final LocalDateTime actionTime;

    private final UUID equipmentId;
    private final String equipmentName;

    private final String userName;
    private final String userRole;

    private final ActionType actionType;

    private final String notes;

    public AuditLog(
            long id,
            LocalDateTime actionTime,
            UUID equipmentId,
            String equipmentName,
            String userName,
            String userRole,
            ActionType actionType,
            String notes) {

        this.id = id;
        this.actionTime = actionTime;
        this.equipmentId = equipmentId;
        this.equipmentName = equipmentName;
        this.userName = userName;
        this.userRole = userRole;
        this.actionType = actionType;
        this.notes = notes;
    }
     @Override
    public String toString() {
        return String.format(
                "%s | %s | %s (%s) | %s | %s",
                actionTime,
                equipmentName,
                userName,
                userRole,
                actionType,
                notes
        );
    }
    
    public long getId() {
        return id;
    }

    public LocalDateTime getTimestamp() {
        return actionTime;
    }

    public UUID getEquipmentId() {
        return equipmentId;
    }

    public String getEquipmentName() {
        return equipmentName;
    }

    public String getUserName() {
        return userName;
    }

    public String getRole() {
        return userRole;
    }

    public ActionType getActionType() {
        return actionType;
    }

    public String getNotes() {
        return notes;
    }
}
