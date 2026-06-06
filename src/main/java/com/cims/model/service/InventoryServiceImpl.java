/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cims.model.service;

import com.cims.model.repository.EquipmentRepository;
import com.cims.model.domain.*;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 *
 * @author gggob
 */
public class InventoryServiceImpl implements InventoryService {

    private final EquipmentRepository equipmentRepository;
    private final UserSession session;
    private final AuditLogService auditLogService;

    public InventoryServiceImpl(
            EquipmentRepository equipmentRepository,
            UserSession session,
            AuditLogService auditLogService) {

        this.equipmentRepository = equipmentRepository;
        this.session = session;
        this.auditLogService = auditLogService;
    }

    @Override
    public void addEquipment(Equipment equipment) {
        if (!session.getRole().canAddEquipment()) {

            throw new SecurityException(
                    "Access denied: role " + session.getRole()
                    + " cannot add equipment.");
        }
        equipmentRepository.save(equipment);
        auditLogService.recordEvent(
        equipment,
        session,
        ActionType.ADDED,
        "Equipment added to inventory");
    }

    @Override

    public void removeEquipment(Equipment equipment) {
        if (!session.getRole().canRemoveEquipment(equipment)) {
            throw new SecurityException(
                    "Access denied: role " + session.getRole()
                    + " cannot remove this equipment."
            );
        }

        equipmentRepository.remove(equipment.getId());
        auditLogService.recordEvent(
        equipment,
        session,
        ActionType.REMOVED,
        "Equipment removed from inventory");
    }

    @Override
    public Collection<Equipment> listAll() {
        return equipmentRepository.findAll();

    }

    @Override
    public Collection<Hardware> listHardware() {

        return equipmentRepository
                .findByType(Hardware.class)
                .stream()
                .map(Hardware.class::cast)
                .collect(Collectors.toList());

    }

    @Override
    public Collection<Apparatus> listApparatus() {

        return equipmentRepository
                .findByType(Apparatus.class)
                .stream()
                .map(Apparatus.class::cast)
                .collect(Collectors.toList());

    }

    @Override
    public Collection<Prop> listProps() {

        return equipmentRepository
                .findByType(Prop.class)
                .stream()
                .map(Prop.class::cast)
                .collect(Collectors.toList());

    }

    //added methods
    @Override
    public Collection<Equipment> search(String searchTerm) {
        return equipmentRepository.search(searchTerm);
    }

    @Override
    public Collection<Equipment> filter(
            String type,
            EquipmentStatus equipmentStatus,
            InspectionStatus inspectionStatus) {

        return equipmentRepository.filter(
                type,
                equipmentStatus,
                inspectionStatus);
    }

}
