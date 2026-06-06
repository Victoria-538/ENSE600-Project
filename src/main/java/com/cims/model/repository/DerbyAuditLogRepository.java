/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cims.model.repository;

import com.cims.database.DatabaseManager;
import com.cims.model.domain.*;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

/**
 *
 * @author gggob
 */
public class DerbyAuditLogRepository implements AuditLogRepository {

    @Override
    public void save(AuditLog log) {

        String sql = """
            INSERT INTO USAGE_HISTORY
            (
                action_time,
                equipment_id,
                equipment_name,
                user_name,
                user_role,
                action_type,
                notes
            )
            VALUES (?, ?, ?, ?, ?, ?, ?)
            """;

        try (
                Connection conn = DatabaseManager.getConnection(); PreparedStatement ps
                = conn.prepareStatement(sql)) {

            ps.setTimestamp(
                    1,
                    Timestamp.valueOf(log.actionTime));

            ps.setString(
                    2,
                    log.equipmentId.toString());

            ps.setString(
                    3,
                    log.equipmentName);

            ps.setString(
                    4,
                    log.userName);

            ps.setString(
                    5,
                    log.userRole);

            ps.setString(
                    6,
                    log.actionType.name());

            ps.setString(
                    7,
                    log.notes!= null ? log.notes : "");

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Collection<AuditLog> findAll() {
        List<AuditLog> logs = new ArrayList<>();

        String sql
                = "SELECT * FROM USAGE_HISTORY ORDER BY action_time DESC";

        try (
                Connection conn = DatabaseManager.getConnection(); PreparedStatement ps
                = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                logs.add(mapRow(rs));
            }

            return logs;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Collection<AuditLog> findByEquipment(UUID equipmentId) {
        List<AuditLog> logs = new ArrayList<>();

        String sql = """
            SELECT *
            FROM USAGE_HISTORY
            WHERE equipment_id = ?
            ORDER BY action_time DESC
            """;

        try (
                Connection conn = DatabaseManager.getConnection(); PreparedStatement ps
                = conn.prepareStatement(sql)) {

            ps.setString(1, equipmentId.toString());

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    logs.add(mapRow(rs));
                }
            }

            return logs;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private AuditLog mapRow(ResultSet rs)
            throws SQLException {

        return new AuditLog(
                rs.getLong("id"),
                rs.getTimestamp("action_time")
                        .toLocalDateTime(),
                UUID.fromString(
                        rs.getString("equipment_id")),
                rs.getString("equipment_name"),
                rs.getString("user_name"),
                rs.getString("user_role"),
                ActionType.valueOf(
                        rs.getString("action_type")),
                rs.getString("notes")
        );
    }
}
