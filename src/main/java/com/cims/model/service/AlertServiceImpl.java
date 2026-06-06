/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cims.model.service;
import com.cims.model.domain.*;
import com.cims.model.repository.EquipmentRepository;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
/**
 *
 * @author gggob
 */
public class AlertServiceImpl implements AlertService {
    
     private final EquipmentRepository repository;

    public AlertServiceImpl(EquipmentRepository repository) {
        this.repository = repository;
    }
    
    @Override
    public Collection<Alert> generateAlerts() {

        List<Alert> alerts = new ArrayList<>();

        for (Equipment equipment : repository.findAll()) {

            String message = equipment.getAlertMessage();

        if (message != null) {
            alerts.add(
                    new Alert(
                            equipment.getName(),
                            message));
        }
    }

    return alerts;
    }
    
}
