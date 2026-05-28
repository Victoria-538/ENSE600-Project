/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.ENSE600.cims;

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
       Collection<Equipment> findByType(Class<? extends Equipment> type);
       void remove(UUID id);
}
