/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package payrollhub.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import payrollhub.data.CsvHandler;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class AttendanceService {
    

    private final Path csvFile;

    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private static final DateTimeFormatter TIME_FORMAT =
            DateTimeFormatter.ofPattern("H:mm");

    public AttendanceService(String csvFilePath) {
        this.csvFile = Path.of(csvFilePath);
    }

        
    //Load attendance Record
    
    public DefaultTableModel loadAttendanceRecords(String employeeID) throws IOException{
        
        String [] columns = {
            "Employee ID",
            "Date",
            "Time In",
            "Time Out" 
        };
        
        DefaultTableModel model = new DefaultTableModel(columns, 0){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
        }
        
        
        };
    
    List<String[]> records = CsvHandler.readAttendance();

            for (int i = records.size() - 1; i >= 0; i--) {

            String[] row = records.get(i);

            if (row.length < 4) {
                continue;
            }

            if (row[0].trim().equals(employeeID.trim())) {
                model.addRow(new Object[]{
                    row[0].trim(),
                    row[1].trim(),
                    row[2].trim(),
                    row[3].trim()
                });
            }
        }

        return model;
    }
    
    public void recordTimeIn(String empNo, LocalDateTime timeIn)throws IOException {

        String date = timeIn.format(DATE_FORMAT);

        // Read existing attendance records from the same CSV file.
        if (Files.exists(csvFile) && Files.size(csvFile) > 0) {

            List<String> lines = Files.readAllLines(
                    csvFile, StandardCharsets.UTF_8);

            for (int i = 1; i < lines.size(); i++) {

                String[] row = lines.get(i).split(",", -1);

                if (row.length < 4) {
                    continue;
                }

                // Check employee ID and today's date.
                if (row[0].trim().equals(empNo.trim())
                        && row[1].trim().equals(date)
                        && !row[2].trim().isEmpty()) {

                    throw new IOException(
                            "You have already recorded Time In today."
                    );
                }
            }
        }

        boolean needsHeader = !Files.exists(csvFile)
                || Files.size(csvFile) == 0;

        boolean needsNewLine = false;

        if (!needsHeader) {
            byte[] content = Files.readAllBytes(csvFile);
            byte lastByte = content[content.length - 1];

            needsNewLine = lastByte != '\n' && lastByte != '\r';
        }

        try (var writer = Files.newBufferedWriter(
                csvFile,
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND)) {

            if (needsHeader) {
                writer.write("empNo,date,logIn,logOut");
                writer.newLine();
            } else if (needsNewLine) {
                writer.newLine();
            }

            writer.write(
                    empNo + ","
                    + date + ","
                    + timeIn.format(TIME_FORMAT) + ","
            );

            writer.newLine();
        }
    }
    
    public void recordTimeOut(String empNo, LocalDateTime timeOut) throws IOException {

        List<String> lines = CsvHandler.readLines(csvFile);

        String date = timeOut.format(DATE_FORMAT);
        String time = timeOut.format(TIME_FORMAT);

        for (int i = 1; i < lines.size(); i++) {

            String[] row = lines.get(i).split(",", -1);

            if (row.length >= 4
                    && row[0].trim().equals(empNo.trim())
                    && row[1].trim().equals(date)) {

                if (row[2].trim().isEmpty()) {
                    throw new IOException(
                            "Please record Time In first."
                    );
                }

                if (!row[3].trim().isEmpty()) {
                    throw new IOException(
                            "Time Out has already been recorded today."
                    );
                }

                row[3] = time;

                lines.set(i, String.join(",", row));

                CsvHandler.writeLines(csvFile, lines);

                return;
            }
        }

        throw new IOException(
                "No Time In record found for this employee today."
        );
    }
    
    public LocalDateTime timeInWithMessage(String empNo) {

        LocalDateTime timeIn = LocalDateTime.now();

        try {
            recordTimeIn(empNo, timeIn);

            JOptionPane.showMessageDialog(
                    null,
                    "Time In recorded successfully!",
                    "Attendance",
                    JOptionPane.INFORMATION_MESSAGE
            );

            return timeIn;

        } catch (IOException e) {

            JOptionPane.showMessageDialog(
                    null,
                    e.getMessage(),
                    "Time In Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return null;
        }
    }


    // Handle Time Out with success and error messages
    public LocalDateTime timeOutWithMessage(String empNo) {

        LocalDateTime timeOut = LocalDateTime.now();

        try {
            recordTimeOut(empNo, timeOut);

            JOptionPane.showMessageDialog(
                    null,
                    "Time Out recorded successfully!",
                    "Attendance",
                    JOptionPane.INFORMATION_MESSAGE
            );

            return timeOut;

        } catch (IOException e) {

            JOptionPane.showMessageDialog(
                    null,
                    e.getMessage(),
                    "Time Out Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return null;
        }
    }
    
    // =====================================================
    // PAYROLL ATTENDANCE
    // =====================================================

    /**
     * Counts completed attendance days within a payroll period.
     */
    public int countDaysWorked(
        String employeeID,
        LocalDate periodStart,
        LocalDate periodEnd
        ) throws IOException {

            if (employeeID == null || periodStart == null || periodEnd == null
                    || periodEnd.isBefore(periodStart)) {

                throw new IllegalArgumentException(
                        "Invalid employee ID or payroll period."
                );
            }

            List<String> lines = Files.readAllLines(
                    csvFile,
                    StandardCharsets.UTF_8
            );

            Set<LocalDate> workedDates = new HashSet<>();

            for (int i = 1; i < lines.size(); i++) {

                String[] row = lines.get(i).split(",", -1);

                if (row.length < 4) {
                    continue;
                }

                String empNo = row[0].trim();
                String dateValue = row[1].trim();
                String logIn = row[2].trim();
                String logOut = row[3].trim();

                if (!empNo.equals(employeeID.trim())) {
                    continue;
                }

                if (logIn.isEmpty() || logOut.isEmpty()) {
                    continue;
                }

                try {

                    LocalDate attendanceDate =
                            LocalDate.parse(dateValue, DATE_FORMAT);

                    if (!attendanceDate.isBefore(periodStart)
                            && !attendanceDate.isAfter(periodEnd)) {

                        workedDates.add(attendanceDate);
                    }

                } catch (java.time.format.DateTimeParseException e) {
                    continue;
                }
            }

            return workedDates.size();
        }
    
}