/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package com.cims.model.repository;

import com.cims.database.DatabaseInitialiser;
import com.cims.database.DatabaseManager;
import com.cims.model.domain.Apparatus;
import com.cims.model.domain.Equipment;
import com.cims.model.domain.Hardware;
import java.util.UUID;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author gggob
 */
public class DerbyEquipmentRepositoryTest {

    private DerbyEquipmentRepository repository;

    @Before //setup a test database to avoid changing data in the main one
    public void setUp() {
        DatabaseManager.setUrl(
                "jdbc:derby:CIMS_TEST_DB;create=true"
        );
        DatabaseInitialiser.initialise();
        repository = new DerbyEquipmentRepository();
    }

    /**
     * Test of save and find by ID method, of class DerbyEquipmentRepository.
     */
    @org.junit.Test
    public void testSaveAndFindById() {
        Hardware hardware = new Hardware("Safety Harness");
        System.out.println("save");
        repository.save(hardware);
        Equipment found = repository.findById(hardware.getId());
        assertNotNull(found);
        assertEquals("Safety Harness", found.getName());

    }

    /**
     * Test of findAll method, of class DerbyEquipmentRepository.
     */
    @org.junit.Test
    public void testFindAllReturnsEquipment() {
        int before = repository.findAll().size();
        repository.save(new Hardware("Helmet"));
        int after = repository.findAll().size();
        assertEquals(before + 1, after);
    }

    /**
     * Test of remove method, of class DerbyEquipmentRepository.
     */
    @org.junit.Test
    public void testRemove() {
        Apparatus app = new Apparatus("Rope");
        repository.save(app);
        UUID id = app.getId();
        repository.remove(id);
        Equipment found = repository.findById(id);
        assertNull(found);
    }

    @Test
    public void testFindByType() {
        repository.save(new Hardware("Harness"));
        assertFalse(repository.findByType(Hardware.class).isEmpty());
    }

    @Test
    public void testUpdateEquipment() {

        Hardware hardware = new Hardware("Old Name");
        repository.save(hardware);
        hardware.setName("New Name");
        repository.save(hardware);
        Equipment updated = repository.findById(hardware.getId());
        assertEquals("New Name", updated.getName());
    }

}
