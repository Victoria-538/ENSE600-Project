/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cims.model.service;

import com.cims.model.domain.*;
import com.cims.model.domain.InventoryReport;
import com.cims.model.repository.EquipmentRepository;
import java.util.Collection;

/**
 *
 * @author gggob
 */
public class ReportServiceImpl implements ReportService {

    private final EquipmentRepository repository;

    public ReportServiceImpl(EquipmentRepository repository) {
        this.repository = repository;
    }

    @Override
    public InventoryReport generateInventoryReport() {
        Collection<Equipment> equipmentList = repository.findAll();
        InventoryReport report = new InventoryReport();
        report.totalEquipment = equipmentList.size();
        
         for (Equipment equipment : equipmentList) {

            // Count by type
            if (equipment instanceof Hardware) {
                report.hardwareCount++;
            } else if (equipment instanceof Apparatus) {
                report.apparatusCount++;
            } else if (equipment instanceof Prop) {
                report.propCount++;
            }

            // Count by equipment status
            switch (equipment.getEquipmentStatus()) {
                case AVAILABLE ->
                    report.availableCount++;

                case CHECKED_OUT ->
                    report.checkedOutCount++;

                case FAULTY ->
                    report.faultyCount++;

                case UNDER_INSPECTION ->
                    report.underInspectionCount++;
            }

            // Count by inspection status
            switch (equipment.getInspectionStatus()) {
                case NOT_DUE ->
                    report.notDueCount++;

                case DUE ->
                    report.dueCount++;

                case FLAGGED_FAULTY ->
                    report.flaggedFaultyCount++;
            }
        }

        return report;
        
    }
}
