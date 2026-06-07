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
import java.util.Set;
import java.util.UUID;

/**
 *
 * @author gggob
 */
public class ReportManagementFrame extends JFrame {

    private final ApplicationContext context;
    private final ReportManagementController controller;

    private JTable equipmentTable;

    private JButton btnRefresh;
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
        setSize(900, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void initialiseComponents() {

        btnRefresh = new JButton("Refresh");
        btnGenerateReport = new JButton("Generate Report");
        btnViewAudit = new JButton("View Audit Log");
        btnBack = new JButton("Back");

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

        main.add(new JScrollPane(equipmentTable), BorderLayout.CENTER);

        JPanel bottom = new JPanel(new BorderLayout());

        JPanel left = new JPanel();
        left.add(btnGenerateReport);
        left.add(btnViewAudit);

        JPanel right = new JPanel();
        right.add(btnRefresh);
        right.add(btnBack);

        bottom.add(left, BorderLayout.WEST);
        bottom.add(right, BorderLayout.EAST);

        main.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        main.add(bottom, BorderLayout.SOUTH);

        add(main);
    }

    private void registerListeners() {

        btnRefresh.addActionListener(e -> loadEquipment());

        btnGenerateReport.addActionListener(e -> {

            Set<java.util.UUID> ids
                    = controller.extractSelectedIds(equipmentTable.getSelectedRows(), equipmentTable);

            InventoryReport report = controller.generateReport(ids);

            String text = controller.formatInventoryReport(report);

            JTextArea textArea = new JTextArea(text);
            textArea.setEditable(false);
            textArea.setFont(new Font(
                    Font.MONOSPACED,
                    Font.PLAIN,
                    12));

            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(600, 500));

            int choice = JOptionPane.showConfirmDialog(
                    this,
                    scrollPane,
                    "Inventory Report",
                    JOptionPane.YES_NO_OPTION
            );

            if (choice == JOptionPane.YES_OPTION) {
                controller.saveToFile(text, "inventory_report.txt");
            }
        });

        btnViewAudit.addActionListener(e -> {

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
        });
        btnBack.addActionListener(e -> {
            new MainMenuFrame(context).setVisible(true);
            dispose();
        });
    }

    private void loadEquipment() {

        DefaultTableModel model = (DefaultTableModel) equipmentTable.getModel();
        model.setRowCount(0);

        for (Equipment e : controller.getAllEquipment()) {

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
