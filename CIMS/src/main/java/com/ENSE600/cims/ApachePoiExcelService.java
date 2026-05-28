/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ENSE600.cims;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

/**
 *
 * @author gggob
 */
public class ApachePoiExcelService implements EquipmentExcelService {

    private final EquipmentRepository repository;

    public ApachePoiExcelService(EquipmentRepository repository) {
        this.repository = repository;
    }

    @Override
    public void importEquipment(String filePath) {
        try (FileInputStream fis = new FileInputStream(filePath); Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet("Inventory");
            if (sheet == null) {
                throw new IllegalStateException("Sheet 'Inventory' not found");
            }

            for (Row row : sheet) {
                if (row.getRowNum() == 0) {
                    continue; // skip header
                }

                UUID id = UUID.fromString(row.getCell(0).getStringCellValue());
                String name = row.getCell(1).getStringCellValue();
                String type = row.getCell(2).getStringCellValue();

                int timesUsed = (int) row.getCell(3).getNumericCellValue();
                EquipmentStatus equipmentStatus
                        = EquipmentStatus.valueOf(row.getCell(4).getStringCellValue());
                InspectionStatus inspectionStatus
                        = InspectionStatus.valueOf(row.getCell(5).getStringCellValue());

                Equipment equipment = createEquipment(
                        type,
                        name,
                        id,
                        timesUsed,
                        equipmentStatus,
                        inspectionStatus
                );

                repository.save(equipment);
            }

        } catch (IOException e) {
            throw new RuntimeException("Failed to import equipment from Excel", e);
        }

    }

    @Override
    public void exportEquipment(String filePath) {
        try (Workbook workbook = new XSSFWorkbook(); FileOutputStream fos = new FileOutputStream(filePath)) {

            Sheet sheet = workbook.createSheet("Inventory");

            // Header row
            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("UUID");
            header.createCell(1).setCellValue("Name");
            header.createCell(2).setCellValue("Type");
            header.createCell(3).setCellValue("Times Used");
            header.createCell(4).setCellValue("Status");
            header.createCell(5).setCellValue("Inspection Status");

            int rowNum = 1;
            for (Equipment equipment : repository.findAll()) {
                Row row = sheet.createRow(rowNum++);

                row.createCell(0).setCellValue(equipment.getId().toString());
                row.createCell(1).setCellValue(equipment.getName());
                row.createCell(2).setCellValue(
                        equipment.getClass().getSimpleName().toUpperCase()
                );
                row.createCell(3).setCellValue(equipment.getTimesUsed());
                row.createCell(4).setCellValue(
                        equipment.getEquipmentStatus().name()
                );
                row.createCell(5).setCellValue(
                        equipment.getInspectionStatus().name()
                );
            }

            workbook.write(fos);

        } catch (IOException e) {
            throw new RuntimeException("Failed to export equipment to Excel", e);
        }
    }

    /**
     * Factory method for creating Equipment subclasses from Excel.
     */
    private Equipment createEquipment(
            String type,
            String name,
            UUID id,
            int timesUsed,
            EquipmentStatus equipmentStatus,
            InspectionStatus inspectionStatus
    ) {
        switch (type.toUpperCase()) {
            case "HARDWARE" -> {
                return new Hardware(id, name, timesUsed, equipmentStatus, inspectionStatus);
            }
            case "APPARATUS" -> {
                return new Apparatus(id, name, timesUsed, equipmentStatus, inspectionStatus);
            }
            case "PROP" -> {
                return new Prop(id, name, timesUsed, equipmentStatus, inspectionStatus);
            }
            default ->
                throw new IllegalArgumentException("Unknown equipment type: " + type);
        }
    }

}
