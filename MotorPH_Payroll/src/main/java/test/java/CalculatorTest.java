/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package test.java;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;

import payrollhub.data.CsvHandler;
import payrollhub.data.EmployeeData;
import payrollhub.pojo.Payroll;
import payrollhub.service.PayrollService;
import util.PayrollCalculator;

public class CalculatorTest {

    public static void main(String[] args) throws IOException {
        
        var employees = CsvHandler.readEmployees();

String[] employeeRow = employees.stream()
        .filter(row -> row[0].equals("10015"))
        .findFirst()
        .orElseThrow();

EmployeeData employee = EmployeeData.fromCsvRow(employeeRow);

        PayrollService payrollService = new PayrollService();

Payroll payroll = payrollService.processPayroll(
        "10015",
        LocalDate.of(2024, 6, 1),
        LocalDate.of(2024, 6, 15),
        BigDecimal.ZERO,
        BigDecimal.ZERO,
        BigDecimal.ZERO,
        BigDecimal.ZERO,
        BigDecimal.ZERO,
        BigDecimal.ZERO
);

System.out.println("Employee: " + payroll.getEmployeeName());
System.out.println("Gross Income: " + payroll.getGrossIncome());
System.out.println("Total Benefits: " + payroll.getTotalBenefits());
System.out.println("Total Deductions: " + payroll.getTotalDeductions());
System.out.println("Take Home Pay: " + payroll.getTakeHomePay());
    }
}