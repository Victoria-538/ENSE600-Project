/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cims.ui.view;

import com.cims.model.domain.*;
import com.cims.model.service.*;
import com.cims.ui.controller.CheckoutController;
import java.awt.BorderLayout;
import java.util.Collection;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author gggob
 */
public class CheckoutManagementFrame extends JFrame {

    private final ApplicationContext context;
    private final UserSession session;

    private final CheckoutController controller;
    private final InventoryService inventoryService;

    private JTable equipmentTable;
    private JTextField txtSearch;

    private JButton btnSearch;

    private JButton btnCheckout;
    private JButton btnCheckin;
    private JButton btnFlagFaulty;
    private JButton btnBack;

    public CheckoutManagementFrame(ApplicationContext context) {

        this.context = context;
        this.session = context.getSession();

        this.inventoryService = context.getInventoryService();

        this.controller = new CheckoutController(
                context.getCheckoutService(),
                context.getInventoryService(),
                context.getInspectionService(),
                session,
                this
        );

        initialiseFrame();
        initialiseComponents();
        buildLayout();
        registerListeners();
        loadEquipment();
    }

    private void initialiseFrame() {
        setTitle("Checkout Management");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void initialiseComponents() {

        txtSearch = new JTextField(20);

        btnSearch = new JButton("Search");

        btnCheckout = new JButton("Checkout");
        btnCheckin = new JButton("Check In");
        btnFlagFaulty = new JButton("Report Fault");

        btnBack = new JButton("Back");

        DefaultTableModel model = new DefaultTableModel() {
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
    }

    private void buildLayout() {

        JPanel main = new JPanel(new BorderLayout(10, 10));

        JPanel top = new JPanel();
        top.add(btnSearch);
        top.add(txtSearch);

        JPanel bottom = new JPanel();
        bottom.add(btnCheckout);
        bottom.add(btnCheckin);
        bottom.add(btnFlagFaulty);
        bottom.add(btnBack);

        main.add(top, BorderLayout.NORTH);
        main.add(new JScrollPane(equipmentTable), BorderLayout.CENTER);
        main.add(bottom, BorderLayout.SOUTH);

        add(main);
    }

    private void registerListeners() {

        btnSearch.addActionListener(e
                -> loadEquipment(inventoryService.search(txtSearch.getText().trim()))
        );

        btnCheckout.addActionListener(e -> {

            controller.checkout(
                    getSelectedEquipment());
            loadEquipment();
        });

        btnCheckin.addActionListener(e -> {

            controller.checkin(
                    getSelectedEquipment());
            loadEquipment();
        });

        btnFlagFaulty.addActionListener(e -> {

            controller.flagFault(
                    getSelectedEquipment());
            loadEquipment();
        });

        btnBack.addActionListener(e -> {
            new MainMenuFrame(context).setVisible(true);
            dispose();
        });
    }

    private void loadEquipment() {
        loadEquipment(inventoryService.listAll());
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

    private Equipment getSelectedEquipment() {

        int row = equipmentTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select equipment first.");
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

}
