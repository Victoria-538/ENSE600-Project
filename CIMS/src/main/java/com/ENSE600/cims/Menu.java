/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ENSE600.cims;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

/**
 *
 * @author gggob
 */
public class Menu {

    private final Scanner scanner = new Scanner(System.in);

    private final InventoryService inventoryService;
    private final CheckoutService checkoutService;
    private final InspectionService inspectionService;

    public Menu(InventoryService inventoryService,
            CheckoutService checkoutService,
            InspectionService inspectionService) {
        this.inventoryService = inventoryService;
        this.checkoutService = checkoutService;
        this.inspectionService = inspectionService;
    }

    /**
     * Entry point for all CUI interaction
     */
    public void start(UserSession session) {
        switch (session.getRole()) {
            case RIGGER, PERFORMER:
                showBasicUserMenu(session);
                break;
            case ENGINEER:
                showEngineerMenu(session);
                break;
            case INVENTORY_MANAGER:
                showInventoryManagerMenu(session);
                break;
            default:
                System.out.println("Unknown role.");
        }
    }

    private void showBasicUserMenu(UserSession session) {
        while (true) {
            System.out.println("\n--- User Menu ---");
            System.out.println("1. Check out equipment");
            System.out.println("2. Check in equipment");
            System.out.println("3. Flag equipment for inspection");
            System.out.println("0. Exit");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1" ->
                    handleCheckout(session);
                case "2" ->
                    handleCheckin(session);
                case "3" ->
                    handleFlagInspection(session);
                case "0" -> {
                    return;
                }
                default ->
                    System.out.println("Invalid option.");
            }
        }
    }

    /* ---------- Shared handlers ---------- */
    private void handleCheckout(UserSession session) {

        while (true) {
            Equipment selected = promptForEquipmentSelection(inventoryService.listAll());

            if (selected == null) {
                System.out.println("Operation cancelled.");
                return;
            }

            try {
                checkoutService.checkOut(selected.getId(), session);
                System.out.println(
                        "Equipment '" + selected.getName() + "' checked out successfully."
                );
                return; // successful checkout → exit handler

            } catch (SecurityException e) {
                // Role is not allowed to check out this equipment
                System.out.println("Access denied: " + e.getMessage());
                System.out.println("Please select a different item.");

            } catch (IllegalStateException e) {
                // Equipment not available / requires inspection
                System.out.println("Cannot check out equipment: " + e.getMessage());
                System.out.println("Please select a different item.");
            }

        }
    }

    private void handleCheckin(UserSession session) {

        while (true) {
            Equipment selected = promptForEquipmentSelection(
                    inventoryService.listAll()
            );

            if (selected == null) {
                System.out.println("Check-in cancelled.");
                return;
            }

            try {
                checkoutService.checkIn(selected.getId(), session);
                System.out.println("Equipment checked in successfully.");
                return;

            } catch (IllegalStateException e) {
                System.out.println("Cannot check in equipment: " + e.getMessage());
                System.out.println("Please select a different item.");
            }
        }

    }

    private void handleFlagInspection(UserSession session) {

        Equipment selected = promptForEquipmentSelection(
                inventoryService.listAll());

        if (selected == null) {
            return;
        }
        inspectionService.flagForInspection(selected.getId(), session);
        System.out.println("Equipment flagged for inspection.");

    }

    private void showEngineerMenu(UserSession session) {

        while (true) {
            System.out.println("\n--- Engineer Menu ---");
            System.out.println("1. View equipment due for inspection");
            System.out.println("2. View equipment flagged faulty");
            System.out.println("0. Exit");

            System.out.print("Select an option: ");
            String choice = scanner.nextLine();

            switch (choice) {

                case "1" ->
                    handleInspection(session, InspectionStatus.DUE);
                case "2" ->
                    handleInspection(session, InspectionStatus.FLAGGED_FAULTY);
                case "0" -> {
                    return;
                }
                default ->
                    System.out.println("Invalid option. Please try again.");
            }
        }

    }

    private void handleInspection(UserSession session, InspectionStatus status) {

        List<Equipment> candidates = inventoryService.listAll().stream()
                .filter(e -> e.getInspectionStatus() == status)
                .toList();

        if (candidates.isEmpty()) {
            System.out.println("No equipment found with inspection status: " + status);
            return;
        }

        while (true) {

            Equipment selected = promptForEquipmentSelection(candidates);

            // User cancelled
            if (selected == null) {
                System.out.println("Inspection cancelled.");
                return;
            }

            try {
                processInspectionDecision(session, selected);
                return; // success or discard → exit inspection flow

            } catch (SecurityException e) {
                System.out.println("⚠️  Access denied: " + e.getMessage());
                System.out.println("Please select a different item.");

            } catch (IllegalStateException e) {
                System.out.println("⚠️  Cannot inspect equipment: " + e.getMessage());
                System.out.println("Please select a different item.");
            }
        }

    }

    private void processInspectionDecision(UserSession session, Equipment equipment) {
        System.out.println("\nInspecting: " + equipment.getName());
        System.out.println("1. Mark safe (return to service)");
        System.out.println("2. Discard equipment");
        System.out.print("Select option: ");

        String choice = scanner.nextLine();

        try {
            switch (choice) {
                case "1" -> {
                    inspectionService.markSafe(equipment.getId(), session);
                    System.out.println("Equipment marked safe and returned to service.");
                }

                case "2" -> {
                    inventoryService.removeEquipment(equipment);
                    System.out.println("Equipment discarded and removed from inventory.");
                }

                default ->
                    System.out.println("Invalid selection.");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void showInventoryManagerMenu(UserSession session) {

        while (true) {
            System.out.println("\n--- Inventory Manager Menu ---");
            System.out.println("1. Add equipment");
            System.out.println("2. Remove equipment");
            System.out.println("3. View all equipment");
            System.out.println("4. View hardware");
            System.out.println("5. View apparatus");
            System.out.println("6. View props");
            System.out.println("0. Exit");

            System.out.print("Select an option: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1" ->
                    addEquipment();
                case "2" ->
                    removeEquipment();
                case "3" ->
                    listAllEquipment();
                case "4" ->
                    listHardware();
                case "5" ->
                    listApparatus();
                case "6" ->
                    listProps();
                case "0" -> {
                    System.out.println("Exiting Inventory Manager menu.");
                    return;
                }
                default ->
                    System.out.println("Invalid option. Please try again.");
            }
        }

    }

    private void addEquipment() {
        try {
            System.out.println("Select equipment type:");
            System.out.println("1. Hardware");
            System.out.println("2. Apparatus");
            System.out.println("3. Prop");

            String typeChoice = scanner.nextLine().trim();

            System.out.print("Enter equipment name: ");
            String name = scanner.nextLine();

            Equipment equipment = switch (typeChoice) {
                case "1" ->
                    new Hardware(name);
                case "2" ->
                    new Apparatus(name);
                case "3" ->
                    new Prop(name);
                default ->
                    null;
            };

            inventoryService.addEquipment(equipment);
            System.out.println("Equipment added successfully.");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void removeEquipment() {

        try {
            Equipment equipment = promptForEquipmentSelection(
                    inventoryService.listAll()
            );

            if (equipment == null) {
                System.out.println("Removal cancelled.");
                return;
            }

            System.out.printf(
                    "Are you sure you want to remove '%s' (%s)? (y/n): ",
                    equipment.getName(),
                    equipment.getClass().getSimpleName()
            );

            String confirmation = scanner.nextLine().trim();

            if (!confirmation.equalsIgnoreCase("y")) {
                System.out.println("Removal cancelled.");
                return;
            }

            inventoryService.removeEquipment(equipment);
            System.out.println("Equipment removed successfully.");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

    }

    private void listAllEquipment() {

        Collection<Equipment> all = inventoryService.listAll();

        if (all.isEmpty()) {
            System.out.println("Inventory is empty.");
            return;
        }

        System.out.println("\n--- All Equipment ---");
        all.forEach(this::printEquipment);

    }

    private void listHardware() {

        Collection<Hardware> hardware = inventoryService.listHardware();

        if (hardware.isEmpty()) {
            System.out.println("No hardware equipment found.");
            return;
        }

        System.out.println("\n--- Hardware ---");
        hardware.forEach(this::printEquipment);

    }

    private void listApparatus() {

        Collection<Apparatus> apparatus = inventoryService.listApparatus();

        if (apparatus.isEmpty()) {
            System.out.println("No apparatus equipment found.");
            return;
        }

        System.out.println("\n--- Apparatus ---");
        apparatus.forEach(this::printEquipment);

    }

    private void listProps() {

        Collection<Prop> props = inventoryService.listProps();

        if (props.isEmpty()) {
            System.out.println("No props found.");
            return;
        }

        System.out.println("\n--- Props ---");
        props.forEach(this::printEquipment);

    }

    private void printEquipment(Equipment e) {

        System.out.printf(
                "%s | %s | %s | Status: %s | Inspection: %s%n",
                getDisplayCode(e),
                e.getClass().getSimpleName(),
                e.getName(),
                e.getEquipmentStatus(),
                e.getInspectionStatus()
        );

    }

    private Equipment promptForEquipmentSelection(Collection<Equipment> equipmentList) {
        List<Equipment> list = new ArrayList<>(equipmentList);

        if (equipmentList.isEmpty()) {
            System.out.println("No equipment available.");
            return null;
        }

        System.out.println("\nSelect equipment:");
        for (int i = 0; i < list.size(); i++) {
            Equipment e = list.get(i);
            System.out.printf(
                    "%d. %s | %s | Status: %s | Inspection: %s%n",
                    i + 1,
                    getDisplayCode(e),
                    e.getName(),
                    e.getEquipmentStatus(),
                    e.getInspectionStatus()
            );
        }

        System.out.print("Enter number (or press Enter to cancel): ");
        String input = scanner.nextLine().trim();

        if (input.isEmpty()) {
            return null;
        }

        try {
            int choice = Integer.parseInt(input);
            if (choice < 1 || choice > list.size()) {
                throw new NumberFormatException();
            }
            return list.get(choice - 1);
        } catch (NumberFormatException e) {
            System.out.println("Invalid selection.");
            return null;
        }
    }

    private String getDisplayCode(Equipment equipment) {
        String prefix = equipment.getClass().getSimpleName().substring(0, 2).toUpperCase();
        String shortId = equipment.getId().toString().substring(0, 6).toUpperCase();
        return prefix + "-" + shortId;
    }

}
