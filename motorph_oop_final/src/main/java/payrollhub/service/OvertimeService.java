/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package payrollhub.service;

/**
 *
 * @author Jae
 */
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import payrollhub.data.CsvHandler;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class OvertimeService {

    private static final DateTimeFormatter TIME_FORMAT =
            DateTimeFormatter.ofPattern("HH:mm");

    public String submitRequest(
            String employeeID,
            LocalDate overtimeDate,
            LocalTime startTime,
            LocalTime endTime,
            String reason
    ) throws IOException {

        // Validate employee ID.
        if (employeeID == null || employeeID.isBlank()) {
            throw new IllegalArgumentException(
                    "Employee ID is required."
            );
        }

        // Validate date.
        if (overtimeDate == null) {
            throw new IllegalArgumentException(
                    "Please select the overtime date."
            );
        }

        // Validate times.
        if (startTime == null || endTime == null) {
            throw new IllegalArgumentException(
                    "Start time and end time are required."
            );
        }

        if (!endTime.isAfter(startTime)) {
            throw new IllegalArgumentException(
                    "End time must be later than start time."
            );
        }

        // Validate reason.
        if (reason == null || reason.isBlank()) {
            throw new IllegalArgumentException(
                    "Please provide a reason for overtime."
            );
        }
        
        // CHECK FOR DUPLICATE REQUEST
        if (hasExistingRequest(employeeID, overtimeDate)) {
            throw new IllegalArgumentException(
                    "You have already submitted an overtime request for this date."
            );
        }

        // Calculate the duration in hours.
        double totalHours =
                java.time.Duration.between(startTime, endTime)
                        .toMinutes() / 60.0;

        // Generate a unique overtime ID.
        String overtimeID = generateOvertimeID();

        // Prepare the CSV record.
        String[] overtimeRecord = {
            overtimeID,
            employeeID.trim(),
            overtimeDate.toString(),
            startTime.format(TIME_FORMAT),
            endTime.format(TIME_FORMAT),
            String.format(java.util.Locale.ROOT, "%.2f", totalHours),
            reason.trim(),
            "PENDING"
        };

        // Save the request.
        CsvHandler.appendOvertime(overtimeRecord);

        return overtimeID;
    }

    private String generateOvertimeID() throws IOException {

        List<String[]> requests = CsvHandler.readOvertime();

        int highestID = 0;

        for (String[] row : requests) {

            if (row.length == 0) {
                continue;
            }

            String id = row[0].trim();

            if (id.matches("OT\\d+")) {

                int number = Integer.parseInt(id.substring(2));

                if (number > highestID) {
                    highestID = number;
                }
            }
        }

        return String.format("OT%04d", highestID + 1);
    }
    
    private boolean hasExistingRequest(String employeeID, LocalDate overtimeDate)throws IOException {

        List<String[]> records = CsvHandler.readOvertime();

        DateTimeFormatter dateFormatter =
                DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for (String[] row : records) {

            if (row.length >= 3
                    && row[1].trim().equals(employeeID.trim())
                    && LocalDate.parse(row[2].trim(), dateFormatter)
                            .equals(overtimeDate)) {

                return true;
            }
        }

        return false;
    }
    
    public BigDecimal getApprovedOvertimeHours(
        String employeeID,
        LocalDate periodStart,
        LocalDate periodEnd
    ) throws IOException {

        List<String[]> records = CsvHandler.readOvertime();

        BigDecimal totalHours = BigDecimal.ZERO;

        for (String[] row : records) {

            if (row.length < 8) {
                continue;
            }

            String empNo = row[1].trim();
            String dateValue = row[2].trim();
            String hoursValue = row[5].trim();
            String status = row[7].trim();

            // Match employee
            if (!empNo.equals(employeeID.trim())) {
                continue;
            }

            // Only approved overtime
            if (!status.equalsIgnoreCase("APPROVED")) {
                continue;
            }

            LocalDate overtimeDate;

            try {
                overtimeDate = LocalDate.parse(dateValue);
            } catch (java.time.format.DateTimeParseException e) {
                continue;
            }

            // Check payroll period
            if (overtimeDate.isBefore(periodStart)
                    || overtimeDate.isAfter(periodEnd)) {
                continue;
            }

            try {
                BigDecimal hours = new BigDecimal(hoursValue);
                totalHours = totalHours.add(hours);
            } catch (NumberFormatException e) {
                continue;
            }
        }

        return totalHours.setScale(2, RoundingMode.HALF_UP);
    }
    
}