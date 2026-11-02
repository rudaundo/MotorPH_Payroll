/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package test.java;

import java.io.IOException;
import java.time.LocalDate;
import payrollhub.pojo.Payroll;
import payrollhub.service.PayrollService;

/**
 *
 * @author Jae
 */
public class PaySlipTest {
    
    public static void main(String[] args) {

            try {

                PayrollService payrollService = new PayrollService();

                Payroll payroll = payrollService.getPayslip(
                        "10001",
                        LocalDate.of(2024, 6, 8)
                );

                if (payroll != null) {

                    System.out.println("Payslip found!");
                    System.out.println("Employee: " + payroll.getEmployeeName());
                    System.out.println("Period: "
                            + payroll.getPeriodStart()
                            + " to "
                            + payroll.getPeriodEnd());

                    System.out.println("Take Home Pay: "
                            + payroll.getTakeHomePay());

                } else {

                    System.out.println("No payslip found for selected date.");

                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    
    
}
