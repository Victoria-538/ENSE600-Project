/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cims.ui.controller;

import com.cims.model.domain.*;
import com.cims.model.service.*;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import javax.swing.JTable;

/**
 *
 * @author gggob
 */
  

    public class ReportManagementController {

        private final InventoryService inventoryService;
        private final AuditLogService auditLogService;
        private final ReportService reportService;

        public ReportManagementController(
                InventoryService inventoryService,
                AuditLogService auditLogService,
                ReportService reportService) {

            this.inventoryService = inventoryService;
            this.auditLogService = auditLogService;
            this.reportService = reportService;
        }

        /* =========================
       EQUIPMENT DATA
       ========================= */
        public Collection<Equipment> getAllEquipment() {
            return inventoryService.listAll();
        }

        /* =========================
       REPORTS
       ========================= */
        public InventoryReport generateReport(Set<UUID> ids) {

            if (ids == null || ids.isEmpty()) {
                return reportService.generateInventoryReport();
            }

            return generateFilteredReport(ids);
        }

        private InventoryReport generateFilteredReport(Set<UUID> ids) {

            InventoryReport report = new InventoryReport();

            for (Equipment equipment : inventoryService.listAll()) {

                if (!ids.contains(equipment.getId())) {
                    continue;
                }

                report.totalEquipment++;

                if (equipment instanceof Hardware) {
                    report.hardwareCount++;
                } else if (equipment instanceof Apparatus) {
                    report.apparatusCount++;
                } else if (equipment instanceof Prop) {
                    report.propCount++;
                }

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

        public String formatInventoryReport(InventoryReport r) {

            return """
=======================================
       CIMS INVENTORY REPORT
=======================================

SUMMARY
---------------------------------------
Total Equipment          : %d

EQUIPMENT TYPES
---------------------------------------
Hardware                 : %d
Apparatus                : %d
Props                    : %d

EQUIPMENT STATUS
---------------------------------------
Available                : %d
Checked Out              : %d
Faulty                   : %d
Under Inspection         : %d

INSPECTION STATUS
---------------------------------------
Not Due                  : %d
Due                      : %d
Flagged Faulty           : %d

=======================================
End of Report
=======================================
""".formatted(
                    r.totalEquipment,
                    r.hardwareCount,
                    r.apparatusCount,
                    r.propCount,
                    r.availableCount,
                    r.checkedOutCount,
                    r.faultyCount,
                    r.underInspectionCount,
                    r.notDueCount,
                    r.dueCount,
                    r.flaggedFaultyCount
            );
        }

/* =========================
   AUDIT LOGS
   ========================= */
public Collection<AuditLog> getAuditLogs(Set<UUID> ids) {

    Collection<AuditLog> logs = auditLogService.getAllLogs();

    return logs.stream()
            .filter(log ->
                    ids == null
                    || ids.isEmpty()
                    || ids.contains(log.getEquipmentId())
            )
            .sorted(
                    Comparator.comparing(
                            AuditLog::getTimestamp
                    ).reversed()
            )
            .toList();
}

    public String formatAuditLogs(Collection<AuditLog> logs) {

        StringBuilder sb = new StringBuilder();

        sb.append("""
==================================================
                 CIMS AUDIT LOG
==================================================

""");

        int recordNumber = 1;

        for (AuditLog log : logs) {

            sb.append("Record #")
                    .append(recordNumber++)
                    .append("\n");

            sb.append("--------------------------------------------------\n");

            sb.append("Timestamp : ")
                    .append(log.getTimestamp())
                    .append("\n");

            sb.append("Equipment : ")
                    .append(log.getEquipmentName())
                    .append("\n");

            sb.append("Equipment ID : ")
                    .append(log.getEquipmentId())
                    .append("\n");

            sb.append("User      : ")
                    .append(log.getUserName())
                    .append(" (")
                    .append(log.getRole())
                    .append(")\n");

            sb.append("Action    : ")
                    .append(log.getActionType())
                    .append("\n");

            sb.append("Notes     : ")
                    .append(
                            log.getNotes() == null
                            || log.getNotes().isBlank()
                            ? "None"
                            : log.getNotes()
                    )
                    .append("\n\n");
        }

        sb.append("""
==================================================
End of Audit Log
==================================================
""");

        return sb.toString();
    }

    /* =========================
       TABLE HELPERS
       ========================= */
    public Set<UUID> extractSelectedIds(
            int[] rows,
            JTable table) {

        Set<UUID> ids = new HashSet<>();

        for (int row : rows) {

            String id
                    = table.getValueAt(row, 0).toString();

            ids.add(UUID.fromString(id));
        }

        return ids;
    }

    /* =========================
       FILE EXPORT
       ========================= */
    public void saveToFile(
            String content,
            String filePath) {

        try (FileWriter writer
                = new FileWriter(filePath)) {

            writer.write(content);

        } catch (IOException ex) {

            throw new RuntimeException(
                    "Error saving file: "
                    + ex.getMessage(),
                    ex
            );
        }
    }
}
