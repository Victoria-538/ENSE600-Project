/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cims.ui.view;

import com.cims.model.domain.*;
import com.cims.model.service.ReportServiceImpl;

import com.cims.ui.controller.ReportManagementController;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Collection;
import java.util.Set;
import java.util.UUID;

/**
 *
 * @author gggob
 */
public class ReportManagementFrame extends JFrame {

    private final ApplicationContext context;
    private final ReportManagementController controller;

    private JTextField txtSearch;

    private JComboBox<String> cmbType;
    private JComboBox<EquipmentStatus> cmbEquipmentStatus;
    private JComboBox<InspectionStatus> cmbInspectionStatus;

    private JButton btnSearch;
    private JTable equipmentTable;

    private JButton btnGenerateReport;
    private JButton btnViewAudit;
    private JButton btnBack;

    public ReportManagementFrame(ApplicationContext context) {

        this.context = context;

        this.controller = new ReportManagementController(
                context.getInventoryService(),
                context.getAuditLogService(),
                new ReportServiceImpl(context.getEquipmentRepository())
        );

        initialiseFrame();
        initialiseComponents();
        buildLayout();
        registerListeners();
        loadEquipment();
    }

    private void initialiseFrame() {
        setTitle("Report Management");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void initialiseComponents() {

        btnGenerateReport = new JButton("Generate Report");
        btnViewAudit = new JButton("View Audit Log");
        btnBack = new JButton("Back");
        txtSearch = new JTextField(20);

        btnSearch = new JButton("Search");

        cmbType = new JComboBox<>(new String[]{
            "All",
            "Hardware",
            "Apparatus",
            "Prop"
        });

        cmbEquipmentStatus = new JComboBox<>();
        cmbEquipmentStatus.addItem(null);

        for (EquipmentStatus s : EquipmentStatus.values()) {
            cmbEquipmentStatus.addItem(s);
        }

        cmbInspectionStatus = new JComboBox<>();
        cmbInspectionStatus.addItem(null);

        for (InspectionStatus s : InspectionStatus.values()) {
            cmbInspectionStatus.addItem(s);
        }
        DefaultTableModel model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        model.addColumn("ID");
        model.addColumn("Name");
        model.addColumn("Type");
        model.addColumn("Status");
        model.addColumn("Inspection");
        model.addColumn("Uses");

        equipmentTable = new JTable(model);
        equipmentTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        // hide ID column
        equipmentTable.getColumnModel().getColumn(0).setMinWidth(0);
        equipmentTable.getColumnModel().getColumn(0).setMaxWidth(0);
        equipmentTable.getColumnModel().getColumn(0).setPreferredWidth(0);
    }

    private void buildLayout() {

        JPanel main = new JPanel(new BorderLayout(10, 10));

        JPanel top = new JPanel(new BorderLayout());

        JPanel filterPanel = new JPanel();

        filterPanel.add(new JLabel("Type:"));
        filterPanel.add(cmbType);

        filterPanel.add(new JLabel("Status:"));
        filterPanel.add(cmbEquipmentStatus);

        filterPanel.add(new JLabel("Inspection:"));
        filterPanel.add(cmbInspectionStatus);

        JPanel searchPanel = new JPanel();

        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(txtSearch);
        searchPanel.add(btnSearch);

        top.add(filterPanel, BorderLayout.WEST);
        top.add(searchPanel, BorderLayout.EAST);

        main.add(top, BorderLayout.NORTH);
        main.add(new JScrollPane(equipmentTable), BorderLayout.CENTER);

        JPanel bottom = new JPanel(new BorderLayout());

        JPanel left = new JPanel();
        left.add(btnGenerateReport);
        left.add(btnViewAudit);

        JPanel right = new JPanel();

        right.add(btnBack);

        bottom.add(left, BorderLayout.WEST);
        bottom.add(right, BorderLayout.EAST);

        main.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        main.add(bottom, BorderLayout.SOUTH);

        add(main);
    }

    private void registerListeners() {

        btnSearch.addActionListener(e -> applyFilters());

        cmbType.addActionListener(e -> applyFilters());

        cmbEquipmentStatus.addActionListener(e -> applyFilters());

        cmbInspectionStatus.addActionListener(e -> applyFilters());
        btnGenerateReport.addActionListener(e -> generateInventoryReport());

        btnViewAudit.addActionListener(e -> viewAuditLog());

        btnBack.addActionListener(e -> {
            new MainMenuFrame(context).setVisible(true);
            dispose();
        });
    }

    private void loadEquipment() {

        loadEquipment(controller.getAllEquipment());
    }

    private void generateInventoryReport() {

        Set<UUID> ids
                = controller.extractSelectedIds(
                        equipmentTable.getSelectedRows(),
                        equipmentTable);

        InventoryReport report
                = controller.generateReport(ids);

        String text
                = controller.formatInventoryReport(report);

        JTextArea area = new JTextArea(text);
        area.setEditable(false);
        area.setCaretPosition(0);

        JScrollPane scrollPane
                = new JScrollPane(area);

        scrollPane.setPreferredSize(
                new Dimension(300, 500));

        JOptionPane.showMessageDialog(
                this,
                scrollPane,
                "Inventory Report",
                JOptionPane.INFORMATION_MESSAGE);

        int choice = JOptionPane.showConfirmDialog(
                this,
                "Save inventory report to file?",
                "Save Inventory Report",
                JOptionPane.YES_NO_OPTION);

        if (choice == JOptionPane.YES_OPTION) {

            controller.saveToFile(
                    text,
                    "inventory_report.txt");
        }
    }

    private void viewAuditLog() {
        Set<UUID> ids
                = controller.extractSelectedIds(
                        equipmentTable.getSelectedRows(),
                        equipmentTable);

        String text
                = controller.formatAuditLogs(
                        controller.getAuditLogs(ids));

        JTextArea area = new JTextArea(text);
        area.setEditable(false);
        area.setCaretPosition(0);

        JScrollPane scrollPane
                = new JScrollPane(area);

        scrollPane.setPreferredSize(
                new Dimension(700, 400));

        JOptionPane.showMessageDialog(
                this,
                scrollPane,
                "Audit Log",
                JOptionPane.INFORMATION_MESSAGE);

        int choice = JOptionPane.showConfirmDialog(
                this,
                "Save audit log to file?",
                "Save Audit Log",
                JOptionPane.YES_NO_OPTION);

        if (choice == JOptionPane.YES_OPTION) {
            controller.saveToFile(
                    text,
                    "audit_log.txt");
        }

    }

    private void applyFilters() {

        String type = (String) cmbType.getSelectedItem();

        if ("All".equals(type)) {
            type = null;
        }

        EquipmentStatus equipmentStatus
                = (EquipmentStatus) cmbEquipmentStatus.getSelectedItem();

        InspectionStatus inspectionStatus
                = (InspectionStatus) cmbInspectionStatus.getSelectedItem();

        Collection<Equipment> results
                = context.getInventoryService().filter(
                        type,
                        equipmentStatus,
                        inspectionStatus);

        String searchTerm = txtSearch.getText().trim();

        if (!searchTerm.isBlank()) {

            results = results.stream()
                    .filter(e
                            -> e.getName()
                            .toLowerCase()
                            .contains(searchTerm.toLowerCase()))
                    .toList();
        }

        loadEquipment(results);
    }

    private void loadEquipment(Collection<Equipment> list) {

        DefaultTableModel model
                = (DefaultTableModel) equipmentTable.getModel();

        model.setRowCount(0);

        for (Equipment e : list) {

            model.addRow(new Object[]{
                e.getId(),
                e.getName(),
                e.getEquipmentType(),
                e.getEquipmentStatus(),
                e.getInspectionStatus(),
                e.getTimesUsed()
            });
        }
    }
}
