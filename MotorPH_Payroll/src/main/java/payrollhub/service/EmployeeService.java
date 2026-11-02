/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package payrollhub.service;

import java.io.IOException;
import java.util.List;
import payrollhub.data.CsvHandler;
import payrollhub.data.EmployeeData;

/**
 *
 * @author Jae
 */
public class EmployeeService {

    /**
     * Adds a new employee to employee.csv.
     *
     * @return true if the employee was successfully added.
     */
    public boolean addEmployee(EmployeeData employee) throws IOException {

        if (employee == null) {
            throw new IllegalArgumentException("Employee record cannot be null.");
        }

        // Read existing employees to check for duplicate employee IDs.
        List<String[]> employees = CsvHandler.readEmployees();

        for (String[] row : employees) {

            if (row.length > 0
                    && row[0].trim().equalsIgnoreCase(employee.getEmpNo())) {

                throw new IllegalArgumentException(
                        "Employee ID already exists: " + employee.getEmpNo()
                );
            }
        }

        // Convert the EmployeeData object into the 17-column CSV format.
        CsvHandler.appendEmployee(employee.toCsvRow());

        return true;
    }

    /**
     * Checks whether an employee ID already exists.
     */
    public boolean employeeExists(String empNo) throws IOException {

        if (empNo == null || empNo.trim().isEmpty()) {
            return false;
        }

        List<String[]> employees = CsvHandler.readEmployees();

        for (String[] row : employees) {

            if (row.length > 0
                    && row[0].trim().equalsIgnoreCase(empNo.trim())) {
                return true;
            }
        }

        return false;
    }
    
    public String generateEmployeeID() throws IOException {

        List<String[]> employees = CsvHandler.readEmployees();

        int highestID = 10000;

        for (String[] row : employees) {

            if (row.length > 0) {

                try {
                    int currentID = Integer.parseInt(row[0].trim());

                    if (currentID > highestID) {
                        highestID = currentID;
                    }

                } catch (NumberFormatException e) {
                    // Skip records with non-numeric employee IDs.
                }
            }
        }

        return String.valueOf(highestID + 1);
    }
    
    public boolean updateEmployee(EmployeeData updatedEmployee) throws IOException {
        if (updatedEmployee == null) {
            throw new IllegalArgumentException("Employee record cannot be null.");
        }

        List<String[]> employees = CsvHandler.readEmployees();

        boolean found = false;

        for (int i = 0; i < employees.size(); i++) {
            String[] row = employees.get(i);

            if (row.length > 0
                    && row[0].trim().equalsIgnoreCase(updatedEmployee.getEmpNo())) {

                employees.set(i, updatedEmployee.toCsvRow());
                found = true;
                break;
            }
        }

        if (!found) {
            throw new IllegalArgumentException(
                    "Employee ID not found: " + updatedEmployee.getEmpNo()
            );
        }

        CsvHandler.writeEmployees(employees);
        return true;
    }
    
    public EmployeeData getEmployee(String empNo) throws IOException {
        if (empNo == null || empNo.trim().isEmpty()) {
            throw new IllegalArgumentException("Please enter an Employee ID.");
        }

        List<String[]> employees = CsvHandler.readEmployees();

        for (String[] row : employees) {
            if (row.length > 0
                    && row[0].trim().equalsIgnoreCase(empNo.trim())) {

                return EmployeeData.fromCsvRow(row);
            }
        }

        return null;
    }
    
}