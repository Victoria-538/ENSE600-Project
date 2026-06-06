/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cims.model.repository;

import com.cims.model.domain.Equipment;
import java.util.Collection;
import java.util.UUID;
import com.cims.database.DatabaseManager;
import com.cims.model.domain.*;
import java.sql.*;
import java.util.*;

/**
 *
 * @author gggob
 */
public class DerbyEquipmentRepository implements EquipmentRepository {

    @Override
    public void save(Equipment equipment) {
        if (findById(equipment.getId()) != null) {
            update(equipment);
        } else {
            insert(equipment);
        }
    }

    @Override
    public Equipment findById(UUID id) {
        String sql
                = "SELECT * FROM EQUIPMENT WHERE id = ?";
        try (
                Connection conn = DatabaseManager.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id.toString());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }

            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Collection<Equipment> findByType(
            Class<? extends Equipment> type) {

        List<Equipment> results = new ArrayList<>();

        String sql = "SELECT * FROM EQUIPMENT WHERE type = ?";

        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, type.getSimpleName());

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    results.add(mapRow(rs));
                }
            }

            return results;

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Error retrieving equipment by type", e);
        }
    }

    @Override
    public Collection<Equipment> findAll() {
        List<Equipment> equipment = new ArrayList<>();

        String sql = "SELECT * FROM EQUIPMENT";

        try (
                Connection conn = DatabaseManager.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                equipment.add(mapRow(rs));
            }
            return equipment;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Collection<Equipment> filter(String type, EquipmentStatus equipmentStatus, InspectionStatus inspectionStatus) {

        List<Equipment> results = new ArrayList<>();

        StringBuilder sql = new StringBuilder("SELECT * FROM EQUIPMENT WHERE 1=1");
        List<Object> params = new ArrayList<>();

        // ✅ Filter by type (ignore null or blank)
        if (type != null && !type.isBlank()) {
            sql.append(" AND type = ?");
            params.add(type);
        }

        // ✅ Filter by equipment status (enum)
        if (equipmentStatus != null) {
            sql.append(" AND equipment_status = ?");
            params.add(equipmentStatus.name());
        }

        // ✅ Filter by inspection status (enum)
        if (inspectionStatus != null) {
            sql.append(" AND inspection_status = ?");
            params.add(inspectionStatus.name());
        }

        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            // ✅ Set parameters dynamically
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    results.add(mapRow(rs));
                }
            }

            return results;

        } catch (SQLException e) {
            throw new RuntimeException("Error executing filter query", e);
        }
    }

    @Override
    public void remove(UUID id) {
        String sql
                = "DELETE FROM EQUIPMENT WHERE id = ?";
        try (
                Connection conn = DatabaseManager.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id.toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void insert(Equipment equipment) {
        String sql = """
        INSERT INTO EQUIPMENT
        (id, name, type, times_used,
         equipment_status, inspection_status)
        VALUES (?, ?, ?, ?, ?, ?)
        """;
        try (
                Connection conn = DatabaseManager.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, equipment.getId().toString());
            ps.setString(2, equipment.getName());
            ps.setString(3, equipment.getEquipmentType());
            ps.setInt(4, equipment.getTimesUsed());
            ps.setString(5, equipment.getEquipmentStatus().name());
            ps.setString(6, equipment.getInspectionStatus().name());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void update(Equipment equipment) {

        String sql = """
        UPDATE EQUIPMENT
        SET name = ?,
            type = ?,
            times_used = ?,
            equipment_status = ?,
            inspection_status = ?
        WHERE id = ?
        """;
        try (
                Connection conn = DatabaseManager.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, equipment.getName());
            ps.setString(2, equipment.getEquipmentType());
            ps.setInt(3, equipment.getTimesUsed());
            ps.setString(4, equipment.getEquipmentStatus().name());
            ps.setString(5, equipment.getInspectionStatus().name());
            ps.setString(6, equipment.getId().toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Equipment mapRow(ResultSet rs)
            throws SQLException {

        UUID id = UUID.fromString(rs.getString("id"));
        String name = rs.getString("name");
        String type = rs.getString("type");
        int timesUsed = rs.getInt("times_used");
        EquipmentStatus equipmentStatus
                = EquipmentStatus.valueOf(
                        rs.getString("equipment_status"));
        InspectionStatus inspectionStatus
                = InspectionStatus.valueOf(
                        rs.getString("inspection_status"));

        return switch (type) {
            case "Hardware" ->
                new Hardware(
                id,
                name,
                timesUsed,
                equipmentStatus,
                inspectionStatus);

            case "Apparatus" ->
                new Apparatus(
                id,
                name,
                timesUsed,
                equipmentStatus,
                inspectionStatus);

            case "Prop" ->
                new Prop(
                id,
                name,
                timesUsed,
                equipmentStatus,
                inspectionStatus);

            default ->
                throw new IllegalStateException(
                        "Unknown equipment type: " + type);
        };
    }

    //added methods - this has replaced findByName and findByType methods - now user can search by anythign else not just a name
    @Override
    public Collection<Equipment> search(String keyword) {
        List<Equipment> results = new ArrayList<>();

        String sql = """
        SELECT *
        FROM EQUIPMENT
        WHERE LOWER(name) LIKE ?
           OR LOWER(id) LIKE ?
           OR LOWER(type) LIKE ?
        """;

        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            String search = "%" + keyword.toLowerCase() + "%";
            if (keyword == null || keyword.isBlank()) {
                return findAll();
            }
            ps.setString(1, search);
            ps.setString(2, search);
            ps.setString(3, search);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    results.add(mapRow(rs));
                }
            }

            return results;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
