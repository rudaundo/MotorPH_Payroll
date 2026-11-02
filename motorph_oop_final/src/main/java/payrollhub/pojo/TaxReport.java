/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package payrollhub.pojo;

/**
 *
 * @author Jae
 */
import java.math.BigDecimal;
import java.time.LocalDate;

public class TaxReport {

    private final String empNo;
    private final String employeeName;
    private final LocalDate periodStart;
    private final LocalDate periodEnd;
    private final BigDecimal grossIncome;
    private final BigDecimal withholdingTax;
    private final BigDecimal totalDeductions;

    public TaxReport(
            String empNo,
            String employeeName,
            LocalDate periodStart,
            LocalDate periodEnd,
            BigDecimal grossIncome,
            BigDecimal withholdingTax,
            BigDecimal totalDeductions) {

        this.empNo = empNo;
        this.employeeName = employeeName;
        this.periodStart = periodStart;
        this.periodEnd = periodEnd;
        this.grossIncome = grossIncome;
        this.withholdingTax = withholdingTax;
        this.totalDeductions = totalDeductions;
    }

    public String getEmpNo() {
        return empNo;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public LocalDate getPeriodStart() {
        return periodStart;
    }

    public LocalDate getPeriodEnd() {
        return periodEnd;
    }

    public BigDecimal getGrossIncome() {
        return grossIncome;
    }

    public BigDecimal getWithholdingTax() {
        return withholdingTax;
    }

    public BigDecimal getTotalDeductions() {
        return totalDeductions;
    }
}
