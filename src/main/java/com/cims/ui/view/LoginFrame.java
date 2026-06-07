/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cims.ui.view;

import com.cims.model.domain.UserRole;
import com.cims.model.domain.UserSession;
import java.awt.event.ActionEvent;
import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import com.cims.model.repository.*;
import com.cims.model.service.*;


/**
 *
 * @author gggob
 */
public class LoginFrame extends JFrame {

    private final EquipmentRepository equipmentRepository;
    private final AuditLogRepository auditRepository;

    private JTextField txtName;

    private JRadioButton rbRigger;
    private JRadioButton rbPerformer;
    private JRadioButton rbEngineer;
    private JRadioButton rbInventoryManager;
    private ButtonGroup roleGroup;
    private JButton btnLogin;

    public LoginFrame(EquipmentRepository equipmentRepository, AuditLogRepository auditRepository) {
        this.equipmentRepository = equipmentRepository;
        this.auditRepository = auditRepository;

        initialiseFrame();
        initialiseComponents();
        buildLayout();
        registerListeners();
    }

    private void initialiseFrame() {
        setTitle("Circus Inventory Management System");
        setSize(450, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void initialiseComponents() {

        txtName = new JTextField(20);

        rbRigger = new JRadioButton("Rigger");
        rbPerformer = new JRadioButton("Performer");
        rbEngineer = new JRadioButton("Engineer");
        rbInventoryManager = new JRadioButton("Inventory Manager");

        roleGroup = new ButtonGroup();

        roleGroup.add(rbRigger);
        roleGroup.add(rbPerformer);
        roleGroup.add(rbEngineer);
        roleGroup.add(rbInventoryManager);

        btnLogin = new JButton("Login");
    }

    private void registerListeners() {

        btnLogin.addActionListener(this::btnLoginActionPerformed);
    }

    private void buildLayout() {

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));

        JPanel namePanel = new JPanel();
        namePanel.add(new JLabel("Name:"));
        namePanel.add(txtName);

        JPanel rolePanel = new JPanel(new GridLayout(4, 1));

        rolePanel.setBorder(
                BorderFactory.createTitledBorder("Select Role"));

        rolePanel.add(rbRigger);
        rolePanel.add(rbPerformer);
        rolePanel.add(rbEngineer);
        rolePanel.add(rbInventoryManager);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnLogin);

        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(15, 15, 15, 15));

        mainPanel.add(namePanel, BorderLayout.NORTH);
        mainPanel.add(rolePanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void btnLoginActionPerformed(ActionEvent evt) {
        String name = txtName.getText().trim();

        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter your name.");
            return;
        }

        UserRole role = getSelectedRole();

        if (role == null) {
            JOptionPane.showMessageDialog(this, "Please select a role.");
            return;
        }

        UserSession session = new UserSession(role, name);

// Create services
        AuditLogService auditLogService
                = new AuditLogServiceImpl(auditRepository);

        InventoryService inventoryService = new InventoryServiceImpl(
                equipmentRepository,
                session,
                auditLogService);

        CheckoutService checkoutService = new CheckoutServiceImpl(
                equipmentRepository,
                auditLogService);

        InspectionService inspectionService = new InspectionServiceImpl(
                equipmentRepository,
                auditLogService);

// Create application context
        ApplicationContext context = new ApplicationContext(
                session,
                inventoryService,
                checkoutService,
                inspectionService,
                auditLogService,
                equipmentRepository,
                auditRepository);

// Open main menu
        MainMenuFrame menu
                = new MainMenuFrame(context);

        menu.setVisible(true);

        dispose();

    }

    private UserRole getSelectedRole() {
        if (rbRigger.isSelected()) {
            return UserRole.RIGGER;
        }

        if (rbPerformer.isSelected()) {
            return UserRole.PERFORMER;
        }

        if (rbEngineer.isSelected()) {
            return UserRole.ENGINEER;
        }

        if (rbInventoryManager.isSelected()) {
            return UserRole.INVENTORY_MANAGER;
        }

        return null;
    }
}
