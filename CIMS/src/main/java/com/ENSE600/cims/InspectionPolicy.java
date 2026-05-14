/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.ENSE600.cims;

/**
 *
 * @author gggob
 */
public interface InspectionPolicy {
    //Determinew whether equioment is due for inspection, returnes true if inspection is required.
    boolean requiresInspection(int timesUsed);
}
