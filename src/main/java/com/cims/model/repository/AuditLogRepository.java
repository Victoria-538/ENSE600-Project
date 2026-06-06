/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.cims.model.repository;
import com.cims.model.domain.AuditLog;
import java.util.Collection;
import java.util.UUID;
/**
 *
 * @author gggob
 */
public interface AuditLogRepository {
    void save(AuditLog auditLog);

    Collection<AuditLog> findAll();

    Collection<AuditLog> findByEquipment(UUID equipmentId);
}
