/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ENSE600.cims;

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
    public Collection<Equipment> findAll() {
        return storage.values();
    }

    @Override
    public Collection<Equipment> findByType(Class<? extends Equipment> type) {
        return storage.values()
                .stream()
                .filter(type::isInstance)
                .collect(Collectors.toList());
    }

    @Override
    public void remove(UUID id) {
        storage.remove(id);
    }

}
