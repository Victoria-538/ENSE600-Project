/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cims.model.repository;

import com.cims.model.domain.Equipment;
import com.cims.model.domain.EquipmentStatus;
import com.cims.model.domain.InspectionStatus;
import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * In-memory implementation of EquipmentRepository.
 *
 * Uses a thread-safe map to support concurrent access.
 */
public class InMemoryEquipmentRepository implements EquipmentRepository {

    private final ConcurrentHashMap<UUID, Equipment> storage = new ConcurrentHashMap<>();

    @Override
    public void save(Equipment equipment) {
        if (equipment == null) {
            throw new IllegalArgumentException("Equipment cannot be null.");
        }
        storage.put(equipment.getId(), equipment);
    }

    @Override
    public Equipment findById(UUID id) {
        return storage.get(id);
    }

    @Override
    public Collection<Equipment> findByType(
            Class<? extends Equipment> type) {

        return storage.values()
                .stream()
                .filter(type::isInstance)
                .toList();
    }

    @Override
    public Collection<Equipment> findAll() {
        return storage.values();
    }

    @Override
    public void remove(UUID id) {
        storage.remove(id);
    }

//added methods
    @Override
    public Collection<Equipment> filter(String type, EquipmentStatus equipmentStatus, InspectionStatus inspectionStatus) {
        return storage.values()
                .stream()
                .filter(e
                        -> type == null
                || type.isBlank()
                || e.getEquipmentType().equalsIgnoreCase(type))
                .filter(e
                        -> equipmentStatus == null
                || e.getEquipmentStatus() == equipmentStatus)
                .filter(e
                        -> inspectionStatus == null
                || e.getInspectionStatus() == inspectionStatus)
                .toList();
    }

    @Override
    public Collection<Equipment> search(String searchTerm) {
        if (searchTerm == null || searchTerm.isBlank()) {
            return findAll();
        }

        String query = searchTerm.toLowerCase();

        return storage.values()
                .stream()
                .filter(e
                        -> e.getName().toLowerCase().contains(query)
                || e.getId().toString().toLowerCase().contains(query)
                || e.getEquipmentType().toLowerCase().contains(query))
                .toList();
    }

}
