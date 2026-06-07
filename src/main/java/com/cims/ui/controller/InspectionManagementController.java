/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cims.ui.controller;

import com.cims.model.domain.*;
import com.cims.model.service.*;
import java.util.*;
import javax.swing.*;

/**
 *
 * @author gggob
 */
public class InspectionManagementController {

    private final InspectionService inspectionService;
    private final InventoryService inventoryService;
    private final UserSession session;
    private final JFrame parent;

    public InspectionManagementController(
            InspectionService inspectionService,
            InventoryService inventoryService,
            UserSession session,
            JFrame parent) {

        this.inspectionService = inspectionService;
        this.inventoryService = inventoryService;
        this.session = session;
        this.parent = parent;
    }

    public boolean canAccess() {

        UserRole role = session.getRole();

        return role == UserRole.ENGINEER
                || role == UserRole.INVENTORY_MANAGER;
    }

    public Collection<Equipment> getEquipmentForRole() {

        Collection<Equipment> all = inventoryService.listAll();
        List<Equipment> result = new ArrayList<>();

        UserRole role = session.getRole();

        for (Equipment e : all) {

            if (role == UserRole.ENGINEER) {

                if (e.getEquipmentStatus() == EquipmentStatus.FAULTY
                        || e.getInspectionStatus() == InspectionStatus.DUE) {

                    result.add(e);
                }

            } else if (role == UserRole.INVENTORY_MANAGER) {

                if (e.getEquipmentType().equals("Prop")
                        && e.getEquipmentStatus() == EquipmentStatus.FAULTY) {

                    result.add(e);
                }
            }
        }

        return result;
    }

    public void markSafe(
            Equipment equipment,
            String notes) {

        if (equipment == null) {
            return;
        }

        try {

            inspectionService.markSafe(
                    equipment.getId(),
                    session,
                    notes
            );

            JOptionPane.showMessageDialog(
                    parent,
                    "Marked safe."
            );

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(
                    parent,
                    ex.getMessage()
            );
        }
    }

   public void discardEquipment(Equipment equipment) {

    if (equipment == null) {
        return;
    }

    try {

        inventoryService.removeEquipment(equipment);

        JOptionPane.showMessageDialog(
                parent,
                "Equipment discarded."
        );

    } catch (Exception ex) {

        JOptionPane.showMessageDialog(
                parent,
                ex.getMessage()
        );
    }
}
}
