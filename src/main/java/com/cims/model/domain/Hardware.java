/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cims.model.domain;

import com.cims.model.policy.UsageBasedInspectionPolicy;
import java.util.UUID;

/**
 *
 * @author gggob
 */
public class Hardware extends Equipment {
     private static final int MAX_USES_BEFORE_INSPECTION = 10;

    public Hardware(String name) {
        super(name, new UsageBasedInspectionPolicy(MAX_USES_BEFORE_INSPECTION));
    }
    
    //constuctor for Excel import
 public Hardware(UUID id, String name) {
        super(id, name, new UsageBasedInspectionPolicy(10));
    }
 
 
public Hardware(
        UUID id,
        String name,
        int timesUsed,
        EquipmentStatus equipmentStatus,
        InspectionStatus inspectionStatus
) {
    super(id,
          name,
          new UsageBasedInspectionPolicy(MAX_USES_BEFORE_INSPECTION),
          timesUsed,
          equipmentStatus,
          inspectionStatus);
}

@Override
public String getEquipmentType() {
    return "Hardware";
}

}
