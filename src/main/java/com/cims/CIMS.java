/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.cims;
import com.cims.view.Menu;
import com.cims.view.ConsoleRoleSelector;
import com.cims.model.service.CheckoutService;
import com.cims.model.service.InventoryService;
import com.cims.model.service.CheckoutServiceImpl;
import com.cims.model.service.InventoryServiceImpl;
import com.cims.model.service.InspectionServiceImpl;
import com.cims.model.service.InspectionService;
import com.cims.model.repository.EquipmentRepository;
import com.cims.model.repository.InMemoryEquipmentRepository;
import com.cims.model.domain.UserRole;
import com.cims.model.domain.UserSession;
import com.cims.model.repository.DerbyEquipmentRepository;
import com.cims.database.DatabaseInitialiser;
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
                  
         // 2. Main application loop 
        while (true) {

            // Ask user for their role
            UserRole role = ConsoleRoleSelector.promptUserForRole(scanner);

            // Create new session for this user
            UserSession session = new UserSession(role);
            System.out.println("Session started for role: " + role);

            // Create services (session-aware where required)
            InventoryService inventoryService
                    = new InventoryServiceImpl(repository, session);

            CheckoutService checkoutService
                    = new CheckoutServiceImpl(repository);

            InspectionService inspectionService
                    = new InspectionServiceImpl(repository);

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
        System.out.println("System shut down. Goodbye!");
    }

}
