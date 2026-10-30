/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package payrollhub.service;

/**
 *
 * @author Jae
 */
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import payrollhub.data.CsvHandler;
import payrollhub.data.EmployeeData;
import payrollhub.pojo.Payroll;
import util.PayrollCalculator;

public class PayrollService {

    private final PayrollCalculator calculator;
    
    private final AttendanceService attendanceService;
    
    private final OvertimeService overtimeService;
    

    public PayrollService() {
        this.calculator = new PayrollCalculator();

        this.attendanceService = new AttendanceService(
                CsvHandler.ATTENDANCE_FILE.toString()
        );
        
        this.overtimeService = new OvertimeService();
    }

    // Retrieve an employee from employee.csv
    private EmployeeData getEmployee(String empNo)
            throws IOException {

        List<String[]> employees = CsvHandler.readEmployees();

        for (String[] row : employees) {

            if (row.length > 0 && row[0].trim().equals(empNo.trim())) {

                return EmployeeData.fromCsvRow(row);
            }
        }

        throw new IllegalArgumentException(
                "Employee not found: " + empNo
        );
    }

    // Process and save payroll
    public Payroll processPayroll(
            String empNo,
            LocalDate periodStart,
            LocalDate periodEnd,
            BigDecimal overtimePay,
            BigDecimal sss,
            BigDecimal philhealth,
            BigDecimal pagibig,
            BigDecimal withholdingTax,
            BigDecimal otherDeductions
    ) throws IOException {
       
        // 1. Retrieve employee information
        EmployeeData employee = getEmployee(empNo);

        // 2. Check for duplicate payroll
        List<String[]> existingPayroll = CsvHandler.readPayroll();

        for (String[] row : existingPayroll) {

            if (row.length >= 5
                    && row[0].equals(empNo)
                    && row[3].equals(periodStart.toString())
                    && row[4].equals(periodEnd.toString())) {

                throw new IllegalStateException(
                        "Payroll already exists for employee "
                        + empNo + " during this period."
                );
            }
        }
        
                int daysWorked = attendanceService.countDaysWorked(
                        empNo,
                        periodStart,
                        periodEnd
        );
                
                BigDecimal approvedOvertimeHours = overtimeService.getApprovedOvertimeHours(
                        empNo,
                        periodStart,
                        periodEnd
        );

                BigDecimal monthlySalary = BigDecimal.valueOf(employee.getBasicSalary());

                BigDecimal calculatedOvertimePay = calculator.calculateOvertimePay(
                        monthlySalary,
                        approvedOvertimeHours
        );
                
                
               

        // 3. Calculate payroll
        Payroll payroll = calculator.calculate(
                employee,
                periodStart,
                periodEnd,
                daysWorked,
                calculatedOvertimePay,
                sss,
                philhealth,
                pagibig,
                withholdingTax,
                otherDeductions
        );

        // 4. Save payroll to payroll.csv
        CsvHandler.appendPayroll(convertToCsvRow(payroll));

        // 5. Return completed payroll
        return payroll;
    }

    // Convert Payroll object into CSV columns
    private String[] convertToCsvRow(Payroll payroll) {

        return new String[] {
            payroll.getEmpNo(),
            payroll.getEmployeeName(),
            payroll.getPosition(),
            payroll.getPeriodStart().toString(),
            payroll.getPeriodEnd().toString(),
            payroll.getMonthlySalary().toPlainString(),
            payroll.getDailyRate().toPlainString(),
            String.valueOf(payroll.getDaysWorked()),
            payroll.getOvertimePay().toPlainString(),
            payroll.getGrossIncome().toPlainString(),
            payroll.getRiceSubsidy().toPlainString(),
            payroll.getPhoneAllowance().toPlainString(),
            payroll.getClothingAllowance().toPlainString(),
            payroll.getTotalBenefits().toPlainString(),
            payroll.getSss().toPlainString(),
            payroll.getPhilhealth().toPlainString(),
            payroll.getPagibig().toPlainString(),
            payroll.getWithholdingTax().toPlainString(),
            payroll.getOtherDeductions().toPlainString(),
            payroll.getTotalDeductions().toPlainString(),
            payroll.getTakeHomePay().toPlainString()
        };
    }
    
    public List<Payroll> processBatchPayroll(
            LocalDate periodStart,
            LocalDate periodEnd
    ) throws IOException {

        List<Payroll> generatedPayrolls = new ArrayList<>();

        List<String[]> employees = CsvHandler.readEmployees();

        for (String[] row : employees) {

            if (row.length < EmployeeData.FIELD_COUNT) {
                continue;
            }

            String empNo = row[0].trim();

            try {
                Payroll payroll = processPayroll(
                        empNo,
                        periodStart,
                        periodEnd,
                        BigDecimal.ZERO, // OT is calculated automatically
                        BigDecimal.ZERO, // SSS
                        BigDecimal.ZERO, // PhilHealth
                        BigDecimal.ZERO, // Pag-IBIG
                        BigDecimal.ZERO, // Withholding tax
                        BigDecimal.ZERO  // Other deductions
                );

                generatedPayrolls.add(payroll);

            } catch (IllegalStateException e) {

                // Skip employees whose payroll already exists.
                if (e.getMessage() != null
                        && e.getMessage().contains("already exists")) {

                    System.out.println(
                            "Skipped duplicate payroll for: " + empNo
                    );

                } else {
                    throw e;
                }
            }
        }

        return generatedPayrolls;
    }
}
