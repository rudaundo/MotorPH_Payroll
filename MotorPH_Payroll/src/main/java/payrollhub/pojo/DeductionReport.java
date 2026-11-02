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

public class DeductionReport {

    private final String empNo;
    private final String employeeName;
    private final LocalDate periodStart;
    private final LocalDate periodEnd;

    private final BigDecimal sss;
    private final BigDecimal philhealth;
    private final BigDecimal pagibig;
    private final BigDecimal withholdingTax;
    private final BigDecimal otherDeductions;
    private final BigDecimal totalDeductions;

    public DeductionReport(
            String empNo,
            String employeeName,
            LocalDate periodStart,
            LocalDate periodEnd,
            BigDecimal sss,
            BigDecimal philhealth,
            BigDecimal pagibig,
            BigDecimal withholdingTax,
            BigDecimal otherDeductions,
            BigDecimal totalDeductions) {

        this.empNo = empNo;
        this.employeeName = employeeName;
        this.periodStart = periodStart;
        this.periodEnd = periodEnd;
        this.sss = sss;
        this.philhealth = philhealth;
        this.pagibig = pagibig;
        this.withholdingTax = withholdingTax;
        this.otherDeductions = otherDeductions;
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

    public BigDecimal getSss() {
        return sss;
    }

    public BigDecimal getPhilhealth() {
        return philhealth;
    }

    public BigDecimal getPagibig() {
        return pagibig;
    }

    public BigDecimal getWithholdingTax() {
        return withholdingTax;
    }

    public BigDecimal getOtherDeductions() {
        return otherDeductions;
    }

    public BigDecimal getTotalDeductions() {
        return totalDeductions;
    }
}