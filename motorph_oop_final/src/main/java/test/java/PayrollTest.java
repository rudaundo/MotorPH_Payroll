/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package test.java;

/**
 *
 * @author Jae
 */
import java.math.BigDecimal;
import java.time.LocalDate;
import payrollhub.pojo.Payroll;
import payrollhub.service.PayrollService;

public class PayrollTest {

    public static void main(String[] args) {

        PayrollService payrollService = new PayrollService();

        String empNo = "10015";

        LocalDate periodStart = LocalDate.of(2024, 6, 1);
        LocalDate periodEnd = LocalDate.of(2024, 6, 15);

        BigDecimal zero = BigDecimal.ZERO;

        try {

            Payroll payroll = payrollService.processPayroll(
                    empNo,
                    periodStart,
                    periodEnd,
                    zero, // This is replaced by automatically calculated OT pay
                    zero, // SSS
                    zero, // PhilHealth
                    zero, // Pag-IBIG
                    zero, // Withholding tax
                    zero  // Other deductions
            );

            System.out.println("===== PAYROLL TEST =====");
            System.out.println("Employee: " + payroll.getEmployeeName());
            System.out.println("Days Worked: " + payroll.getDaysWorked());
            System.out.println("Overtime Pay: " + payroll.getOvertimePay());
            System.out.println("Gross Income: " + payroll.getGrossIncome());
            System.out.println("Total Benefits: " + payroll.getTotalBenefits());
            System.out.println("Total Deductions: " + payroll.getTotalDeductions());
            System.out.println("Take Home Pay: " + payroll.getTakeHomePay());

        } catch (Exception e) {
            System.out.println("Payroll test failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
