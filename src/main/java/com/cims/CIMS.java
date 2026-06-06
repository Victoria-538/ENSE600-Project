/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.cims;

import com.cims.view.Menu;
import com.cims.view.ConsoleRoleSelector;
import com.cims.model.service.*;
import com.cims.model.repository.*;
import com.cims.model.domain.UserRole;
import com.cims.model.domain.UserSession;
import com.cims.database.*;
import com.cims.model.domain.*;
import java.util.Collection;
import java.util.Scanner;

/**
 *
 * @author gggob
 */
public class CIMS {

    public static void main(String[] args) {

        DatabaseInitialiser.initialise();
        Scanner scanner = new Scanner(System.in);
        System.out.println("=== Circus Inventory Management System ===");

        // 1. Create repository (single source of truth)
        EquipmentRepository repository = new DerbyEquipmentRepository();
        AuditLogRepository auditRepo = new DerbyAuditLogRepository();
        AuditLogService auditLogService = new AuditLogServiceImpl(auditRepo);
                      
        // 2. Main application loop 
        while (true) {

            // Ask user for their role
            UserRole role = ConsoleRoleSelector.promptUserForRole(scanner);

            // Create new session for this user
            UserSession session = new UserSession(role);
            System.out.println("Session started for role: " + role);

            // Create services (session-aware where required)
            InventoryService inventoryService
                    = new InventoryServiceImpl(repository, session, auditLogService);

            CheckoutService checkoutService
                    = new CheckoutServiceImpl(repository, auditLogService);

            InspectionService inspectionService
                    = new InspectionServiceImpl(repository, auditLogService);

            ReportService reportService
                    = new ReportServiceImpl(repository);

            AlertService alertService
                    = new AlertServiceImpl(repository);

            // Create and start menu
            Menu menu = new Menu(
                    inventoryService,
                    checkoutService,
                    inspectionService,
                    scanner
            );
            menu.start(session);
            // Menu returned → role logout
            System.out.println("\nUser logged out.\n");

            // Ask if another user wants to log in
            System.out.print("Would you like to switch user? (y/n): ");
            String choice = scanner.nextLine().trim();

            if (!choice.equalsIgnoreCase("y")) {
                break; // exit application loop
            }
        }
        scanner.close();
        DatabaseManager.shutdown();
        System.out.println("System shut down. Goodbye!");
    }

}
