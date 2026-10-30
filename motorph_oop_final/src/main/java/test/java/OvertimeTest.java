/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package test.java;

import java.math.BigDecimal;
import java.time.LocalDate;
import payrollhub.service.OvertimeService;
import util.PayrollCalculator;

/**
 *
 * @author Jae
 */
public class OvertimeTest {
    
    public static void main(String[] args) {

        PayrollCalculator calculator = new PayrollCalculator();

BigDecimal overtimePay = calculator.calculateOvertimePay(
        new BigDecimal("53500"),
        new BigDecimal("4")
);

System.out.println("Calculated OT Pay: " + overtimePay);
    }
    
}
