/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cims.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author gggob
 */
public class DatabaseManager {

    private static String DATABASE_URL = "jdbc:derby:CIMS_DB;create=true";

    //methoid for JUnit testing
    public static void setUrl(String newUrl) {
        DATABASE_URL = newUrl;
    }

    private DatabaseManager() {/* prevent instantiation*/
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DATABASE_URL);
    }
}
