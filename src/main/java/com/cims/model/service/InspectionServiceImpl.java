/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cims.model.service;

import com.cims.model.domain.ActionType;
import com.cims.model.repository.EquipmentRepository;
import com.cims.model.domain.EquipmentStatus;
import com.cims.model.domain.InspectionStatus;
import com.cims.model.domain.UserSession;
import com.cims.model.domain.Equipment;
import java.util.UUID;

/**
 *
 * @author gggob
 */
public class InspectionServiceImpl implements InspectionService {

    private final EquipmentRepository equipmentRepository;
    private final AuditLogService auditLogService;

    public InspectionServiceImpl(
            EquipmentRepository equipmentRepository,
            AuditLogService auditLogService) {

        this.equipmentRepository = equipmentRepository;
        this.auditLogService = auditLogService;
    }

    @Override
    public void flagForInspection(UUID equipmentId, UserSession session, String notes) {
        // Role check not required - everyone is permitted to flag for inspection,
        //even though technically only riggers and perforomers will be doing so

        //check if equipment exists
        Equipment equipment = equipmentRepository.findById(equipmentId);
        if (equipment == null) {
            throw new IllegalArgumentException("Equipment not found.");
        }
        
        String finalNote = (notes == null || notes.trim().isEmpty())
        ? "Flagged for inspection"
        : "Flagged for inspection | Note: " + notes.trim();
        // Move equipment to inspection state
        equipment.markFaulty();
        equipmentRepository.save(equipment);
        auditLogService.recordEvent(
        equipment,
        session,
        ActionType.FAULT_REPORTED,
        finalNote);

    }

    @Override
    public void conductInspection(UUID equipmentId, UserSession session) {

        Equipment equipment = equipmentRepository.findById(equipmentId);
        if (equipment == null) {
            throw new IllegalArgumentException("Equipment not found.");
        }
        // Only engineers can conduct inspections
        if (!session.getRole().canConductInspection(equipment)) {
            throw new SecurityException("Only engineers may conduct inspections.");
        }

        // Ensure equipment is actually under inspection
        if (equipment.getEquipmentStatus() != EquipmentStatus.UNDER_INSPECTION
                && equipment.getEquipmentStatus() != EquipmentStatus.FAULTY) {
            throw new IllegalStateException("Equipment is not under inspection.");
        }

        /*
         * Inspection decision:
         * - Safe equipment returns to AVAILABLE
         * - Unsafe equipment is DISCARDED
         *
         * NOTE: In a real system this decision might be interactive or data-driven.
         * Here we assume the engineer decides externally and calls the appropriate method.
         */
        boolean isSafe = assessEquipmentSafety(equipment);

        if (isSafe) {
            equipment.markAvailable();
            equipmentRepository.save(equipment);
            auditLogService.recordEvent(
        equipment,
        session,
        ActionType.INSPECTION,
        "Inspection passed - equipment marked available");
        } else {
            equipmentRepository.remove(equipmentId);
            auditLogService.recordEvent(
        equipment,
        session,
        ActionType.REMOVED,
        "Inspection failed - equipment discarded");
        }
    }

    @Override
    public void markSafe(UUID equipmentId, UserSession session, String notes) {
        Equipment equipment = equipmentRepository.findById(equipmentId);
        if (equipment == null) {
            throw new IllegalArgumentException("Equipment not found.");
        }

        if (!session.getRole().canConductInspection(equipment)) {
            throw new SecurityException("Access denied.");
        }

        equipment.markAvailable();
        equipmentRepository.save(equipment);
          String finalNote = notes == null || notes.isBlank()
            ? "Manually marked safe after inspection"
            : "Manually marked safe after inspection | Note: "
                    + notes.trim();
        auditLogService.recordEvent(
        equipment,
        session,
        ActionType.INSPECTION,
        finalNote);
    }

    private boolean assessEquipmentSafety(Equipment equipment) {
        // Placeholder logic
        // In real scenarios, this might involve inspection data, user input, or checklists********************
        return equipment.getInspectionStatus() != InspectionStatus.FLAGGED_FAULTY;
    }

}
