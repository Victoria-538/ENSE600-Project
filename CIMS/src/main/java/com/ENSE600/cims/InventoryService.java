/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.ENSE600.cims;

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
}
