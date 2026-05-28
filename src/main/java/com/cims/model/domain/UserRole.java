/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package com.cims.model.domain;

import com.cims.model.domain.Equipment;
import com.cims.model.domain.Hardware;
import com.cims.model.domain.Apparatus;
import com.cims.model.domain.Prop;

/**
 *
 * @author gggob
 */
public enum UserRole {

    RIGGER {
        @Override
        public boolean canCheckout(Equipment equipment) {
            return equipment instanceof Hardware
                    || equipment instanceof Apparatus;
        }

        @Override
        public boolean canConductInspection(Equipment equipment) {
            return false;
        }

        @Override
        public boolean canAddEquipment() {
            return false;
        }

        @Override
        public boolean canRemoveEquipment(Equipment equipment) {
            return false;
        }

    },
    PERFORMER {
        @Override
        public boolean canCheckout(Equipment equipment) {
            return equipment instanceof Apparatus
                    || equipment instanceof Prop;
        }

        @Override
        public boolean canConductInspection(Equipment equipment) {
            return false;
        }

        @Override
        public boolean canAddEquipment() {
            return false;
        }

        @Override
        public boolean canRemoveEquipment(Equipment equipment) {
            return false;
        }

    },
    ENGINEER {
        @Override
        public boolean canCheckout(Equipment equipment) {
            return false;
        }

        @Override
        public boolean canConductInspection(Equipment equipment) {
            return equipment instanceof Hardware
                    || equipment instanceof Apparatus;
        }

        @Override
        public boolean canAddEquipment() {
            return false;
        }

        @Override
        public boolean canRemoveEquipment(Equipment equipment) {
            return true;
        }

    },
    INVENTORY_MANAGER {
        @Override
        public boolean canCheckout(Equipment equipment) {
            return false;
        }

        @Override
        public boolean canConductInspection(Equipment equipment) {
            return equipment instanceof Prop;
        }

        @Override
        public boolean canAddEquipment() {
            return true;
        }

        @Override
        public boolean canRemoveEquipment(Equipment equipment) {
            return true;
        }

    };

    /**
     * Determines whether this role is allowed to check out the given equipment.
     */
    public abstract boolean canCheckout(Equipment equipment);

    public abstract boolean canConductInspection(Equipment equipment);

    public abstract boolean canAddEquipment();

    public abstract boolean canRemoveEquipment(Equipment equipment);

}
