/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cims.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author gggob
 */
public class DatabaseInitialiser {

    private DatabaseInitialiser() {
    }

    public static void initialise() {
        try (Connection conn = DatabaseManager.getConnection(); Statement stmt = conn.createStatement()) {
            createEquipmentTable(stmt);
            createUsageHistoryTable(stmt);

        } catch (SQLException e) {
            System.err.println("Database initialization failed: " + e.getMessage());
        }
    }

    private static void createEquipmentTable(Statement stmt)
            throws SQLException {
        try {

            stmt.executeUpdate("""
                CREATE TABLE EQUIPMENT (
                    id VARCHAR(36) PRIMARY KEY,
                    name VARCHAR(100) NOT NULL,
                    type VARCHAR(20) NOT NULL,
                    times_used INTEGER  DEFAULT 0 NOT NULL,
                    equipment_status VARCHAR(30) NOT NULL,
                    inspection_status VARCHAR(30) NOT NULL
                )
            """);
            System.out.println("Equipment table created.");
        } catch (SQLException e) {
            // Derby throws exception if table already exists
            if ("X0Y32".equals(e.getSQLState())) {
                System.out.println("Equipment table already exists.");
            } else {
                throw e;
            }
        }

    }

    private static void createUsageHistoryTable(Statement stmt)
            throws SQLException {

        try {

            stmt.executeUpdate("""
            CREATE TABLE USAGE_HISTORY (
                id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,            
                action_time TIMESTAMP NOT NULL,            
                equipment_id VARCHAR(36) NOT NULL,
                equipment_name VARCHAR(100) NOT NULL,
                user_name VARCHAR(50),    
                user_role VARCHAR(30) NOT NULL,            
                action_type VARCHAR(30) NOT NULL,            
                notes VARCHAR(255)
            )
        """);

            System.out.println("Usage history table created.");

        } catch (SQLException e) {

            if ("X0Y32".equals(e.getSQLState())) {
                System.out.println("Usage history table already exists.");
            } else {
                throw e;
            }
        }
    }

}
