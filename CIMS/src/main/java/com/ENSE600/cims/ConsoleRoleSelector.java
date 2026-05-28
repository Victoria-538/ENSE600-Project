/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ENSE600.cims;

import java.util.Scanner;

/**
 *
 * @author gggob
 */
public class ConsoleRoleSelector {
    
  // Prevent instantiation
    private ConsoleRoleSelector() {
    }

    
   public static UserRole promptUserForRole() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println();
            System.out.println("Select your role:");
            System.out.println("1. Rigger");
            System.out.println("2. Performer");
            System.out.println("3. Engineer");
            System.out.println("4. Inventory Manager");
            System.out.print("Enter choice (1-4): ");

            String input = scanner.nextLine().trim();

            switch (input) {
                case "1":
                    return UserRole.RIGGER;
                case "2":
                    return UserRole.PERFORMER;
                case "3":
                    return UserRole.ENGINEER;
                case "4":
                    return UserRole.INVENTORY_MANAGER;
                default:
                    System.out.println("Invalid selection. Please try again.");
            }
        }
    }

}
