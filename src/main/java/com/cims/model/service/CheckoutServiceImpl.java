/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cims.model.service;

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
public class CheckoutServiceImpl implements CheckoutService {

    private final EquipmentRepository equipmentRepository;

    public CheckoutServiceImpl(EquipmentRepository equipmentRepository) {
        this.equipmentRepository = equipmentRepository;
    }

    @Override
    public void checkOut(UUID equipmentId, UserSession session) {
        
        Equipment equipment = equipmentRepository.findById(equipmentId);
        if (equipment == null) {
            throw new IllegalArgumentException("Equipment not found.");
        }
        
        // Check if user is authorised to check out
        if (!session.getRole().canCheckout(equipment)) {
            throw new SecurityException(
                    "Role " + session.getRole() + " cannot check out this equipment."
            );
        }

        // Safety & availability checks
        if (equipment.getEquipmentStatus() != EquipmentStatus.AVAILABLE) {
            throw new IllegalStateException("Equipment is not available for checkout.");
        }

        if (equipment.getInspectionStatus() != InspectionStatus.NOT_DUE) {
            throw new IllegalStateException("Equipment requires inspection before use.");
        }

        // Perform checkout
        equipment.markCheckedOut();
        equipmentRepository.save(equipment);

    }

    @Override
    public void checkIn(UUID equipmentId, UserSession session) {

        Equipment equipment = equipmentRepository.findById(equipmentId);
        if (equipment == null) {
            throw new IllegalArgumentException("Equipment not found.");
        }

        // Equipment must be checked out
        if (equipment.getEquipmentStatus() != EquipmentStatus.CHECKED_OUT) {
            throw new IllegalStateException("Equipment is not currently checked out.");
        }

        // Perform check-in (increments usage inside Equipment)
        equipment.markCheckedIn();
        equipmentRepository.save(equipment);

    }

}
