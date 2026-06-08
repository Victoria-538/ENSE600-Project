/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cims.ui.controller;

import com.cims.model.domain.*;
import com.cims.model.service.InventoryService;

import java.util.Collection;

/**
 *
 * @author gggob
 */
public class EquipmentManagementController {

    private final InventoryService inventoryService;

    public EquipmentManagementController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    public Collection<Equipment> getAllEquipment() {
        return inventoryService.listAll();
    }

    public Collection<Equipment> search(String term) {
        if (term == null || term.isBlank()) {
            return getAllEquipment();
        }
        return inventoryService.search(term.trim());
    }

    public void removeEquipment(Equipment equipment) {
        inventoryService.removeEquipment(equipment);
    }

    public Collection<Equipment> filter(
            String searchTerm,
            String type,
            EquipmentStatus equipmentStatus,
            InspectionStatus inspectionStatus) {

        Collection<Equipment> results
                = inventoryService.filter(
                        type,
                        equipmentStatus,
                        inspectionStatus);

        // Apply search on top of filtered results
        if (searchTerm == null || searchTerm.isBlank()) {
            return results;
        }

        String search = searchTerm.toLowerCase().trim();

        return results.stream()
                .filter(e
                        -> e.getName().toLowerCase().contains(search)
                || e.getId().toString().toLowerCase().contains(search)
                || e.getEquipmentType().toLowerCase().contains(search))
                .toList();
    }

}
