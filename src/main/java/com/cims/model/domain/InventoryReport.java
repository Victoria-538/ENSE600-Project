/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cims.model.domain;

/**
 *
 * @author gggob
 */
public class InventoryReport {
    
    //classes are declared as public due to a lrage amount of getters to be added otherwise
    //in real-life project these fielsds would be private      
    public int totalEquipment;

    // Type counts
    public int hardwareCount;
    public int apparatusCount;
    public int propCount;

    // Equipment status counts
    public int availableCount;
    public int checkedOutCount;
    public int faultyCount;
    public int underInspectionCount;

    // Inspection status counts
    public int notDueCount;
    public int dueCount;
    public int flaggedFaultyCount;
    
     @Override
    public String toString() {
        return """
               === Inventory Report ===

               Total Equipment: %d

               By Type:
               Hardware: %d
               Apparatus: %d
               Props: %d

               By Equipment Status:
               Available: %d
               Checked Out: %d
               Faulty: %d
               Under Inspection: %d

               By Inspection Status:
               Not Due: %d
               Due: %d
               Flagged Faulty: %d
               """
                .formatted(
                        totalEquipment,
                        hardwareCount,
                        apparatusCount,
                        propCount,
                        availableCount,
                        checkedOutCount,
                        faultyCount,
                        underInspectionCount,
                        notDueCount,
                        dueCount,
                        flaggedFaultyCount
                );
    }
}
    

