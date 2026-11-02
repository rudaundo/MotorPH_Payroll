/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package payrollhub.service;

import java.awt.Window;
import javax.swing.JFrame;
import payrollhub.gui.GUILogin;

/**
 *
 * @author Jae
 */
public class LogoutHandler {
    
   public void logout(JFrame currentForm) {

        // Open the Login GUI
        GUILogin loginForm = new GUILogin();
        loginForm.setLocationRelativeTo(null);
        loginForm.setVisible(true);

        // Close all other open windows
        for (Window window : Window.getWindows()) {

            if (window != loginForm) {
                window.dispose();
            }
        }
    }
}
