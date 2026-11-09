/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package payrollhub.service;

import java.io.IOException;
import java.util.List;
import payrollhub.data.CsvHandler;
import payrollhub.data.EmployeeData;
import util.PasswordUtil;

/**
 *
 * @author Jae
 */
public class UserRegistrationService {
    
    // Retrieve employee information from employee.csv
    public EmployeeData findEmployee(String empNo) throws IOException {

        if (empNo == null || empNo.trim().isEmpty()) {
            throw new IllegalArgumentException("Employee ID is required.");
        }

        CsvHandler.initialize();

        List<String[]> employees = CsvHandler.readEmployees();

        for (String[] row : employees) {

            if (row.length > 0
                    && row[0].trim().equalsIgnoreCase(empNo.trim())) {

                return EmployeeData.fromCsvRow(row);
            }
        }

        return null;
    }

    // Register a new employee account
    public void registerUser(
            String empNo,
            String userName,
            String password
    ) throws IOException {

        if (empNo == null || empNo.trim().isEmpty()
                || userName == null || userName.trim().isEmpty()
                || password == null || password.isEmpty()) {

            throw new IllegalArgumentException(
                    "All registration fields are required."
            );
        }

        CsvHandler.initialize();

        // Confirm that the employee exists
        EmployeeData employee = findEmployee(empNo);

        if (employee == null) {
            throw new IllegalArgumentException(
                    "Employee ID does not exist."
            );
        }

        // Check existing registered accounts
        List<String[]> users = CsvHandler.readUsers();

        for (String[] user : users) {

            if (user.length >= 4) {

                // Prevent duplicate employee registration
                if (user[0].trim().equalsIgnoreCase(empNo.trim())) {
                    throw new IllegalArgumentException(
                            "This employee is already registered."
                    );
                }

                // Prevent duplicate usernames
                if (user[1].trim().equalsIgnoreCase(userName.trim())) {
                    throw new IllegalArgumentException(
                            "This username is already taken."
                    );
                }
            }
        }

        // Hash password before saving
        String hashedPassword = PasswordUtil.hashPassword(password);

        // Default role: Employee
        String[] newUser = {
            employee.getEmpNo(),
            userName.trim(),
            hashedPassword,
            "Employee"
        };

        CsvHandler.appendUser(newUser);
    }
    
}
