/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package payrollhub.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.JOptionPane;

/**
 *
 * @author Jae
 */
public class AttendanceHandler {
    
    private final AttendanceService attendanceService;

    private static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss a");

    public AttendanceHandler(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    public LocalDateTime timeIn(String employeeID) {

        LocalDateTime timeIn = LocalDateTime.now();

        try {
            attendanceService.recordTimeIn(employeeID, timeIn);

            JOptionPane.showMessageDialog(
                    null,
                    "Time In recorded successfully!",
                    "Attendance",
                    JOptionPane.INFORMATION_MESSAGE
            );

            return timeIn;

        } catch (IOException e) {

            JOptionPane.showMessageDialog(
                    null,
                    e.getMessage(),
                    "Time In Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return null;
        }
    }
}
