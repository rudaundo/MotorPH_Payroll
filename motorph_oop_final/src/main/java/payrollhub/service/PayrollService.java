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
import payrollhub.pojo.TaxReport;
import payrollhub.pojo.DeductionReport;

public class PayrollService {

    private final PayrollCalculator calculator;
    
    private final AttendanceService attendanceService;
    
    private final OvertimeService overtimeService;
    
    private final DeductionCalculator deductionCalculator;
    

    public PayrollService() {
        this.calculator = new PayrollCalculator();

        this.attendanceService = new AttendanceService(
                CsvHandler.ATTENDANCE_FILE.toString()
        );
        
        this.overtimeService = new OvertimeService();
        
        this.deductionCalculator = new DeductionCalculator();
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
                
                    BigDecimal monthlySSS = deductionCalculator.calculateSSS(monthlySalary);

                    BigDecimal monthlyPhilHealth = deductionCalculator.calculatePhilHealth(monthlySalary);

                    BigDecimal monthlyPagIBIG = deductionCalculator.calculatePagIBIG(monthlySalary);

                    // Allocate monthly contributions to one payroll cutoff
                    BigDecimal calculatedSSS = deductionCalculator.calculatePerCutoff(monthlySSS);

                    BigDecimal calculatedPhilHealth = deductionCalculator.calculatePerCutoff(monthlyPhilHealth);

                    BigDecimal calculatedPagIBIG = deductionCalculator.calculatePerCutoff(monthlyPagIBIG);

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
                calculatedOvertimePay,
                calculatedSSS,
                calculatedPhilHealth,
                calculatedPagIBIG,
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
    
    // Retrieve a payslip for one employee using a selected date
        public Payroll getPayslip(String empNo, LocalDate selectedDate)
                throws IOException {

            List<String[]> payrollRecords = CsvHandler.readPayroll();

            for (String[] row : payrollRecords) {

                // Make sure the CSV row contains all 21 payroll columns
                if (row.length < 21) {
                    continue;
                }

                String recordEmpNo = row[0].trim();

                // Only retrieve the logged-in employee's payroll
                if (!recordEmpNo.equals(empNo.trim())) {
                    continue;
                }

                try {

                    LocalDate periodStart = LocalDate.parse(row[3].trim());
                    LocalDate periodEnd = LocalDate.parse(row[4].trim());

                    // Check whether the selected date is within the payroll period
                    boolean matchesDate =
                            !selectedDate.isBefore(periodStart)
                            && !selectedDate.isAfter(periodEnd);

                    if (!matchesDate) {
                        continue;
                    }

                    // Reconstruct the Payroll object using the existing CSV columns
                    return new Payroll(
                            row[0].trim(),                 // Employee number
                            row[1].trim(),                 // Employee name
                            row[2].trim(),                 // Position
                            periodStart,                   // Period start
                            periodEnd,                     // Period end
                            parseAmount(row[5]),            // Monthly salary
                            parseAmount(row[6]),            // Daily rate
                            Integer.parseInt(row[7].trim()),// Days worked
                            parseAmount(row[8]),            // Overtime pay
                            parseAmount(row[9]),            // Gross income
                            parseAmount(row[10]),           // Rice subsidy
                            parseAmount(row[11]),           // Phone allowance
                            parseAmount(row[12]),           // Clothing allowance
                            parseAmount(row[13]),           // Total benefits
                            parseAmount(row[14]),           // SSS
                            parseAmount(row[15]),           // PhilHealth
                            parseAmount(row[16]),           // Pag-IBIG
                            parseAmount(row[17]),           // Withholding tax
                            parseAmount(row[18]),           // Other deductions
                            parseAmount(row[19]),           // Total deductions
                            parseAmount(row[20])            // Take-home pay
                    );

                } catch (java.time.format.DateTimeParseException
                        | NumberFormatException e) {

                    System.err.println(
                            "Skipping invalid payroll record for employee "
                            + recordEmpNo + ": " + e.getMessage()
                    );
                }
            }

            // No matching payroll record was found
            return null;
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
    
    // Retrieve tax reports for all employees within a date range
    public List<TaxReport> getTaxReports(
            LocalDate startDate,
            LocalDate endDate
    ) throws IOException {

        return getTaxReports(startDate, endDate, null);
    }


    // Retrieve tax reports for a specific employee or all employees
        public List<TaxReport> getTaxReports(
                LocalDate startDate,
                LocalDate endDate,
                String empNo
        ) throws IOException {

            List<String[]> payrollRecords = CsvHandler.readPayroll();
            List<TaxReport> taxReports = new ArrayList<>();

            for (String[] row : payrollRecords) {

                // Ensure all required CSV columns are present
                if (row.length < 21) {
                    continue;
                }

                String recordEmpNo = row[0].trim();

                // Filter by employee when an employee ID is provided
                if (empNo != null
                        && !empNo.isBlank()
                        && !recordEmpNo.equals(empNo.trim())) {

                    continue;
                }

                try {

                    LocalDate periodStart = LocalDate.parse(row[3].trim());
                    LocalDate periodEnd = LocalDate.parse(row[4].trim());

                    // Include payroll periods overlapping the selected dates
                    boolean matchesDate =
                            !periodEnd.isBefore(startDate)
                            && !periodStart.isAfter(endDate);

                    if (!matchesDate) {
                        continue;
                    }

                    BigDecimal grossIncome =
                            parseAmount(row[9]);

                    BigDecimal withholdingTax =
                            parseAmount(row[17]);

                    BigDecimal totalDeductions =
                            parseAmount(row[19]);

                    TaxReport report = new TaxReport(
                            recordEmpNo,
                            row[1].trim(),
                            periodStart,
                            periodEnd,
                            grossIncome,
                            withholdingTax,
                            totalDeductions
                    );

                    taxReports.add(report);

                } catch (java.time.format.DateTimeParseException
                        | NumberFormatException e) {

                    System.err.println(
                            "Skipping invalid payroll record for employee "
                            + recordEmpNo + ": " + e.getMessage()
                    );
                }
            }

            return taxReports;
        }


        // Convert a CSV amount into BigDecimal
        private BigDecimal parseAmount(String value) {

            if (value == null || value.isBlank()) {
                return BigDecimal.ZERO;
            }

            return new BigDecimal(value.trim());
        }
        
        // Retrieve deduction reports for all employees
        public List<DeductionReport> getDeductionReports(
                LocalDate startDate,
                LocalDate endDate
        ) throws IOException {

            return getDeductionReports(startDate, endDate, null);
        }


        // Retrieve deduction reports for a specific employee or all employees
        public List<DeductionReport> getDeductionReports(
                LocalDate startDate,
                LocalDate endDate,
                String empNo
        ) throws IOException {

            List<String[]> payrollRecords = CsvHandler.readPayroll();

            List<DeductionReport> deductionReports = new ArrayList<>();

            for (String[] row : payrollRecords) {

                if (row.length < 21) {
                    continue;
                }

                String recordEmpNo = row[0].trim();

                // Filter selected employee
                if (empNo != null
                        && !empNo.isBlank()
                        && !recordEmpNo.equals(empNo.trim())) {

                    continue;
                }

                try {

                    LocalDate periodStart = LocalDate.parse(row[3].trim());
                    LocalDate periodEnd = LocalDate.parse(row[4].trim());

                    // Include payroll periods overlapping selected dates
                    boolean matchesDate =
                            !periodEnd.isBefore(startDate)
                            && !periodStart.isAfter(endDate);

                    if (!matchesDate) {
                        continue;
                    }

                    BigDecimal sss = parseAmount(row[14]);
                    BigDecimal philhealth = parseAmount(row[15]);
                    BigDecimal pagibig = parseAmount(row[16]);
                    BigDecimal withholdingTax = parseAmount(row[17]);
                    BigDecimal otherDeductions = parseAmount(row[18]);
                    BigDecimal totalDeductions = parseAmount(row[19]);

                    DeductionReport report = new DeductionReport(
                            recordEmpNo,
                            row[1].trim(),
                            periodStart,
                            periodEnd,
                            sss,
                            philhealth,
                            pagibig,
                            withholdingTax,
                            otherDeductions,
                            totalDeductions
                    );

                    deductionReports.add(report);

                } catch (java.time.format.DateTimeParseException
                        | NumberFormatException e) {

                    System.err.println(
                            "Skipping invalid deduction record for employee "
                            + recordEmpNo + ": " + e.getMessage()
                    );
                }
            }

            return deductionReports;
        }
    }
