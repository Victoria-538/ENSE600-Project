/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cims.model.service;

import com.cims.database.*;
import com.cims.model.domain.*;
import com.cims.model.repository.*;

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

public class CheckoutServiceTest {

    private CheckoutService service;
    private EquipmentRepository repository;
    private UserSession session;

    @Before
    public void setUp() {

        DatabaseManager.setUrl(
                "jdbc:derby:CIMS_TEST_DB;create=true"
        );

        DatabaseInitialiser.initialise();

        repository = new DerbyEquipmentRepository();

        AuditLogRepository auditRepo
                = new DerbyAuditLogRepository();

        AuditLogService auditLogService
                = new AuditLogServiceImpl(auditRepo);

        service = new CheckoutServiceImpl(
                repository,
                auditLogService
        );

        session = new UserSession(
                UserRole.RIGGER,
                "Test User"
        );
    }

    @Test
    public void testCheckoutChangesStatusToCheckedOut() {

        Equipment equipment
                = new Hardware("Safety Harness");

        repository.save(equipment);

        service.checkOut(
                equipment.getId(),
                session,
                "Testing checkout"
        );

        Equipment updated
                = repository.findById(equipment.getId());

        assertEquals(
                EquipmentStatus.CHECKED_OUT,
                updated.getEquipmentStatus()
        );
    }

    @Test
    public void testCheckinChangesStatusToAvailable() {

        Equipment equipment
                = new Hardware("Safety Harness");

        repository.save(equipment);

        service.checkOut(
                equipment.getId(),
                session,
                ""
        );

        service.checkIn(
                equipment.getId(),
                session,
                ""
        );

        Equipment updated
                = repository.findById(equipment.getId());

        assertEquals(
                EquipmentStatus.AVAILABLE,
                updated.getEquipmentStatus()
        );
    }

    //test user role permission
    @Test(expected = SecurityException.class)
    public void testEngineerCannotCheckoutEquipment() {

        Equipment equipment = new Hardware("Harness");
        repository.save(equipment);

        UserSession engineer
                = new UserSession(
                        UserRole.ENGINEER,
                        "Engineer"
                );

        service.checkOut(
                equipment.getId(),
                engineer,
                ""
        );
    }
}
