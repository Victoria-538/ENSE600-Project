/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cims.ui.view;

import com.cims.model.domain.*;
import com.cims.model.service.InventoryService;
import com.cims.ui.controller.EquipmentManagementController;
import java.awt.BorderLayout;
import java.util.Collection;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author gggob
 */
public class EquipmentManagementFrame extends JFrame {

    private final UserSession session;
    private final EquipmentManagementController controller;
    private final ApplicationContext context;

    private JTable equipmentTable;
    private JScrollPane scrollPane;

    private JTextField txtSearch;

    private JButton btnSearch;
    private JButton btnAdd;
    private JButton btnRemove;
    private JButton btnBack;

    public EquipmentManagementFrame(ApplicationContext context) {
        this.context = context;
        this.session = context.getSession();

        this.controller = new EquipmentManagementController(
                context.getInventoryService()
        );

        initialiseFrame();
        initialiseComponents();
        applyPermissions();
        buildLayout();
        registerListeners();
        refreshEquipmentTable();
    }

    private void initialiseFrame() {
        setTitle("Equipment Management");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void initialiseComponents() {

        txtSearch = new JTextField(20);

        btnSearch = new JButton("Search");
        btnAdd = new JButton("Add");
        btnRemove = new JButton("Remove");
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
        model.addColumn("Times Used");

        equipmentTable = new JTable(model);

        equipmentTable.getColumnModel().getColumn(0).setMinWidth(0);
        equipmentTable.getColumnModel().getColumn(0).setMaxWidth(0);
        equipmentTable.getColumnModel().getColumn(0).setPreferredWidth(0);

        scrollPane = new JScrollPane(equipmentTable);
    }

    private void buildLayout() {

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));

        JPanel searchPanel = new JPanel();
        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(txtSearch);
        searchPanel.add(btnSearch);

        JPanel bottomPanel = new JPanel(new BorderLayout());

        JPanel left = new JPanel();
        left.add(btnAdd);
        left.add(btnRemove);

        JPanel right = new JPanel();
        right.add(btnBack);

        bottomPanel.add(left, BorderLayout.WEST);
        bottomPanel.add(right, BorderLayout.EAST);

        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        mainPanel.add(searchPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void registerListeners() {

        btnAdd.addActionListener(e -> {
            AddEquipmentFrame frame = new AddEquipmentFrame(context);
            frame.setVisible(true);

            frame.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosed(java.awt.event.WindowEvent e) {
                    refreshEquipmentTable();
                }
            });
        });

        btnRemove.addActionListener(e -> removeSelectedEquipment());

        btnBack.addActionListener(e -> {
            new MainMenuFrame(context).setVisible(true);
            dispose();
        });

        btnSearch.addActionListener(e -> performSearch());
    }

    private void refreshEquipmentTable() {
        loadEquipment(controller.getAllEquipment());
    }

    private void performSearch() {
        loadEquipment(controller.search(txtSearch.getText()));
    }

    private void loadEquipment(Collection<Equipment> list) {

        DefaultTableModel model = (DefaultTableModel) equipmentTable.getModel();
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

    private void removeSelectedEquipment() {

        int row = equipmentTable.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select equipment first.");
            return;
        }

        String id = equipmentTable.getValueAt(row, 0).toString();

        Equipment selected = controller.getAllEquipment().stream()
                .filter(e -> e.getId().toString().equals(id))
                .findFirst()
                .orElse(null);

        if (selected == null) return;

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Remove " + selected.getName() + "?",
                "Confirm",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) return;

        controller.removeEquipment(selected);

        refreshEquipmentTable();
    }

    private void applyPermissions() {

        UserRole role = session.getRole();

        btnAdd.setEnabled(role == UserRole.INVENTORY_MANAGER);
        btnRemove.setEnabled(role == UserRole.INVENTORY_MANAGER);
    }
}
