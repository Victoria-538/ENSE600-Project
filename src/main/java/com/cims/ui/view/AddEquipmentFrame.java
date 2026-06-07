/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cims.ui.view;

import com.cims.model.domain.*;
import com.cims.model.service.InventoryService;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.JFrame;
import javax.swing.*;

/**
 *
 * @author gggob
 */
public class AddEquipmentFrame extends JFrame {

    private final ApplicationContext context;
    private final InventoryService inventoryService;

    private JTextField txtName;

    private JRadioButton rbHardware;
    private JRadioButton rbApparatus;
    private JRadioButton rbProp;

    private ButtonGroup typeGroup;

    private JButton btnSave;
    private JButton btnCancel;

    public AddEquipmentFrame(ApplicationContext context) {
        this.context = context;
        this.inventoryService = context.getInventoryService();

        initialiseFrame();
        initialiseComponents();
        buildLayout();
        registerListeners();
    }

    private void initialiseFrame() {

        setTitle("Add Equipment");
        setSize(400, 300);
        setLocationRelativeTo(null);
    }

    private void initialiseComponents() {

        txtName = new JTextField(20);

        rbHardware = new JRadioButton("Hardware");
        rbApparatus = new JRadioButton("Apparatus");
        rbProp = new JRadioButton("Prop");

        typeGroup = new ButtonGroup();

        typeGroup.add(rbHardware);
        typeGroup.add(rbApparatus);
        typeGroup.add(rbProp);

        btnSave = new JButton("Save");
        btnCancel = new JButton("Cancel");
    }

    private void buildLayout() {

        JPanel mainPanel
                = new JPanel(new BorderLayout(10, 10));

        JPanel formPanel
                = new JPanel(new GridLayout(4, 1));

        JPanel namePanel = new JPanel();

        namePanel.add(new JLabel("Name:"));
        namePanel.add(txtName);

        formPanel.add(namePanel);
        formPanel.add(rbHardware);
        formPanel.add(rbApparatus);
        formPanel.add(rbProp);

        JPanel buttonPanel = new JPanel();

        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);

        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        15, 15, 15, 15));

        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void registerListeners() {

        btnSave.addActionListener(e -> saveEquipment());

        btnCancel.addActionListener(e -> dispose());
    }

    private void saveEquipment() {

        String name = txtName.getText().trim();

        if (name.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter equipment name.");

            return;
        }

        Equipment equipment = createEquipment(name);

        if (equipment == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select equipment type.");

            return;
        }

        try {

            inventoryService.addEquipment(equipment);

            dispose();

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage()
            );
        }
    }

    private Equipment createEquipment(String name) {

        if (rbHardware.isSelected()) {
            return new Hardware(name);
        }

        if (rbApparatus.isSelected()) {
            return new Apparatus(name);
        }

        if (rbProp.isSelected()) {
            return new Prop(name);
        }

        return null;
    }
}
