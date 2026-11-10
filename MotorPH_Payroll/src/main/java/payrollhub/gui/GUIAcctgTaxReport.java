/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package payrollhub.gui;

import com.opencsv.exceptions.CsvException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import payrollhub.service.LogoutHandler;
import java.time.LocalDate;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import payrollhub.data.CsvHandler;
import payrollhub.pojo.Payroll;
import payrollhub.service.PayrollService;
import payrollhub.pojo.TaxReport;
import java.time.format.DateTimeFormatter;
import java.text.NumberFormat;
import java.util.Locale;

/**
 *
 * @author Jae
 */
public class GUIAcctgTaxReport extends javax.swing.JFrame {    
    
    public GUIAcctgTaxReport(String employeeID) throws IOException, FileNotFoundException, CsvException {

        initComponents();

        text_id.setText(employeeID);

        jd_start.setDateFormatString("yyyy-MM-dd");
        jd_end.setDateFormatString("yyyy-MM-dd");
        
        readEmployeeData(employeeID);
        
        jc_employee.setVisible(false);

        loadEmployees();

        jr_allEmp.setSelected(true);
        
    }
    
    public GUIAcctgTaxReport() {
        
        initComponents();
        
        jd_start.setDateFormatString("yyyy-MM-dd");
        jd_end.setDateFormatString("yyyy-MM-dd");
        
        jc_employee.setVisible(false);

        loadEmployees();

        jr_allEmp.setSelected(true);

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

            jc_employee.removeAllItems();

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

                jc_employee.addItem(employeeName);
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
    
    private void displayTaxReport(List<TaxReport> taxReports) {

        DefaultTableModel model = (DefaultTableModel) tbl_taxReport.getModel();

        model.setRowCount(0);

        BigDecimal totalTax = BigDecimal.ZERO;

        NumberFormat currencyFormat =
                NumberFormat.getCurrencyInstance(
                        Locale.forLanguageTag("en-PH")
                );

        DateTimeFormatter dateFormat =
                DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for (TaxReport report : taxReports) {

            BigDecimal withholdingTax = report.getWithholdingTax();

            if (withholdingTax == null) {
                withholdingTax = BigDecimal.ZERO;
            }

            String payrollPeriod =
                    report.getPeriodStart().format(dateFormat)
                    + " to "
                    + report.getPeriodEnd().format(dateFormat);

            model.addRow(new Object[] {
                report.getEmpNo(),
                report.getEmployeeName(),
                payrollPeriod,
                currencyFormat.format(report.getGrossIncome()),
                currencyFormat.format(withholdingTax),
                currencyFormat.format(report.getTotalDeductions())
            });

            totalTax = totalTax.add(withholdingTax);
        }

        jl_totalTax.setText(currencyFormat.format(totalTax));
    }
    
    
     /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        bg_employeeSelection = new javax.swing.ButtonGroup();
        text_id = new javax.swing.JTextField();
        text_name = new javax.swing.JTextField();
        text_position = new javax.swing.JTextField();
        jb_close = new javax.swing.JButton();
        jb_empDashSignout = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jd_start = new com.toedter.calendar.JDateChooser();
        jd_end = new com.toedter.calendar.JDateChooser();
        jLabel3 = new javax.swing.JLabel();
        jb_cancel = new javax.swing.JButton();
        jb_generate = new javax.swing.JButton();
        jr_allEmp = new javax.swing.JRadioButton();
        jr_selectedEmp = new javax.swing.JRadioButton();
        jc_employee = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_taxReport = new javax.swing.JTable();
        jl_totalTax = new javax.swing.JLabel();
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

        jb_close.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jb_close.setText("HOME");
        jb_close.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jb_closeActionPerformed(evt);
            }
        });
        getContentPane().add(jb_close, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 20, 100, -1));

        jb_empDashSignout.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jb_empDashSignout.setText("SIGN OUT");
        jb_empDashSignout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jb_empDashSignoutActionPerformed(evt);
            }
        });
        getContentPane().add(jb_empDashSignout, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 60, 100, -1));

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "TAX REPORT FORM", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Georgia", 1, 14), new java.awt.Color(0, 0, 204))); // NOI18N
        jPanel1.setOpaque(false);

        jLabel1.setFont(new java.awt.Font("Georgia", 1, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Start Date:");

        jLabel2.setFont(new java.awt.Font("Georgia", 1, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("End Date :");

        jd_start.setFont(new java.awt.Font("Georgia", 1, 12)); // NOI18N

        jd_end.setFont(new java.awt.Font("Georgia", 1, 12)); // NOI18N

        jLabel3.setFont(new java.awt.Font("Georgia", 1, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Employee Selection");

        jb_cancel.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jb_cancel.setText("CANCEL");
        jb_cancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jb_cancelActionPerformed(evt);
            }
        });

        jb_generate.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jb_generate.setText("GENERATE");
        jb_generate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jb_generateActionPerformed(evt);
            }
        });

        bg_employeeSelection.add(jr_allEmp);
        jr_allEmp.setFont(new java.awt.Font("Georgia", 1, 14)); // NOI18N
        jr_allEmp.setForeground(new java.awt.Color(255, 255, 255));
        jr_allEmp.setText("All Employees");
        jr_allEmp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jr_allEmpActionPerformed(evt);
            }
        });

        bg_employeeSelection.add(jr_selectedEmp);
        jr_selectedEmp.setFont(new java.awt.Font("Georgia", 1, 14)); // NOI18N
        jr_selectedEmp.setForeground(new java.awt.Color(255, 255, 255));
        jr_selectedEmp.setText("Selected Employees");
        jr_selectedEmp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jr_selectedEmpActionPerformed(evt);
            }
        });

        tbl_taxReport.setModel(new javax.swing.table.DefaultTableModel(
            new Object[][] {},
            new String[] {
                "Employee ID",
                "Employee Name",
                "Payroll Period",
                "Gross Income",
                "Withholding Tax",
                "Total Deductions"
            }
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        }
    );
    jScrollPane1.setViewportView(tbl_taxReport);

    javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
    jPanel1.setLayout(jPanel1Layout);
    jPanel1Layout.setHorizontalGroup(
        jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
            .addGap(17, 17, 17)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                .addComponent(jScrollPane1)
                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(18, 18, 18)
                    .addComponent(jd_start, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 45, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(jd_end, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(jr_selectedEmp, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(jc_employee, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(52, 52, 52))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jr_allEmp, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jb_generate, javax.swing.GroupLayout.DEFAULT_SIZE, 168, Short.MAX_VALUE)
                        .addComponent(jb_cancel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
            .addGap(20, 20, 20))
    );
    jPanel1Layout.setVerticalGroup(
        jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel1Layout.createSequentialGroup()
            .addGap(18, 18, 18)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jLabel1)
                .addComponent(jLabel2)
                .addComponent(jd_end, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jd_start, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGap(18, 18, 18)
            .addComponent(jLabel3)
            .addGap(18, 18, 18)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jr_allEmp)
                .addComponent(jb_generate))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jr_selectedEmp)
                .addComponent(jc_employee, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jb_cancel))
            .addGap(18, 18, 18)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addContainerGap(40, Short.MAX_VALUE))
    );

    getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 144, 630, 440));

    jl_totalTax.setFont(new java.awt.Font("Georgia", 1, 24)); // NOI18N
    jl_totalTax.setForeground(new java.awt.Color(255, 255, 255));
    jl_totalTax.setToolTipText("");
    jl_totalTax.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Total Tax :", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Georgia", 1, 14), new java.awt.Color(0, 0, 255))); // NOI18N
    jl_totalTax.setPreferredSize(new java.awt.Dimension(200, 50));
    getContentPane().add(jl_totalTax, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 100, 250, -1));

    background.setIcon(new javax.swing.ImageIcon(System.getProperty("user.dir") + ("\\src\\main\\java\\images\\bluebg.png")));
    background.setPreferredSize(new java.awt.Dimension(900, 600));
    getContentPane().add(background, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 650, 630));

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

        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to reset the tax report form?",
            "Confirm Reset",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
    );

    if (confirm == JOptionPane.YES_OPTION) {

        // Clear date selection
        jd_start.setDate(null);
        jd_end.setDate(null);

        // Reset employee selection
        bg_employeeSelection.clearSelection();
        jr_allEmp.setSelected(true);

        // Hide employee dropdown
        jc_employee.setVisible(false);

        // Reset employee dropdown
        if (jc_employee.getItemCount() > 0) {
            jc_employee.setSelectedIndex(0);
        }

        // Clear tax report table
        DefaultTableModel model =
                (DefaultTableModel) tbl_taxReport.getModel();

        model.setRowCount(0);

        // Reset total tax
        NumberFormat currencyFormat =
                NumberFormat.getCurrencyInstance(
                        Locale.forLanguageTag("en-PH")
                );

        jl_totalTax.setText(currencyFormat.format(BigDecimal.ZERO));

        // Refresh form
        revalidate();
        repaint();

        JOptionPane.showMessageDialog(
                this,
                "Tax report form has been reset.",
                "Reset Successful",
                JOptionPane.INFORMATION_MESSAGE
        );
    }   
        
    }//GEN-LAST:event_jb_cancelActionPerformed

    private void jb_generateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jb_generateActionPerformed

        // Validate date selection
        if (jd_start.getDate() == null
                || jd_end.getDate() == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select both start and end dates.",
                    "Missing Dates",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        // Convert selected dates to LocalDate
        LocalDate startDate = jd_start.getDate().toInstant()
                .atZone(java.time.ZoneId.systemDefault())
                .toLocalDate();

        LocalDate endDate = jd_end.getDate().toInstant()
                .atZone(java.time.ZoneId.systemDefault())
                .toLocalDate();

        // Validate date range
        if (endDate.isBefore(startDate)) {

            JOptionPane.showMessageDialog(
                    this,
                    "End date cannot be earlier than start date.",
                    "Invalid Date Range",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        // Validate employee selection
        if (!jr_allEmp.isSelected()
                && !jr_selectedEmp.isSelected()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select an employee report option.",
                    "Missing Selection",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        String empNo = null;

        // Retrieve employee ID if Selected Employee is chosen
        if (jr_selectedEmp.isSelected()) {

            String selectedEmployee =
                    (String) jc_employee.getSelectedItem();

            if (selectedEmployee == null) {

                JOptionPane.showMessageDialog(
                        this,
                        "Please select an employee.",
                        "Missing Employee",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }

            empNo = selectedEmployee.split(" - ", 2)[0].trim();
        }

        try {

            PayrollService payrollService = new PayrollService();

            List<TaxReport> taxReports =
                    payrollService.getTaxReports(
                            startDate,
                            endDate,
                            empNo
                    );

            displayTaxReport(taxReports);

            if (taxReports.isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "No tax report records were found for the selected criteria.",
                        "No Records",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }

        } catch (IOException | RuntimeException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to generate tax report: " + e.getMessage(),
                    "Tax Report Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
        
    }//GEN-LAST:event_jb_generateActionPerformed

    private void jr_allEmpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jr_allEmpActionPerformed
       
        jc_employee.setVisible(false);
                
    }//GEN-LAST:event_jr_allEmpActionPerformed

    private void jr_selectedEmpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jr_selectedEmpActionPerformed
      
        jc_employee.setVisible(true);
        
    }//GEN-LAST:event_jr_selectedEmpActionPerformed

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
            java.util.logging.Logger.getLogger(GUIAcctgTaxReport.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GUIAcctgTaxReport.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GUIAcctgTaxReport.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GUIAcctgTaxReport.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
       
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GUIAcctgTaxReport().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel background;
    private javax.swing.ButtonGroup bg_employeeSelection;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton jb_cancel;
    private javax.swing.JButton jb_close;
    private javax.swing.JButton jb_empDashSignout;
    private javax.swing.JButton jb_generate;
    private javax.swing.JComboBox<String> jc_employee;
    private com.toedter.calendar.JDateChooser jd_end;
    private com.toedter.calendar.JDateChooser jd_start;
    private javax.swing.JLabel jl_totalTax;
    private javax.swing.JRadioButton jr_allEmp;
    private javax.swing.JRadioButton jr_selectedEmp;
    private javax.swing.JTable tbl_taxReport;
    private javax.swing.JTextField text_id;
    private javax.swing.JTextField text_name;
    private javax.swing.JTextField text_position;
    // End of variables declaration//GEN-END:variables
}
