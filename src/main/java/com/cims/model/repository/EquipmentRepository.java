/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.cims.model.repository;

import com.cims.model.domain.Equipment;
import com.cims.model.domain.EquipmentStatus;
import com.cims.model.domain.InspectionStatus;
import java.util.Collection;
import java.util.UUID;

/**
 *
 * @author gggob
 */
public interface EquipmentRepository {
     void save(Equipment equipment);
      Equipment findById(UUID id);
      Collection<Equipment> findAll();
      Collection<Equipment> findByType(
        Class<? extends Equipment> type);
      // added methods
      Collection<Equipment> search(String searchTerm);
      Collection<Equipment> filter(String type,EquipmentStatus equipmentStatus,InspectionStatus inspectionStatus);
      void remove(UUID id);
      
      
}
