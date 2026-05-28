/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cims.model.domain;

import com.cims.model.policy.NoInspectionPolicy;
import java.util.UUID;

/**
 *
 * @author gggob
 */
public class Prop extends Equipment {

    public Prop(String name) {
        super(name, new NoInspectionPolicy());
    }
//constructor for Excel import

    public Prop(UUID id, String name) {
        super(id, name, new NoInspectionPolicy());
    }

    public Prop(
            UUID id,
            String name,
            int timesUsed,
            EquipmentStatus equipmentStatus,
            InspectionStatus inspectionStatus
    ) {
        super(id,
                name,
                new NoInspectionPolicy(),
                timesUsed,
                equipmentStatus,
                inspectionStatus);
    }

    @Override
    public String getEquipmentType() {
        return "Prop";
    }
}
