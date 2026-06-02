/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cims.model.domain;

import com.cims.model.policy.InspectionPolicy;
import java.util.UUID;

/**
 *
 * @author gggob
 */
public abstract class Equipment {

    private UUID eqId;
    private String name;

    private EquipmentStatus equipmentStatus;
    private InspectionStatus inspectionStatus;
    private int timesUsed;
    private InspectionPolicy inspectionPolicy;

    //new equipment added
    protected Equipment(String name, InspectionPolicy inspectionPolicy) {
        this(UUID.randomUUID(), name, inspectionPolicy);
    }

    //import from file
    protected Equipment(UUID eqId, String name, InspectionPolicy inspectionPolicy) {
        this.eqId = eqId;
        this.name = name;
        this.inspectionPolicy = inspectionPolicy;
        this.timesUsed = 0;
        this.equipmentStatus = EquipmentStatus.AVAILABLE;
        this.inspectionStatus = InspectionStatus.NOT_DUE;

    }
    //save into file  

    protected Equipment(
            UUID id,
            String name,
            InspectionPolicy inspectionPolicy,
            int timesUsed,
            EquipmentStatus equipmentStatus,
            InspectionStatus inspectionStatus
    ) {
        this.eqId = id;
        this.name = name;
        this.inspectionPolicy = inspectionPolicy;
        this.timesUsed = timesUsed;
        this.equipmentStatus = equipmentStatus;
        this.inspectionStatus = inspectionStatus;
    }


    /* =========================
       State Transitions
       ========================= */
    public void markCheckedOut() {
        this.equipmentStatus = EquipmentStatus.CHECKED_OUT;
    }

    public void markCheckedIn() {
        this.equipmentStatus = EquipmentStatus.AVAILABLE;
        incrementUsage();
    }

    public void markFaulty() {
        this.equipmentStatus = EquipmentStatus.FAULTY;
        this.inspectionStatus = InspectionStatus.FLAGGED_FAULTY;
    }

    public void markUnderInspection() {
        this.equipmentStatus = EquipmentStatus.UNDER_INSPECTION;
        this.inspectionStatus = InspectionStatus.DUE;
    }

    /**
     * Called when an inspection passes. Returns the equipment to service and
     * resets usage count.
     */
    public void markAvailable() {
        this.equipmentStatus = EquipmentStatus.AVAILABLE;
        this.inspectionStatus = InspectionStatus.NOT_DUE;
        this.timesUsed = 0;
    }


    /* =========================
       Usage & Inspection Logic
       ========================= */
    public void incrementUsage() {
        timesUsed++;
        if (inspectionPolicy.requiresInspection(timesUsed)) {
            inspectionStatus = InspectionStatus.DUE;
        }
    }

    public boolean requiresInspection() {
        return inspectionPolicy.requiresInspection(timesUsed);
    }


    /* =========================
       Validation Helpers
       ========================= */
    private void ensureAvailable() {
        if (equipmentStatus != EquipmentStatus.AVAILABLE) {
            throw new IllegalStateException(
                    "Equipment must be AVAILABLE to be checked out."
            );
        }
    }

    /* =========================
       Getters
       ========================= */
    public UUID getId() {
        return eqId;
    }

    public String getName() {
        return name;
    }

    public EquipmentStatus getEquipmentStatus() {
        return equipmentStatus;
    }

    public InspectionStatus getInspectionStatus() {
        return inspectionStatus;
    }

    public int getTimesUsed() {
        return timesUsed;
    }
    
    public abstract String getEquipmentType();

    //setter for testing
    public void setName(String name) {
    this.name = name;
}
}
