/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cims.ui.view;

import com.cims.model.domain.UserSession;
import com.cims.model.repository.*;
import com.cims.model.service.*;

/**
 *
 * @author gggob
 */
public class ApplicationContext {

    private final UserSession session;

    private final InventoryService inventoryService;
    private final CheckoutService checkoutService;
    private final InspectionService inspectionService;
    private final AuditLogService auditLogService;
    private final EquipmentRepository equipmentRepository;
    private final AuditLogRepository auditRepository;

    public ApplicationContext(
            UserSession session,
            InventoryService inventoryService,
            CheckoutService checkoutService,
            InspectionService inspectionService,
            AuditLogService auditLogService,
            EquipmentRepository equipmentRepository,
            AuditLogRepository auditRepository) {

        this.session = session;
        this.inventoryService = inventoryService;
        this.checkoutService = checkoutService;
        this.inspectionService = inspectionService;
        this.auditLogService = auditLogService;
        this.equipmentRepository = equipmentRepository;
        this.auditRepository = auditRepository;
    }

    public UserSession getSession() {
        return session;
    }

    public InventoryService getInventoryService() {
        return inventoryService;
    }

    public CheckoutService getCheckoutService() {
        return checkoutService;
    }

    public InspectionService getInspectionService() {
        return inspectionService;
    }

    public AuditLogService getAuditLogService() {
        return auditLogService;
    }

    public EquipmentRepository getEquipmentRepository() {
        return equipmentRepository;
    }

    public AuditLogRepository getAuditRepository() {
        return auditRepository;
    }
}
