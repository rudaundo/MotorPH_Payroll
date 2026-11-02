/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package payrollhub.gui;

import com.opencsv.exceptions.CsvException;
import java.io.FileNotFoundException;
import java.io.IOException;
import payrollhub.service.LogoutHandler;
import java.util.List;
import javax.swing.JOptionPane;
import payrollhub.data.CsvHandler;
import payrollhub.service.PayrollService;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Locale;
import javax.swing.JOptionPane;
import payrollhub.pojo.Payroll;
import payrollhub.service.PayrollService;
import java.util.Date;
import javax.swing.table.DefaultTableModel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

/**
 *
 * @author Jae
 */
public class GUIPaySlip extends javax.swing.JFrame {

    private final PayrollService payrollService = new PayrollService();
    
    public GUIPaySlip(String employeeID) throws IOException, FileNotFoundException, CsvException {

        initComponents();

        text_id.setText(employeeID);

        jd_payrollDate.setDateFormatString("yyyy-MM-dd");
        
        readEmployeeData(employeeID);

        loadEmployees();
        
    }
    
    public GUIPaySlip() {
        
        initComponents();
        
        jd_payrollDate.setDateFormatString("yyyy-MM-dd");

        loadEmployees();

    }
    
    private void readEmployeeData(String employeeID) throws FileNotFoundException, IOException, CsvException{
        
        try {
            List<String[]> employees = CsvHandler.readEmployees();

            boolean empdata = false;
        
        
        for (String [] row : employees) {
                if (row[0].trim().equals(employeeID.trim())) {
                    
                    String firstname = row[1].trim();
                    String lastname = row[2].trim();
                    String position = row[11].trim();
                    
                    text_id.setText(row[0].trim());
                    text_name.setText(lastname + "," + firstname);
                    text_position.setText(position);
                    empdata = true;
                    break;
        }   
    }
        } catch (IOException en) {
            en.printStackTrace();
            JOptionPane.showMessageDialog(this,"Error Loading of Data");
        }
    }
    
    private void loadEmployees() {

        try {

            List<String[]> employees = CsvHandler.readEmployees();

            for (String[] row : employees) {

                if (row.length < 3) {
                    continue;
                }

                String empNo = row[0].trim();
                String lastName = row[1].trim();
                String firstName = row[2].trim();

                String employeeName =
                        empNo + " - " + lastName + ", " + firstName;
                
            }

        } catch (IOException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to load employee records: " + e.getMessage(),
                    "Employee Loading Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
    
    private void displayPayslip(Payroll payroll) {

        NumberFormat currency =
                NumberFormat.getCurrencyInstance(
                        Locale.forLanguageTag("en-PH")
                );

        // Employee information
        lbl_employeeID.setText(payroll.getEmpNo());
        lbl_employeeName.setText(payroll.getEmployeeName());
        lbl_position.setText(payroll.getPosition());

        // Payroll period
        lbl_payPeriod.setText(
                payroll.getPeriodStart() + " to "
                + payroll.getPeriodEnd()
        );

        // Earnings
        lbl_monthlySalary.setText(
                currency.format(payroll.getMonthlySalary())
        );

        lbl_dayWorked.setText(
                String.valueOf(payroll.getDaysWorked())
        );

        lbl_overtimePay.setText(
                currency.format(payroll.getOvertimePay())
        );

        lbl_grossIncome.setText(
                currency.format(payroll.getGrossIncome())
        );

        // Allowances and benefits
        lbl_riceSubsidy.setText(
                currency.format(payroll.getRiceSubsidy())
        );

        lbl_phoneAllowance.setText(
                currency.format(payroll.getPhoneAllowance())
        );

        lbl_clothingAllowance.setText(
                currency.format(payroll.getClothingAllowance())
        );

        lbl_totalDeductions.setText(
                currency.format(payroll.getTotalBenefits())
        );

        // Deductions
        lbl_sss.setText(currency.format(payroll.getSss()));
        lbl_philhealth.setText(currency.format(payroll.getPhilhealth()));
        lbl_pagibig.setText(currency.format(payroll.getPagibig()));

        lbl_withholdingTax.setText(
                currency.format(payroll.getWithholdingTax())
        );

        lbl_otherDeductions.setText(
                currency.format(payroll.getOtherDeductions())
        );

        // lbl_totalBenefits1 is your existing total deductions label.
        lbl_totalBenefits1.setText(
                currency.format(payroll.getTotalDeductions())
        );

        // Net pay
        lbl_netPay.setText(
                currency.format(payroll.getTakeHomePay())
        );
    }
    
    private void clearPayslip() {

        lbl_employeeID.setText("");
        lbl_employeeName.setText("");
        lbl_position.setText("");
        lbl_payPeriod.setText("");

        lbl_monthlySalary.setText("");
        lbl_dayWorked.setText("");
        lbl_overtimePay.setText("");
        lbl_grossIncome.setText("");

        lbl_riceSubsidy.setText("");
        lbl_phoneAllowance.setText("");
        lbl_clothingAllowance.setText("");
        lbl_totalDeductions.setText("");

        lbl_sss.setText("");
        lbl_philhealth.setText("");
        lbl_pagibig.setText("");
        lbl_withholdingTax.setText("");
        lbl_otherDeductions.setText("");

        lbl_totalBenefits1.setText("");
        lbl_netPay.setText("");
    }
    
    private void printPayslip() {

        PrinterJob printerJob = PrinterJob.getPrinterJob();

        printerJob.setJobName("MotorPH Employee Payslip");

        printerJob.setPrintable((Graphics graphics,
                PageFormat pageFormat,
                int pageIndex) -> {

            // Print only one page.
            if (pageIndex > 0) {
                return Printable.NO_SUCH_PAGE;
            }

            Graphics2D g2d = (Graphics2D) graphics.create();

            // Calculate the scaling needed to fit the payslip.
            double scaleX = pageFormat.getImageableWidth()
                    / panel_payslip.getWidth();

            double scaleY = pageFormat.getImageableHeight()
                    / panel_payslip.getHeight();

            double scale = Math.min(scaleX, scaleY);

            // Center the payslip on the printable page.
            double x = pageFormat.getImageableX()
                    + (pageFormat.getImageableWidth()
                    - panel_payslip.getWidth() * scale) / 2;

            double y = pageFormat.getImageableY()
                    + (pageFormat.getImageableHeight()
                    - panel_payslip.getHeight() * scale) / 2;

            g2d.translate(x, y);
            g2d.scale(scale, scale);

            panel_payslip.printAll(g2d);

            g2d.dispose();

            return Printable.PAGE_EXISTS;
        });

        if (printerJob.printDialog()) {

            try {
                printerJob.print();

            } catch (PrinterException e) {

                JOptionPane.showMessageDialog(
                        this,
                        "Printing failed: " + e.getMessage(),
                        "Print Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }
    
    
     /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        text_id = new javax.swing.JTextField();
        text_name = new javax.swing.JTextField();
        text_position = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jd_payrollDate = new com.toedter.calendar.JDateChooser();
        jb_cancel = new javax.swing.JButton();
        jb_generate = new javax.swing.JButton();
        jb_close = new javax.swing.JButton();
        jb_empDashSignout = new javax.swing.JButton();
        jb_print = new javax.swing.JButton();
        panel_payslip = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        lbl_employeeID = new javax.swing.JLabel();
        lbl_employeeName = new javax.swing.JLabel();
        lbl_position = new javax.swing.JLabel();
        lbl_payPeriod = new javax.swing.JLabel();
        lbl_monthlySalary = new javax.swing.JLabel();
        lbl_dayWorked = new javax.swing.JLabel();
        lbl_overtimePay = new javax.swing.JLabel();
        lbl_grossIncome = new javax.swing.JLabel();
        lbl_riceSubsidy = new javax.swing.JLabel();
        lbl_phoneAllowance = new javax.swing.JLabel();
        lbl_clothingAllowance = new javax.swing.JLabel();
        lbl_totalDeductions = new javax.swing.JLabel();
        lbl_sss = new javax.swing.JLabel();
        lbl_philhealth = new javax.swing.JLabel();
        lbl_pagibig = new javax.swing.JLabel();
        lbl_totalBenefits1 = new javax.swing.JLabel();
        lbl_withholdingTax = new javax.swing.JLabel();
        lbl_otherDeductions = new javax.swing.JLabel();
        lbl_netPay = new javax.swing.JLabel();
        background = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        text_id.setEditable(false);
        text_id.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                text_idActionPerformed(evt);
            }
        });
        getContentPane().add(text_id, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 140, -1));

        text_name.setEditable(false);
        text_name.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                text_nameActionPerformed(evt);
            }
        });
        getContentPane().add(text_name, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, 140, -1));

        text_position.setEditable(false);
        getContentPane().add(text_position, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 100, 250, -1));

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "PAYSLIP NAVIGATION", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Georgia", 1, 14), new java.awt.Color(0, 0, 204))); // NOI18N
        jPanel1.setOpaque(false);

        jLabel1.setFont(new java.awt.Font("Georgia", 1, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Payroll Date :");

        jd_payrollDate.setFont(new java.awt.Font("Georgia", 1, 12)); // NOI18N

        jb_cancel.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jb_cancel.setText("CANCEL");
        jb_cancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jb_cancelActionPerformed(evt);
            }
        });

        jb_generate.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jb_generate.setText("GENERATE PAYSLIP");
        jb_generate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jb_generateActionPerformed(evt);
            }
        });

        jb_close.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jb_close.setText("HOME");
        jb_close.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jb_closeActionPerformed(evt);
            }
        });

        jb_empDashSignout.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jb_empDashSignout.setText("SIGN OUT");
        jb_empDashSignout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jb_empDashSignoutActionPerformed(evt);
            }
        });

        jb_print.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jb_print.setText("PRINT PAYSLIP");
        jb_print.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jb_printActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jd_payrollDate, javax.swing.GroupLayout.DEFAULT_SIZE, 178, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jb_generate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jb_cancel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jb_close, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jb_empDashSignout, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jb_print, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(25, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jd_payrollDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jb_generate)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jb_print)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jb_cancel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 160, Short.MAX_VALUE)
                .addComponent(jb_close)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jb_empDashSignout)
                .addContainerGap())
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 144, 230, 420));

        panel_payslip.setBackground(new java.awt.Color(255, 255, 255));
        panel_payslip.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "EMPLOYEE PAYSLIP", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Georgia", 1, 14), new java.awt.Color(0, 0, 255))); // NOI18N

        jLabel2.setFont(new java.awt.Font("Georgia", 1, 18)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("MOTORPH");

        jLabel3.setFont(new java.awt.Font("Georgia", 1, 12)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("EMPLOYEE PAYSLIP");

        jLabel4.setFont(new java.awt.Font("Georgia", 0, 10)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel4.setText("EMPLOYEE NUMBER :");
        jLabel4.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        jLabel5.setFont(new java.awt.Font("Georgia", 0, 12)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("------------------------------------------------------------------------------------------------------");

        jLabel6.setFont(new java.awt.Font("Georgia", 0, 12)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("PAYROLL STATEMENT");

        jLabel7.setFont(new java.awt.Font("Georgia", 0, 10)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel7.setText("EMPLOYEE NAME :");
        jLabel7.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        jLabel8.setFont(new java.awt.Font("Georgia", 0, 10)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel8.setText("PAY PERIOD :");
        jLabel8.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        jLabel9.setFont(new java.awt.Font("Georgia", 0, 10)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel9.setText("POSITION :");
        jLabel9.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        jLabel10.setFont(new java.awt.Font("Georgia", 0, 12)); // NOI18N
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("------------------------------------------------------------------------------------------------------");

        jLabel11.setFont(new java.awt.Font("Georgia", 0, 12)); // NOI18N
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel11.setText("I. EARNINGS");

        jLabel12.setFont(new java.awt.Font("Georgia", 0, 10)); // NOI18N
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel12.setText("MONTHLY SALARY");

        jLabel13.setFont(new java.awt.Font("Georgia", 0, 10)); // NOI18N
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel13.setText("DAYS WORKED");

        jLabel14.setFont(new java.awt.Font("Georgia", 0, 10)); // NOI18N
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel14.setText("OVERTIME PAY");

        jLabel15.setFont(new java.awt.Font("Georgia", 1, 10)); // NOI18N
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel15.setText("GROSS INCOME :");

        jLabel16.setFont(new java.awt.Font("Georgia", 0, 12)); // NOI18N
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel16.setText("II. ALLOWANCE AND BENEFITS");

        jLabel17.setFont(new java.awt.Font("Georgia", 0, 10)); // NOI18N
        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel17.setText("RICE SUBSIDY");

        jLabel18.setFont(new java.awt.Font("Georgia", 0, 10)); // NOI18N
        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel18.setText("PHONE ALLOWANCE");

        jLabel19.setFont(new java.awt.Font("Georgia", 0, 10)); // NOI18N
        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel19.setText("CLOTHING ALLOWANCE");

        jLabel20.setFont(new java.awt.Font("Georgia", 1, 10)); // NOI18N
        jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel20.setText("TOTAL BENEFITS :");

        jLabel21.setFont(new java.awt.Font("Georgia", 0, 12)); // NOI18N
        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel21.setText("III. DEDUCTIONS");

        jLabel22.setFont(new java.awt.Font("Georgia", 0, 10)); // NOI18N
        jLabel22.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel22.setText("SSS");

        jLabel23.setFont(new java.awt.Font("Georgia", 0, 10)); // NOI18N
        jLabel23.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel23.setText("PHILHEALTH");

        jLabel24.setFont(new java.awt.Font("Georgia", 0, 10)); // NOI18N
        jLabel24.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel24.setText("PAGIBIG");

        jLabel25.setFont(new java.awt.Font("Georgia", 1, 10)); // NOI18N
        jLabel25.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel25.setText("TOTAL DEDUCTIONS :");

        jLabel26.setFont(new java.awt.Font("Georgia", 0, 10)); // NOI18N
        jLabel26.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel26.setText("WITHHOLDING TAX");

        jLabel27.setFont(new java.awt.Font("Georgia", 0, 10)); // NOI18N
        jLabel27.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel27.setText("OTHER DEDUCTIONS");

        jLabel28.setFont(new java.awt.Font("Georgia", 1, 14)); // NOI18N
        jLabel28.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel28.setText("NET PAY");
        jLabel28.setPreferredSize(new java.awt.Dimension(170, 20));

        lbl_employeeID.setFont(new java.awt.Font("Georgia", 0, 10)); // NOI18N
        lbl_employeeID.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_employeeID.setToolTipText("");

        lbl_employeeName.setFont(new java.awt.Font("Georgia", 0, 10)); // NOI18N
        lbl_employeeName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_employeeName.setToolTipText("");
        lbl_employeeName.setPreferredSize(new java.awt.Dimension(107, 13));

        lbl_position.setFont(new java.awt.Font("Georgia", 0, 10)); // NOI18N
        lbl_position.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_position.setToolTipText("");
        lbl_position.setPreferredSize(new java.awt.Dimension(107, 13));

        lbl_payPeriod.setFont(new java.awt.Font("Georgia", 0, 10)); // NOI18N
        lbl_payPeriod.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_payPeriod.setToolTipText("");
        lbl_payPeriod.setPreferredSize(new java.awt.Dimension(107, 13));

        lbl_monthlySalary.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        lbl_monthlySalary.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lbl_monthlySalary.setToolTipText("");
        lbl_monthlySalary.setPreferredSize(new java.awt.Dimension(170, 13));

        lbl_dayWorked.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        lbl_dayWorked.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lbl_dayWorked.setToolTipText("");
        lbl_dayWorked.setPreferredSize(new java.awt.Dimension(170, 13));

        lbl_overtimePay.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        lbl_overtimePay.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lbl_overtimePay.setToolTipText("");
        lbl_overtimePay.setPreferredSize(new java.awt.Dimension(170, 13));

        lbl_grossIncome.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        lbl_grossIncome.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lbl_grossIncome.setToolTipText("");
        lbl_grossIncome.setPreferredSize(new java.awt.Dimension(170, 13));

        lbl_riceSubsidy.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        lbl_riceSubsidy.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lbl_riceSubsidy.setToolTipText("");
        lbl_riceSubsidy.setPreferredSize(new java.awt.Dimension(170, 13));

        lbl_phoneAllowance.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        lbl_phoneAllowance.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lbl_phoneAllowance.setToolTipText("");
        lbl_phoneAllowance.setPreferredSize(new java.awt.Dimension(170, 13));

        lbl_clothingAllowance.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        lbl_clothingAllowance.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lbl_clothingAllowance.setToolTipText("");
        lbl_clothingAllowance.setPreferredSize(new java.awt.Dimension(170, 13));

        lbl_totalDeductions.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        lbl_totalDeductions.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lbl_totalDeductions.setToolTipText("");
        lbl_totalDeductions.setPreferredSize(new java.awt.Dimension(170, 13));

        lbl_sss.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        lbl_sss.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lbl_sss.setToolTipText("");
        lbl_sss.setPreferredSize(new java.awt.Dimension(170, 13));

        lbl_philhealth.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        lbl_philhealth.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lbl_philhealth.setToolTipText("");
        lbl_philhealth.setPreferredSize(new java.awt.Dimension(170, 13));

        lbl_pagibig.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        lbl_pagibig.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lbl_pagibig.setToolTipText("");
        lbl_pagibig.setPreferredSize(new java.awt.Dimension(170, 13));

        lbl_totalBenefits1.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        lbl_totalBenefits1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lbl_totalBenefits1.setToolTipText("");
        lbl_totalBenefits1.setPreferredSize(new java.awt.Dimension(170, 13));

        lbl_withholdingTax.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        lbl_withholdingTax.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lbl_withholdingTax.setToolTipText("");
        lbl_withholdingTax.setPreferredSize(new java.awt.Dimension(170, 13));

        lbl_otherDeductions.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        lbl_otherDeductions.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lbl_otherDeductions.setToolTipText("");
        lbl_otherDeductions.setPreferredSize(new java.awt.Dimension(170, 13));

        lbl_netPay.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lbl_netPay.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lbl_netPay.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        lbl_netPay.setPreferredSize(new java.awt.Dimension(170, 20));

        javax.swing.GroupLayout panel_payslipLayout = new javax.swing.GroupLayout(panel_payslip);
        panel_payslip.setLayout(panel_payslipLayout);
        panel_payslipLayout.setHorizontalGroup(
            panel_payslipLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_payslipLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_payslipLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel_payslipLayout.createSequentialGroup()
                        .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(panel_payslipLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(panel_payslipLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panel_payslipLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lbl_employeeID, javax.swing.GroupLayout.DEFAULT_SIZE, 118, Short.MAX_VALUE)
                            .addComponent(lbl_employeeName, javax.swing.GroupLayout.DEFAULT_SIZE, 118, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panel_payslipLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panel_payslipLayout.createSequentialGroup()
                                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lbl_payPeriod, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(panel_payslipLayout.createSequentialGroup()
                                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lbl_position, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGap(11, 11, 11))
                    .addGroup(panel_payslipLayout.createSequentialGroup()
                        .addGroup(panel_payslipLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(panel_payslipLayout.createSequentialGroup()
                        .addGroup(panel_payslipLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panel_payslipLayout.createSequentialGroup()
                                .addGap(38, 38, 38)
                                .addGroup(panel_payslipLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(167, 167, 167)
                                .addGroup(panel_payslipLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lbl_phoneAllowance, javax.swing.GroupLayout.DEFAULT_SIZE, 133, Short.MAX_VALUE)
                                    .addComponent(lbl_totalDeductions, javax.swing.GroupLayout.DEFAULT_SIZE, 133, Short.MAX_VALUE)
                                    .addComponent(lbl_clothingAllowance, javax.swing.GroupLayout.DEFAULT_SIZE, 133, Short.MAX_VALUE)
                                    .addComponent(lbl_riceSubsidy, javax.swing.GroupLayout.DEFAULT_SIZE, 133, Short.MAX_VALUE)))
                            .addGroup(panel_payslipLayout.createSequentialGroup()
                                .addGap(37, 37, 37)
                                .addGroup(panel_payslipLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(panel_payslipLayout.createSequentialGroup()
                                        .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(168, 168, 168)
                                        .addComponent(lbl_otherDeductions, javax.swing.GroupLayout.DEFAULT_SIZE, 0, Short.MAX_VALUE))
                                    .addGroup(panel_payslipLayout.createSequentialGroup()
                                        .addGroup(panel_payslipLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(168, 168, 168)
                                        .addGroup(panel_payslipLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(lbl_philhealth, javax.swing.GroupLayout.DEFAULT_SIZE, 133, Short.MAX_VALUE)
                                            .addComponent(lbl_pagibig, javax.swing.GroupLayout.DEFAULT_SIZE, 133, Short.MAX_VALUE)
                                            .addComponent(lbl_sss, javax.swing.GroupLayout.DEFAULT_SIZE, 133, Short.MAX_VALUE)
                                            .addComponent(lbl_withholdingTax, javax.swing.GroupLayout.DEFAULT_SIZE, 0, Short.MAX_VALUE)))
                                    .addGroup(panel_payslipLayout.createSequentialGroup()
                                        .addGroup(panel_payslipLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(168, 168, 168)
                                        .addGroup(panel_payslipLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(lbl_totalBenefits1, javax.swing.GroupLayout.DEFAULT_SIZE, 133, Short.MAX_VALUE)
                                            .addComponent(lbl_netPay, javax.swing.GroupLayout.DEFAULT_SIZE, 0, Short.MAX_VALUE))))))
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel_payslipLayout.createSequentialGroup()
                        .addGap(41, 41, 41)
                        .addGroup(panel_payslipLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panel_payslipLayout.createSequentialGroup()
                                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(164, 164, 164)
                                .addComponent(lbl_dayWorked, javax.swing.GroupLayout.DEFAULT_SIZE, 133, Short.MAX_VALUE))
                            .addGroup(panel_payslipLayout.createSequentialGroup()
                                .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(164, 164, 164)
                                .addComponent(lbl_grossIncome, javax.swing.GroupLayout.DEFAULT_SIZE, 133, Short.MAX_VALUE))
                            .addGroup(panel_payslipLayout.createSequentialGroup()
                                .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(164, 164, 164)
                                .addComponent(lbl_overtimePay, javax.swing.GroupLayout.DEFAULT_SIZE, 133, Short.MAX_VALUE))
                            .addGroup(panel_payslipLayout.createSequentialGroup()
                                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(164, 164, 164)
                                .addComponent(lbl_monthlySalary, javax.swing.GroupLayout.DEFAULT_SIZE, 133, Short.MAX_VALUE)))
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel_payslipLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(panel_payslipLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel_payslipLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(panel_payslipLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel_payslipLayout.createSequentialGroup()
                                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(175, 175, 175))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel_payslipLayout.createSequentialGroup()
                                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(157, 157, 157))))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel_payslipLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel10)
                                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE))))))
        );
        panel_payslipLayout.setVerticalGroup(
            panel_payslipLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_payslipLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_payslipLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panel_payslipLayout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panel_payslipLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(jLabel8)
                            .addComponent(lbl_employeeID, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(lbl_payPeriod, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_payslipLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panel_payslipLayout.createSequentialGroup()
                        .addGroup(panel_payslipLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panel_payslipLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel7)
                                .addComponent(jLabel9))
                            .addComponent(lbl_employeeName, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel10))
                    .addComponent(lbl_position, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_payslipLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panel_payslipLayout.createSequentialGroup()
                        .addGroup(panel_payslipLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(panel_payslipLayout.createSequentialGroup()
                                .addGroup(panel_payslipLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(panel_payslipLayout.createSequentialGroup()
                                        .addComponent(jLabel12)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel13))
                                    .addGroup(panel_payslipLayout.createSequentialGroup()
                                        .addComponent(lbl_monthlySalary, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(lbl_dayWorked, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel14))
                            .addComponent(lbl_overtimePay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel15))
                    .addComponent(lbl_grossIncome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel16)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_payslipLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panel_payslipLayout.createSequentialGroup()
                        .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel18)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel19)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel20))
                    .addGroup(panel_payslipLayout.createSequentialGroup()
                        .addGroup(panel_payslipLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(panel_payslipLayout.createSequentialGroup()
                                .addComponent(lbl_riceSubsidy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lbl_phoneAllowance, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(19, 19, 19))
                            .addComponent(lbl_clothingAllowance, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(19, 19, 19))
                    .addComponent(lbl_totalDeductions, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel21)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_payslipLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panel_payslipLayout.createSequentialGroup()
                        .addGroup(panel_payslipLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(panel_payslipLayout.createSequentialGroup()
                                .addComponent(jLabel22)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel23)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel24)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel26))
                            .addGroup(panel_payslipLayout.createSequentialGroup()
                                .addGroup(panel_payslipLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(panel_payslipLayout.createSequentialGroup()
                                        .addComponent(lbl_sss, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(lbl_philhealth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(19, 19, 19))
                                    .addComponent(lbl_pagibig, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lbl_withholdingTax, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel27))
                    .addComponent(lbl_otherDeductions, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_payslipLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel25)
                    .addComponent(lbl_totalBenefits1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_payslipLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_netPay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(22, Short.MAX_VALUE))
        );

        getContentPane().add(panel_payslip, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 10, 490, 550));

        background.setBackground(new java.awt.Color(255, 255, 255));
        background.setIcon(new javax.swing.ImageIcon(System.getProperty("user.dir") + ("\\src\\main\\java\\images\\bluebg.png")));
        background.setPreferredSize(new java.awt.Dimension(900, 600));
        getContentPane().add(background, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 800, 580));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void text_idActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_text_idActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_text_idActionPerformed

    private void text_nameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_text_nameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_text_nameActionPerformed

    private void jb_empDashSignoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jb_empDashSignoutActionPerformed

        LogoutHandler handler = new LogoutHandler();
        handler.logout(this);

    }//GEN-LAST:event_jb_empDashSignoutActionPerformed

    private void jb_closeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jb_closeActionPerformed
      
        this.dispose();
        
    }//GEN-LAST:event_jb_closeActionPerformed

    private void jb_cancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jb_cancelActionPerformed

        jd_payrollDate.setDate(null);

        clearPayslip();
        
    }//GEN-LAST:event_jb_cancelActionPerformed

    private void jb_generateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jb_generateActionPerformed
        
      // Check whether a payroll date was selected.
        if (jd_payrollDate.getDate() == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a payroll date.",
                    "Missing Payroll Date",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        // Convert the selected date into LocalDate.
        LocalDate selectedDate = jd_payrollDate.getDate()
                .toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        String employeeID = text_id.getText().trim();

        try {

            Payroll payroll = payrollService.getPayslip(
                    employeeID,
                    selectedDate
            );

            if (payroll == null) {

                clearPayslip();

                JOptionPane.showMessageDialog(
                        this,
                        "No payroll record found for the selected date.",
                        "Payslip Not Found",
                        JOptionPane.INFORMATION_MESSAGE
                );

                return;
            }

            // Display the retrieved payroll record.
            displayPayslip(payroll);

        } catch (IOException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to retrieve payslip: " + e.getMessage(),
                    "Payroll Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
        
    }//GEN-LAST:event_jb_generateActionPerformed

    private void jb_printActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jb_printActionPerformed
       
        printPayslip();
        
        
    }//GEN-LAST:event_jb_printActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(GUIPaySlip.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GUIPaySlip.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GUIPaySlip.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GUIPaySlip.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GUIPaySlip().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel background;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JButton jb_cancel;
    private javax.swing.JButton jb_close;
    private javax.swing.JButton jb_empDashSignout;
    private javax.swing.JButton jb_generate;
    private javax.swing.JButton jb_print;
    private com.toedter.calendar.JDateChooser jd_payrollDate;
    private javax.swing.JLabel lbl_clothingAllowance;
    private javax.swing.JLabel lbl_dayWorked;
    private javax.swing.JLabel lbl_employeeID;
    private javax.swing.JLabel lbl_employeeName;
    private javax.swing.JLabel lbl_grossIncome;
    private javax.swing.JLabel lbl_monthlySalary;
    private javax.swing.JLabel lbl_netPay;
    private javax.swing.JLabel lbl_otherDeductions;
    private javax.swing.JLabel lbl_overtimePay;
    private javax.swing.JLabel lbl_pagibig;
    private javax.swing.JLabel lbl_payPeriod;
    private javax.swing.JLabel lbl_philhealth;
    private javax.swing.JLabel lbl_phoneAllowance;
    private javax.swing.JLabel lbl_position;
    private javax.swing.JLabel lbl_riceSubsidy;
    private javax.swing.JLabel lbl_sss;
    private javax.swing.JLabel lbl_totalBenefits1;
    private javax.swing.JLabel lbl_totalDeductions;
    private javax.swing.JLabel lbl_withholdingTax;
    private javax.swing.JPanel panel_payslip;
    private javax.swing.JTextField text_id;
    private javax.swing.JTextField text_name;
    private javax.swing.JTextField text_position;
    // End of variables declaration//GEN-END:variables
}
