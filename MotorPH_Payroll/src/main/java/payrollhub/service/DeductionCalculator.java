/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package payrollhub.service;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 *
 * @author Jae
 */


public class DeductionCalculator {

        private static final BigDecimal ZERO = BigDecimal.ZERO;
        private static final BigDecimal TWO = new BigDecimal("2");
        private static final BigDecimal FIVE_PERCENT = new BigDecimal("0.05");

        // SSS employee share: 5% of Monthly Salary Credit (MSC)
        public BigDecimal calculateSSS(BigDecimal monthlySalary) {

            if (monthlySalary == null || monthlySalary.compareTo(ZERO) <= 0) {
                return ZERO.setScale(2);
            }

            BigDecimal msc = monthlySalary
                    .divide(new BigDecimal("500"), 0, RoundingMode.HALF_UP)
                    .multiply(new BigDecimal("500"));

            // Minimum MSC: ₱5,000
            if (msc.compareTo(new BigDecimal("5000")) < 0) {
                msc = new BigDecimal("5000");
            }

            // Maximum MSC: ₱35,000
            if (msc.compareTo(new BigDecimal("35000")) > 0) {
                msc = new BigDecimal("35000");
            }

            return msc.multiply(FIVE_PERCENT)
                    .setScale(2, RoundingMode.HALF_UP);
        }

        // PhilHealth employee share: half of the 5% monthly premium
        public BigDecimal calculatePhilHealth(BigDecimal monthlyBasicSalary) {

            if (monthlyBasicSalary == null || monthlyBasicSalary.compareTo(ZERO) <= 0) {
                return ZERO.setScale(2);
            }

            BigDecimal salary = monthlyBasicSalary;

            if (salary.compareTo(new BigDecimal("10000")) < 0) {
                salary = new BigDecimal("10000");
            }

            if (salary.compareTo(new BigDecimal("100000")) > 0) {
                salary = new BigDecimal("100000");
            }

            return salary.multiply(FIVE_PERCENT)
                    .divide(TWO, 2, RoundingMode.HALF_UP);
        }

        // Pag-IBIG employee share
        public BigDecimal calculatePagIBIG(BigDecimal monthlyFundSalary) {

            if (monthlyFundSalary == null || monthlyFundSalary.compareTo(ZERO) <= 0) {
                return ZERO.setScale(2);
            }

            BigDecimal fundSalary = monthlyFundSalary;

            // Maximum fund salary: ₱10,000
            if (fundSalary.compareTo(new BigDecimal("10000")) > 0) {
                fundSalary = new BigDecimal("10000");
            }

            BigDecimal rate = fundSalary.compareTo(new BigDecimal("1500")) <= 0
                    ? new BigDecimal("0.01")
                    : new BigDecimal("0.02");

            return fundSalary.multiply(rate)
                    .setScale(2, RoundingMode.HALF_UP);
        }

        // Allocate monthly employee contribution into two payroll cutoffs
        public BigDecimal calculatePerCutoff(BigDecimal monthlyContribution) {

            if (monthlyContribution == null) {
                return ZERO.setScale(2);
            }

            return monthlyContribution.divide(TWO, 2, RoundingMode.HALF_UP);
        }
    }
