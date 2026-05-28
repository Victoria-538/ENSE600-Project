/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ENSE600.cims;

/**
 *
 * @author gggob
 */
public class UsageBasedInspectionPolicy implements InspectionPolicy {

    private final int maxUses;

    public UsageBasedInspectionPolicy(int maxUses) {
        this.maxUses = maxUses;
    }

    @Override
    public boolean requiresInspection(int timesUsed) {
        return timesUsed >= maxUses;
    }

}
