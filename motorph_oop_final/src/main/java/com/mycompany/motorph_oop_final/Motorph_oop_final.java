/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.motorph_oop_final;

import payrollhub.gui.GUILogin;

/**
 *
 * @author Jae
 */
public class Motorph_oop_final {

    public static void main(String[] args) {
        try {
        payrollhub.data.CsvHandler.initialize();

    } catch (java.io.IOException e) {

        javax.swing.JOptionPane.showMessageDialog(
                null,
                "Unable to initialize CSV files: "
                + e.getMessage()
        );
    }

    java.awt.EventQueue.invokeLater(() -> {
        new GUILogin().setVisible(true);
    });
    }
}
