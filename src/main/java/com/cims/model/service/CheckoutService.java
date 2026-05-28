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
public interface CheckoutService {
     void checkOut(UUID equipmentId, UserSession session);
     void checkIn(UUID equipmentId, UserSession session);
}
