/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.cims;

import com.cims.ui.view.LoginFrame;
import com.cims.model.repository.*;
import com.cims.database.*;
import javax.swing.SwingUtilities;
/**
 *
 * @author gggob
 */
public class CIMS {

    public static void main(String[] args) {
 DatabaseInitialiser.initialise();

        EquipmentRepository repository =
                new DerbyEquipmentRepository();

        AuditLogRepository auditRepo =
                new DerbyAuditLogRepository();

        SwingUtilities.invokeLater(() -> {

            LoginFrame loginFrame =
                    new LoginFrame(
                            repository,
                            auditRepo);

            loginFrame.setVisible(true);
        });
           
        //]DatabaseManager.shutdown();      
    }
}
