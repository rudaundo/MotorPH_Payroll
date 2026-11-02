/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package test.java;

import java.math.BigDecimal;
import payrollhub.service.DeductionCalculator;

/**
 *
 * @author Jae
 */
public class DedCalcTest {
    
    
    public static void main(String[] args) {
        
    DeductionCalculator calculator = new DeductionCalculator();

    BigDecimal monthlySalary = new BigDecimal("20000");

    BigDecimal sss = calculator.calculateSSS(monthlySalary);
    BigDecimal philhealth = calculator.calculatePhilHealth(monthlySalary);
    BigDecimal pagibig = calculator.calculatePagIBIG(monthlySalary);

    System.out.println("Monthly SSS: " + sss);
    System.out.println("Monthly PhilHealth: " + philhealth);
    System.out.println("Monthly Pag-IBIG: " + pagibig);

    System.out.println("SSS per cutoff: "
            + calculator.calculatePerCutoff(sss));

    System.out.println("PhilHealth per cutoff: "
            + calculator.calculatePerCutoff(philhealth));

    System.out.println("Pag-IBIG per cutoff: "
            + calculator.calculatePerCutoff(pagibig));
    
    }
    
}
