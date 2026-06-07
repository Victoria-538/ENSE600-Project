/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cims.ui.controller;
import com.cims.model.domain.*;
import com.cims.model.service.*;
import javax.swing.*;

/**
 *
 * @author gggob
 */
public class CheckoutController {
    
    private final CheckoutService checkoutService;
    private final InventoryService inventoryService;
    private final InspectionService inspectionService;
    private final UserSession session;
    private final JFrame parent;

    public CheckoutController(
            CheckoutService checkoutService,
            InventoryService inventoryService,
            InspectionService inspectionService,
            UserSession session,
            JFrame parent) {

        this.checkoutService = checkoutService;
        this.inventoryService = inventoryService;
        this.inspectionService = inspectionService;
        this.session = session;
        this.parent = parent;
    }

    // =========================
    // CHECKOUT
    // =========================
    public void checkout(Equipment equipment) {
        if (equipment == null) return;

        String note = promptNote("Checkout", equipment);

        try {
            checkoutService.checkOut(equipment.getId(), session, note);
           
        } catch (Exception ex) {
            showError(ex.getMessage());
        }
    }

    // =========================
    // CHECKIN
    // =========================
    public void checkin(Equipment equipment) {
        if (equipment == null) return;

        String note = promptNote("Check-in", equipment);

        try {
            checkoutService.checkIn(equipment.getId(), session, note);
            
        } catch (Exception ex) {
            showError(ex.getMessage());
        }
    }

    // =========================
    // FAULT
    // =========================
    public void flagFault(Equipment equipment) {
        if (equipment == null) return;

        String note = promptNote("Fault Report", equipment);

        try {
            inspectionService.flagForInspection(
                    equipment.getId(),
                    session,
                    note
            );
      
        } catch (Exception ex) {
            showError(ex.getMessage());
        }
    }

    // =========================
    // NOTE DIALOG
    // =========================
    private String promptNote(String action, Equipment equipment) {

        String input = JOptionPane.showInputDialog(
                parent,
                "Enter note for " + action + ":\n" + equipment.getName(),
                "Action Note",
                JOptionPane.QUESTION_MESSAGE
        );

        return (input == null) ? "" : input.trim();
    }

   

    private void showError(String msg) {
        JOptionPane.showMessageDialog(parent, msg);
    }
}
