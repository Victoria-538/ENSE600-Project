/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.cims.model.service;

import com.cims.model.domain.Equipment;
import com.cims.model.domain.Hardware;
import com.cims.model.domain.Apparatus;
import com.cims.model.domain.EquipmentStatus;
import com.cims.model.domain.InspectionStatus;
import com.cims.model.domain.Prop;
import java.util.Collection;

/**
 *
 * @author gggob
 */
public interface InventoryService {
    
    void addEquipment(Equipment equipment);
    void removeEquipment(Equipment equipment);
    Collection<Equipment> listAll();
    Collection<Hardware>  listHardware();
    Collection<Apparatus> listApparatus();
    Collection<Prop>  listProps();
    
    //added methods
    Collection<Equipment> search(String searchTerm);
    Collection<Equipment> filter(
        String type,
        EquipmentStatus equipmentStatus,
        InspectionStatus inspectionStatus);
}
