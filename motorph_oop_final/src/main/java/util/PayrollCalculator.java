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
            EmployeeData employee, LocalDate periodStart, LocalDate periodEnd, int daysWorked, BigDecimal overtimePay, BigDecimal sss, BigDecimal philhealth, BigDecimal pagibig, BigDecimal withholdingTax, BigDecimal otherDeductions, BigDecimal otherDeductions1) {

        if (employee == null) {
            throw new IllegalArgumentException("Employee is required.");
        }

        if (periodStart == null || periodEnd == null
                || periodEnd.isBefore(periodStart)) {
            throw new IllegalArgumentException("Invalid payroll period.");
        }

        if (daysWorked < 0 || daysWorked > 31) {
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
        BigDecimal validOther = money(otherDeductions);
        
        // Regular taxable compensation
        BigDecimal regularTaxableCompensation = basicPay
                .subtract(validSss)
                .subtract(validPhilhealth)
                .subtract(validPagibig)
                .max(BigDecimal.ZERO);

        // Overtime is supplementary compensation
        BigDecimal validTax = calculateSemiMonthlyTax(
                regularTaxableCompensation,
                validOvertimePay
        );

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
    
        public BigDecimal calculateSemiMonthlyTax(
            BigDecimal regularTaxableCompensation,
            BigDecimal supplementaryCompensation) {

        BigDecimal regular = regularTaxableCompensation.max(BigDecimal.ZERO);
        BigDecimal supplementary = supplementaryCompensation.max(BigDecimal.ZERO);

        BigDecimal tax = BigDecimal.ZERO;
        BigDecimal taxBase = BigDecimal.ZERO;
        BigDecimal rate = BigDecimal.ZERO;

        if (regular.compareTo(new BigDecimal("10417")) <= 0) {
            taxBase = new BigDecimal("10417");
            rate = BigDecimal.ZERO;

        } else if (regular.compareTo(new BigDecimal("16667")) <= 0) {
            taxBase = new BigDecimal("10417");
            rate = new BigDecimal("0.15");

        } else if (regular.compareTo(new BigDecimal("33333")) <= 0) {
            taxBase = new BigDecimal("16667");
            tax = new BigDecimal("937.50");
            rate = new BigDecimal("0.20");

        } else if (regular.compareTo(new BigDecimal("83333")) <= 0) {
            taxBase = new BigDecimal("33333");
            tax = new BigDecimal("4270.70");
            rate = new BigDecimal("0.25");

        } else if (regular.compareTo(new BigDecimal("333333")) <= 0) {
            taxBase = new BigDecimal("83333");
            tax = new BigDecimal("16770.70");
            rate = new BigDecimal("0.30");

        } else {
            taxBase = new BigDecimal("333333");
            tax = new BigDecimal("91770.70");
            rate = new BigDecimal("0.35");
        }

    // Tax on regular compensation
    BigDecimal excessRegular = regular.subtract(taxBase);

    if (excessRegular.signum() > 0) {
        tax = tax.add(excessRegular.multiply(rate));
    }

    // Tax on supplementary compensation, such as overtime
    tax = tax.add(supplementary.multiply(rate));

    return tax.setScale(2, RoundingMode.HALF_UP);
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
