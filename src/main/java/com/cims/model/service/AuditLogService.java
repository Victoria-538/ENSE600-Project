/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.cims.model.service;
import com.cims.model.domain.ActionType;
import com.cims.model.domain.AuditLog;
import com.cims.model.domain.Equipment;
import com.cims.model.domain.UserSession;
import java.util.Collection;
/**
 *
 * @author gggob
 */
public interface AuditLogService {
    void recordEvent(
            Equipment equipment,
            UserSession session,
            ActionType actionType,
            String notes);

    Collection<AuditLog> getAllLogs();
}
