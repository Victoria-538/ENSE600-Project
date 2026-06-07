/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cims.ui.view;

import com.cims.model.domain.*;
import com.cims.model.service.*;
import com.cims.ui.controller.InspectionManagementController;
import java.awt.BorderLayout;
import java.util.Collection;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author gggob
 */
public class InspectionManagementFrame extends JFrame {

    private final ApplicationContext context;
    private final UserSession session;

    private final InventoryService inventoryService;
    private final InspectionManagementController controller;

    private JTable equipmentTable;
    private JScrollPane scrollPane;

    private JButton btnInspectSelected;
    private JButton btnBack;

    public InspectionManagementFrame(ApplicationContext context) {

        this.context = context;
        this.session = context.getSession();

        this.inventoryService = context.getInventoryService();

        this.controller = new InspectionManagementController(
                context.getInspectionService(),
                context.getInventoryService(),
                session,
                this
        );

        if (!controller.canAccess()) {
            JOptionPane.showMessageDialog(this, "Access denied.");
            dispose();
            return;
        }

        initialiseFrame();
        initialiseComponents();
        applyPermissions();
        buildLayout();
        registerListeners();
        loadEquipment();
    }

    private void initialiseFrame() {
        setTitle("Inspection Management");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void initialiseComponents() {

        btnInspectSelected = new JButton("Inspect Selected");
        btnBack = new JButton("Back");

        DefaultTableModel model = new DefaultTableModel() {

            @Override
            public boolean isCellEditable(int row, int column) {
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

        equipmentTable.getColumnModel().getColumn(0).setMinWidth(0);
        equipmentTable.getColumnModel().getColumn(0).setMaxWidth(0);
        equipmentTable.getColumnModel().getColumn(0).setPreferredWidth(0);

        scrollPane = new JScrollPane(equipmentTable);
    }

    private void buildLayout() {

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));

        JPanel topPanel = new JPanel();

        JPanel bottomPanel = new JPanel(new BorderLayout());

        JPanel leftPanel = new JPanel();
        leftPanel.add(btnInspectSelected);

        JPanel rightPanel = new JPanel();
        rightPanel.add(btnBack);
        
        bottomPanel.add(leftPanel, BorderLayout.WEST);
        bottomPanel.add(rightPanel, BorderLayout.EAST);

        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        10, 10, 10, 10)
        );

        mainPanel.add(
                topPanel,
                BorderLayout.NORTH);

        mainPanel.add(
                scrollPane,
                BorderLayout.CENTER);

        mainPanel.add(
                bottomPanel,
                BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void applyPermissions() {

        UserRole role = session.getRole();

        boolean isEngineer = role == UserRole.ENGINEER;
        boolean isInventoryManager = role == UserRole.INVENTORY_MANAGER;

        btnInspectSelected.setEnabled(isEngineer || isInventoryManager);
    }

    private void registerListeners() {

        btnInspectSelected.addActionListener(
                e -> inspectEquipment(
                        getSelectedEquipment()
                )
        );

        btnBack.addActionListener(e -> {

            new MainMenuFrame(context)
                    .setVisible(true);

            dispose();
        });
    }

    private void loadEquipment() {

        DefaultTableModel model
                = (DefaultTableModel) equipmentTable.getModel();

        model.setRowCount(0);

        for (Equipment e : controller.getEquipmentForRole()) {

            model.addRow(toRow(e));
        }
    }

    private Object[] toRow(Equipment e) {

        return new Object[]{
            e.getId(),
            e.getName(),
            e.getEquipmentType(),
            e.getEquipmentStatus(),
            e.getInspectionStatus(),
            e.getTimesUsed()
        };
    }

    private Equipment getSelectedEquipment() {

        int row = equipmentTable.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Select equipment."
            );
            return null;
        }

        String id = equipmentTable.getValueAt(row, 0).toString();

        for (Equipment e : inventoryService.listAll()) {

            if (e.getId().toString().equals(id)) {
                return e;
            }
        }

        return null;
    }

    private void inspectEquipment(
            Equipment equipment) {

        if (equipment == null) {
            return;
        }

        JTextArea txtNotes
                = new JTextArea(5, 30);

        JPanel panel
                = new JPanel(
                        new BorderLayout(5, 5));

        panel.add(
                new JLabel(
                        "Inspecting: "
                        + equipment.getName()),
                BorderLayout.NORTH);

        panel.add(
                new JScrollPane(txtNotes),
                BorderLayout.CENTER);

        Object[] options = {
            "Mark Safe",
            "Discard",
            "Cancel"
        };

        int choice
                = JOptionPane.showOptionDialog(
                        this,
                        panel,
                        "Equipment Inspection",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        options,
                        options[0]
                );

        String notes
                = txtNotes.getText().trim();

        switch (choice) {

            case 0:

                controller.markSafe(
                        equipment,
                        notes
                );

                loadEquipment();
                break;

            case 1:

                controller.discardEquipment(
                        equipment
                );

                loadEquipment();
                break;

            default:
                break;
        }
    }

}
