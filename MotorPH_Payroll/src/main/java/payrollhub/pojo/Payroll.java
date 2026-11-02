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

public class Payroll {

    private String empNo;
    private String employeeName;
    private String position;
    private LocalDate periodStart;
    private LocalDate periodEnd;

    private BigDecimal monthlySalary;
    private BigDecimal dailyRate;
    private int daysWorked;
    private BigDecimal overtimePay;
    private BigDecimal grossIncome;

    private BigDecimal riceSubsidy;
    private BigDecimal phoneAllowance;
    private BigDecimal clothingAllowance;
    private BigDecimal totalBenefits;

    private BigDecimal sss;
    private BigDecimal philhealth;
    private BigDecimal pagibig;
    private BigDecimal withholdingTax;
    private BigDecimal otherDeductions;
    private BigDecimal totalDeductions;

    private BigDecimal takeHomePay;

    public Payroll(
            String empNo,
            String employeeName,
            String position,
            LocalDate periodStart,
            LocalDate periodEnd,
            BigDecimal monthlySalary,
            BigDecimal dailyRate,
            int daysWorked,
            BigDecimal overtimePay,
            BigDecimal grossIncome,
            BigDecimal riceSubsidy,
            BigDecimal phoneAllowance,
            BigDecimal clothingAllowance,
            BigDecimal totalBenefits,
            BigDecimal sss,
            BigDecimal philhealth,
            BigDecimal pagibig,
            BigDecimal withholdingTax,
            BigDecimal otherDeductions,
            BigDecimal totalDeductions,
            BigDecimal takeHomePay) {

        this.empNo = empNo;
        this.employeeName = employeeName;
        this.position = position;
        this.periodStart = periodStart;
        this.periodEnd = periodEnd;
        this.monthlySalary = monthlySalary;
        this.dailyRate = dailyRate;
        this.daysWorked = daysWorked;
        this.overtimePay = overtimePay;
        this.grossIncome = grossIncome;
        this.riceSubsidy = riceSubsidy;
        this.phoneAllowance = phoneAllowance;
        this.clothingAllowance = clothingAllowance;
        this.totalBenefits = totalBenefits;
        this.sss = sss;
        this.philhealth = philhealth;
        this.pagibig = pagibig;
        this.withholdingTax = withholdingTax;
        this.otherDeductions = otherDeductions;
        this.totalDeductions = totalDeductions;
        this.takeHomePay = takeHomePay;
    }

    public String getEmpNo() { return empNo; }
    public String getEmployeeName() { return employeeName; }
    public String getPosition() { return position; }

    public LocalDate getPeriodStart() { return periodStart; }
    public LocalDate getPeriodEnd() { return periodEnd; }

    public BigDecimal getMonthlySalary() { return monthlySalary; }
    public BigDecimal getDailyRate() { return dailyRate; }
    public int getDaysWorked() { return daysWorked; }
    public BigDecimal getOvertimePay() { return overtimePay; }
    public BigDecimal getGrossIncome() { return grossIncome; }

    public BigDecimal getRiceSubsidy() { return riceSubsidy; }
    public BigDecimal getPhoneAllowance() { return phoneAllowance; }
    public BigDecimal getClothingAllowance() { return clothingAllowance; }
    public BigDecimal getTotalBenefits() { return totalBenefits; }

    public BigDecimal getSss() { return sss; }
    public BigDecimal getPhilhealth() { return philhealth; }
    public BigDecimal getPagibig() { return pagibig; }
    public BigDecimal getWithholdingTax() { return withholdingTax; }
    public BigDecimal getOtherDeductions() { return otherDeductions; }
    public BigDecimal getTotalDeductions() { return totalDeductions; }

    public BigDecimal getTakeHomePay() { return takeHomePay; }
    
    
}