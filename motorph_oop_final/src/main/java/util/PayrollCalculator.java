/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

/**
 *
 * @author Jae
 */
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

import payrollhub.data.EmployeeData;
import payrollhub.pojo.Payroll;

public class PayrollCalculator {

    private static final BigDecimal WORKING_DAYS_PER_MONTH =
            BigDecimal.valueOf(20);

    private static BigDecimal money(BigDecimal value) {
        if (value == null || value.signum() < 0) {
            throw new IllegalArgumentException(
                    "Payroll amounts must be non-negative.");
        }

        return value.setScale(2, RoundingMode.HALF_UP);
    }

    public Payroll calculate(
            EmployeeData employee, LocalDate periodStart, LocalDate periodEnd,
            int daysWorked, BigDecimal overtimePay, BigDecimal sss, BigDecimal philhealth,
            BigDecimal pagibig, BigDecimal withholdingTax, BigDecimal otherDeductions) {

        if (employee == null) {
            throw new IllegalArgumentException("Employee is required.");
        }

        if (periodStart == null || periodEnd == null
                || periodEnd.isBefore(periodStart)) {
            throw new IllegalArgumentException("Invalid payroll period.");
        }

        if (daysWorked < 0 || daysWorked > 20) {
            throw new IllegalArgumentException(
                    "Days worked must be between 0 and 20.");
        }

        BigDecimal monthlySalary =
                BigDecimal.valueOf(employee.getBasicSalary())
                        .setScale(2, RoundingMode.HALF_UP);

        // Daily rate = Monthly salary / 20 working days
        BigDecimal dailyRate = monthlySalary.divide(
                WORKING_DAYS_PER_MONTH,
                2,
                RoundingMode.HALF_UP
        );

        // Earnings
        BigDecimal basicPay = dailyRate.multiply(
                BigDecimal.valueOf(daysWorked)
        );

        BigDecimal validOvertimePay = money(overtimePay);

        BigDecimal grossIncome = basicPay
                .add(validOvertimePay)
                .setScale(2, RoundingMode.HALF_UP);

        // Benefits
        BigDecimal rice = money(
        BigDecimal.valueOf(employee.getRiceSubsidy())
        );

        BigDecimal phone = money(
                BigDecimal.valueOf(employee.getPhoneAllowance())
        );

        BigDecimal clothing = money(
                BigDecimal.valueOf(employee.getClothingAllowance())
        );

        // Calculate total benefits
        BigDecimal totalBenefits = rice
                .add(phone)
                .add(clothing)
                .setScale(2, RoundingMode.HALF_UP);

        // Deductions
        BigDecimal validSss = money(sss);
        BigDecimal validPhilhealth = money(philhealth);
        BigDecimal validPagibig = money(pagibig);
        BigDecimal validTax = money(withholdingTax);
        BigDecimal validOther = money(otherDeductions);

        BigDecimal totalDeductions = validSss
                .add(validPhilhealth)
                .add(validPagibig)
                .add(validTax)
                .add(validOther)
                .setScale(2, RoundingMode.HALF_UP);

        // Take-home pay
        BigDecimal takeHomePay = grossIncome
                .add(totalBenefits)
                .subtract(totalDeductions)
                .setScale(2, RoundingMode.HALF_UP);

        return new Payroll(
                employee.getEmpNo(),
                employee.getLastName() + ", " + employee.getFirstName(),
                employee.getPosition(),
                periodStart,
                periodEnd,
                monthlySalary,
                dailyRate,
                daysWorked,
                validOvertimePay,
                grossIncome,
                rice,
                phone,
                clothing,
                totalBenefits,
                validSss,
                validPhilhealth,
                validPagibig,
                validTax,
                validOther,
                totalDeductions,
                takeHomePay
        );
    }
    
    public BigDecimal calculateOvertimePay(
                BigDecimal monthlySalary,
                BigDecimal approvedOvertimeHours
        ) {

            BigDecimal workingDays = BigDecimal.valueOf(20);
            BigDecimal workingHours = BigDecimal.valueOf(8);
            BigDecimal overtimeMultiplier = new BigDecimal("1.15");

            BigDecimal hourlyRate = monthlySalary
                    .divide(workingDays.multiply(workingHours), 10,
                            RoundingMode.HALF_UP);

            return hourlyRate
                    .multiply(approvedOvertimeHours)
                    .multiply(overtimeMultiplier)
                    .setScale(2, RoundingMode.HALF_UP);
        }
    
    
}
