/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ENSE600.cims;

import java.util.UUID;

/**
 *
 * @author gggob
 */
public class Apparatus extends Equipment {

    private static final int MAX_USES_BEFORE_INSPECTION = 20;

    public Apparatus(String name) {
        super(name, new UsageBasedInspectionPolicy(MAX_USES_BEFORE_INSPECTION));
    }
    //constructor for Excel import

    public Apparatus(UUID id, String name) {
        super(id, name, new UsageBasedInspectionPolicy(20));
    }

    public Apparatus(
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

}
