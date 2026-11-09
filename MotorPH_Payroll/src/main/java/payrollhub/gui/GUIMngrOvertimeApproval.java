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
import javax.swing.table.DefaultTableModel;
import payrollhub.data.CsvHandler;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


/**
 *
 * @author Jae
 */
public class GUIMngrOvertimeApproval extends javax.swing.JFrame {    
    
    public GUIMngrOvertimeApproval(String employeeID) throws IOException, FileNotFoundException, CsvException {

        initComponents();

        initializeOvertimeTable();

        text_id.setText(employeeID);

        readEmployeeData(employeeID);

        loadEmployees();

        jc_employee.setVisible(false);

        jr_allEmp.setSelected(true);

        loadOvertimeRequests();
        
    }
    
    public GUIMngrOvertimeApproval() {
        
        initComponents();

        initializeOvertimeTable();

        loadEmployees();

        jc_employee.setVisible(false);

        jr_allEmp.setSelected(true);

        loadOvertimeRequests();

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
    
    private void initializeOvertimeTable() {

        DefaultTableModel model = new DefaultTableModel(
                new Object[][]{},
                new String[]{
                    "Overtime ID",
                    "Employee ID",
                    "Employee Name",
                    "Overtime Date",
                    "Start Time",
                    "End Time",
                    "Total Hours",
                    "Reason",
                    "Status"
                }
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tbl_overtimeMonitoring.setModel(model);

        tbl_overtimeMonitoring.setSelectionMode(
                javax.swing.ListSelectionModel.SINGLE_SELECTION
        );
    }
    
    private void loadOvertimeRequests() {

        try {

            DefaultTableModel model =
                    (DefaultTableModel) tbl_overtimeMonitoring.getModel();

            model.setRowCount(0);

            List<String[]> overtimeRecords = CsvHandler.readOvertime();
            List<String[]> employees = CsvHandler.readEmployees();

            // Map employee ID to employee name.
            Map<String, String> employeeNames = new HashMap<>();

            for (String[] employee : employees) {

                if (employee.length >= 3) {

                    String employeeID = employee[0].trim();
                    String lastName = employee[1].trim();
                    String firstName = employee[2].trim();

                    employeeNames.put(
                            employeeID,
                            lastName + ", " + firstName
                    );
                }
            }

            // Determine whether a specific employee was selected.
            String selectedEmployeeID = null;

            if (jr_selectedEmp.isSelected()) {

                Object selectedItem = jc_employee.getSelectedItem();

                if (selectedItem == null) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Please select an employee.",
                            "Missing Employee",
                            JOptionPane.WARNING_MESSAGE
                    );

                    return;
                }

                selectedEmployeeID = selectedItem.toString()
                        .split(" - ", 2)[0].trim();
            }

            // Read and display overtime records.
            for (String[] overtime : overtimeRecords) {

                if (overtime.length < 8) {
                    continue;
                }

                String overtimeID = overtime[0].trim();
                String employeeID = overtime[1].trim();
                String overtimeDate = overtime[2].trim();
                String startTime = overtime[3].trim();
                String endTime = overtime[4].trim();
                String totalHours = overtime[5].trim();
                String reason = overtime[6].trim();
                String status = overtime[7].trim();

                // Filter by selected employee.
                if (selectedEmployeeID != null
                        && !employeeID.equalsIgnoreCase(selectedEmployeeID)) {
                    continue;
                }

                String employeeName = employeeNames.getOrDefault(
                        employeeID,
                        "Employee Not Found"
                );

                model.addRow(new Object[]{
                    overtimeID,
                    employeeID,
                    employeeName,
                    overtimeDate,
                    startTime,
                    endTime,
                    totalHours,
                    reason,
                    status
                });
            }

            if (model.getRowCount() == 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "No overtime requests found for the selected employee filter.",
                        "No Records",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }

        } catch (IOException ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to load overtime requests.\n" + ex.getMessage(),
                    "CSV Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
    
    private void updateOvertimeStatus(String newStatus) {

        int selectedRow = tbl_overtimeMonitoring.getSelectedRow();

        if (selectedRow == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select an overtime request from the table.",
                    "No Request Selected",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int modelRow =
                tbl_overtimeMonitoring.convertRowIndexToModel(selectedRow);

        String overtimeID = tbl_overtimeMonitoring
                .getModel().getValueAt(modelRow, 0).toString();

        String employeeID = tbl_overtimeMonitoring
                .getModel().getValueAt(modelRow, 1).toString();

        String employeeName = tbl_overtimeMonitoring
                .getModel().getValueAt(modelRow, 2).toString();

        String currentStatus = tbl_overtimeMonitoring
                .getModel().getValueAt(modelRow, 8).toString();

        if (!currentStatus.equalsIgnoreCase("PENDING")) {

            JOptionPane.showMessageDialog(
                    this,
                    "This overtime request has already been "
                    + currentStatus + ".",
                    "Request Already Processed",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int confirmation = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to "
                + newStatus.toLowerCase()
                + " this overtime request?\n\n"
                + "Overtime ID: " + overtimeID + "\n"
                + "Employee ID: " + employeeID + "\n"
                + "Employee Name: " + employeeName + "\n"
                + "New Status: " + newStatus,
                "Confirm Overtime Decision",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (confirmation != JOptionPane.YES_OPTION) {
            return;
        }

        try {

            List<String[]> overtimeRecords = CsvHandler.readOvertime();

            boolean updated = false;

            for (String[] overtime : overtimeRecords) {

                if (overtime.length >= 8
                        && overtime[0].trim().equalsIgnoreCase(overtimeID)) {

                    // Check the latest status from the CSV.
                    if (!overtime[7].trim().equalsIgnoreCase("PENDING")) {

                        JOptionPane.showMessageDialog(
                                this,
                                "This request has already been processed.",
                                "Request Already Processed",
                                JOptionPane.WARNING_MESSAGE
                        );

                        loadOvertimeRequests();
                        return;
                    }

                    String[] updatedOvertime = Arrays.copyOf(overtime, 8);

                    updatedOvertime[7] = newStatus;

                    updated = CsvHandler.updateOvertime(
                            overtimeID,
                            updatedOvertime
                    );

                    break;
                }
            }

            if (updated) {

                JOptionPane.showMessageDialog(
                        this,
                        "Overtime request " + overtimeID
                        + " has been " + newStatus.toLowerCase() + ".",
                        "Update Successful",
                        JOptionPane.INFORMATION_MESSAGE
                );

                loadOvertimeRequests();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Unable to update the overtime request.",
                        "Update Failed",
                        JOptionPane.ERROR_MESSAGE
                );
            }

        } catch (IOException ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error updating overtime.csv:\n" + ex.getMessage(),
                    "CSV Error",
                    JOptionPane.ERROR_MESSAGE
            );
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

        bg_employeeSelection = new javax.swing.ButtonGroup();
        text_id = new javax.swing.JTextField();
        text_name = new javax.swing.JTextField();
        text_position = new javax.swing.JTextField();
        jb_close = new javax.swing.JButton();
        jb_empDashSignout = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jb_cancel = new javax.swing.JButton();
        jb_load = new javax.swing.JButton();
        jr_allEmp = new javax.swing.JRadioButton();
        jr_selectedEmp = new javax.swing.JRadioButton();
        jc_employee = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_overtimeMonitoring = new javax.swing.JTable();
        jb_approved = new javax.swing.JButton();
        jb_reject = new javax.swing.JButton();
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

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "OVERTIME APPROVAL FORM", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Georgia", 1, 14), new java.awt.Color(0, 0, 204))); // NOI18N
        jPanel1.setOpaque(false);

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

        jb_load.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jb_load.setText("LOAD EMPLOYEE");
        jb_load.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jb_loadActionPerformed(evt);
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

        tbl_overtimeMonitoring.setModel(new javax.swing.table.DefaultTableModel(
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
    jScrollPane1.setViewportView(tbl_overtimeMonitoring);

    jb_approved.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    jb_approved.setText("APPROVED");
    jb_approved.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jb_approvedActionPerformed(evt);
        }
    });

    jb_reject.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    jb_reject.setText("REJECT");
    jb_reject.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jb_rejectActionPerformed(evt);
        }
    });

    javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
    jPanel1.setLayout(jPanel1Layout);
    jPanel1Layout.setHorizontalGroup(
        jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel1Layout.createSequentialGroup()
            .addGap(17, 17, 17)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 583, Short.MAX_VALUE)
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
                        .addComponent(jb_cancel, javax.swing.GroupLayout.DEFAULT_SIZE, 168, Short.MAX_VALUE)
                        .addComponent(jb_load, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
            .addGap(20, 20, 20))
        .addGroup(jPanel1Layout.createSequentialGroup()
            .addGap(23, 23, 23)
            .addComponent(jb_approved, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(jb_reject, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );
    jPanel1Layout.setVerticalGroup(
        jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel1Layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jLabel3)
            .addGap(18, 18, 18)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jr_allEmp)
                .addComponent(jb_load))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jr_selectedEmp)
                .addComponent(jc_employee, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jb_cancel))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 253, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jb_approved)
                .addComponent(jb_reject))
            .addContainerGap(28, Short.MAX_VALUE))
    );

    getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 144, 630, 440));

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

        jr_allEmp.setSelected(true);

        jc_employee.setVisible(false);

        if (jc_employee.getItemCount() > 0) {
            jc_employee.setSelectedIndex(0);
        }

        loadOvertimeRequests();
        
    }//GEN-LAST:event_jb_cancelActionPerformed

    private void jb_loadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jb_loadActionPerformed

   loadOvertimeRequests();
        
    }//GEN-LAST:event_jb_loadActionPerformed

    private void jr_allEmpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jr_allEmpActionPerformed
       
        jc_employee.setVisible(false);
                
    }//GEN-LAST:event_jr_allEmpActionPerformed

    private void jr_selectedEmpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jr_selectedEmpActionPerformed
      
        jc_employee.setVisible(true);
        
    }//GEN-LAST:event_jr_selectedEmpActionPerformed

    private void jb_approvedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jb_approvedActionPerformed
        
        updateOvertimeStatus("APPROVED");
        
    }//GEN-LAST:event_jb_approvedActionPerformed

    private void jb_rejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jb_rejectActionPerformed
        
        updateOvertimeStatus("REJECTED");
        
    }//GEN-LAST:event_jb_rejectActionPerformed

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
            java.util.logging.Logger.getLogger(GUIMngrOvertimeApproval.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GUIMngrOvertimeApproval.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GUIMngrOvertimeApproval.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GUIMngrOvertimeApproval.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
      
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GUIMngrOvertimeApproval().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel background;
    private javax.swing.ButtonGroup bg_employeeSelection;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton jb_approved;
    private javax.swing.JButton jb_cancel;
    private javax.swing.JButton jb_close;
    private javax.swing.JButton jb_empDashSignout;
    private javax.swing.JButton jb_load;
    private javax.swing.JButton jb_reject;
    private javax.swing.JComboBox<String> jc_employee;
    private javax.swing.JRadioButton jr_allEmp;
    private javax.swing.JRadioButton jr_selectedEmp;
    private javax.swing.JTable tbl_overtimeMonitoring;
    private javax.swing.JTextField text_id;
    private javax.swing.JTextField text_name;
    private javax.swing.JTextField text_position;
    // End of variables declaration//GEN-END:variables
}
