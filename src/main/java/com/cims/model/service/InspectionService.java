/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.cims.model.service;

import com.cims.model.domain.UserSession;
import java.util.UUID;

/**
 *
 * @author gggob
 */
public interface InspectionService {

    void flagForInspection(UUID equipmentId, UserSession session, String notes);
    void conductInspection(UUID equipmentId, UserSession session);
    void markSafe(UUID equipmentId, UserSession session,  String notes);
}
