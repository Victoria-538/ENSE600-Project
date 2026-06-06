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

    public final LocalDateTime actionTime;

    public final UUID equipmentId;
    public final String equipmentName;

    public final String userName;
    public final String userRole;

    public final ActionType actionType;

    public final String notes;

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
}
