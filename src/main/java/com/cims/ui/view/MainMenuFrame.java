/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cims.ui.view;

import com.cims.model.domain.*;
import com.cims.model.service.*;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.*;

/**
 *
 * @author gggob
 */
public class MainMenuFrame extends JFrame {

    private final ApplicationContext context;
    private final UserSession session;

    private JLabel lblUser;
    private JLabel lblRole;

    private JButton btnEquipment;
    private JButton btnCheckout;
    private JButton btnInspection;
    private JButton btnAudit;
    private JButton btnLogout;

    public MainMenuFrame(ApplicationContext context) {
        this.context = context;
        this.session = context.getSession();

        initialiseFrame();
        initialiseComponents();
        applyPermissions();
        buildLayout();
        registerListeners();
    }

    private void initialiseFrame() {
        setTitle("CIMS Main Menu");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void initialiseComponents() {

        lblUser = new JLabel("Welcome: " + session.getUserName());
        lblRole = new JLabel("Role: " + session.getRole());

        btnEquipment = new JButton("Equipment Management");
        btnCheckout = new JButton("Checkout / Check In");
        btnInspection = new JButton("Inspections");
        btnAudit = new JButton("Audit History");

        btnLogout = new JButton("Logout");
    }

    private void buildLayout() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        JPanel centrePanel = new JPanel(new GridLayout(4, 1, 5, 5));

        centrePanel.add(btnEquipment);
        centrePanel.add(btnCheckout);
        centrePanel.add(btnInspection);
        centrePanel.add(btnAudit);

        JPanel topPanel = new JPanel(new GridLayout(2, 1));

        topPanel.add(lblUser);
        topPanel.add(lblRole);

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(btnLogout);

        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(centrePanel, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void applyPermissions() {
        UserRole role = session.getRole();

        boolean canCheckout
                = role == UserRole.RIGGER
                || role == UserRole.PERFORMER;

        boolean canInspect
                = role == UserRole.ENGINEER
                || role == UserRole.INVENTORY_MANAGER;

        btnCheckout.setEnabled(canCheckout);
        btnInspection.setEnabled(canInspect);
    }

    private void registerListeners() {

        btnEquipment.addActionListener(e -> {
            EquipmentManagementFrame frame
                    = new EquipmentManagementFrame(context);

            frame.setVisible(true);

            dispose();
        });

        btnCheckout.addActionListener(e -> {
            CheckoutManagementFrame frame
                    = new CheckoutManagementFrame(context);

            frame.setVisible(true);
            dispose();
        });

        btnInspection.addActionListener(e -> {
             InspectionManagementFrame frame
                    = new InspectionManagementFrame(context);

            frame.setVisible(true);
            dispose();
        });

        btnAudit.addActionListener(e -> {
            ReportManagementFrame frame
                    = new ReportManagementFrame(context);

            frame.setVisible(true);
            dispose();
        });
        btnLogout.addActionListener(e -> logout());
    }

    private void logout() {
        LoginFrame loginFrame
                = new LoginFrame(
                        context.getEquipmentRepository(),
                        context.getAuditRepository());

        loginFrame.setVisible(true);
        dispose();

        dispose();
    }

}
