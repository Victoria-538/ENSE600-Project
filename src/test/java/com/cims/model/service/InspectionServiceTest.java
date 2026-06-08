/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cims.model.service;

import com.cims.database.*;
import com.cims.model.domain.*;
import com.cims.model.repository.*;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class InspectionServiceTest {

    private InspectionService service;
    private EquipmentRepository repository;
    private UserSession session;

    @Before
    public void setUp() {

        DatabaseManager.setUrl(
                "jdbc:derby:CIMS_TEST_DB;create=true"
        );

        DatabaseInitialiser.initialise();

        repository = new InMemoryEquipmentRepository();

        AuditLogRepository auditRepo
                = new DerbyAuditLogRepository();

        AuditLogService auditLogService
                = new AuditLogServiceImpl(auditRepo);

        service = new InspectionServiceImpl(
                repository,
                auditLogService
        );

        session = new UserSession(
                UserRole.ENGINEER,
                "Test Engineer"
        );
    }

    @Test
    public void testMarkSafeChangesStatusToAvailable() {

        Equipment equipment
                = new Hardware("Safety Harness");

        equipment.markFaulty();

        repository.save(equipment);

        service.markSafe(
                equipment.getId(),
                session,
                "Inspection completed successfully"
        );

        Equipment updated
                = repository.findById(
                        equipment.getId()
                );

        assertEquals(
                EquipmentStatus.AVAILABLE,
                updated.getEquipmentStatus()
        );
    }
    
    //testing permission logic 
    @Test(expected = SecurityException.class)
public void testPerformerCannotMarkSafe() {

    Equipment equipment =
            new Hardware("Safety Harness");

    equipment.markFaulty();

    repository.save(equipment);

    UserSession performer =
            new UserSession(
                    UserRole.PERFORMER,
                    "Performer"
            );

    service.markSafe(
            equipment.getId(),
            performer,
            ""
    );
}
}
