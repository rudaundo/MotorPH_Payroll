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
import java.util.ArrayList;
import java.util.List;
import payrollhub.data.CsvHandler;

public class LeaveService {

    // Read all leave requests.
    public List<String[]> getAllRequests()
            throws IOException {

        return CsvHandler.readLeaves();
    }

    // Return requests belonging to a particular employee.
    public List<String[]> getEmployeeRequests(
            String employeeID
    ) throws IOException {

        List<String[]> results = new ArrayList<>();

        for (String[] row : getAllRequests()) {

            if (row.length >= 7
                    && row[1].trim().equals(employeeID.trim())) {

                results.add(row);
            }
        }

        return results;
    }

    // Submit a new leave request.
    public String submitRequest(
            String employeeID,
            String type,
            LocalDate startDate,
            LocalDate endDate,
            String reason
    ) throws IOException {

        // Validate employee ID.
        if (employeeID == null || employeeID.isBlank()) {
            throw new IllegalArgumentException(
                    "Employee ID is required."
            );
        }

        // Validate dates.
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException(
                    "Start date and end date are required."
            );
        }

        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException(
                    "End date cannot be before start date."
            );
        }

        // Validate leave type.
        if (type == null || type.isBlank()) {
            throw new IllegalArgumentException(
                    "Leave type is required."
            );
        }

        // Validate reason.
        if (reason == null || reason.isBlank()) {
            throw new IllegalArgumentException(
                    "Please provide a reason for your leave."
            );
        }

        // Check overlapping pending or approved requests.
        for (String[] row : getAllRequests()) {

            if (row.length < 7) {
                continue;
            }

            String existingEmployeeID = row[1].trim();
            String existingStatus = row[5].trim();

            if (!existingEmployeeID.equals(employeeID.trim())) {
                continue;
            }

            if (!existingStatus.equalsIgnoreCase("PENDING")
                    && !existingStatus.equalsIgnoreCase("APPROVED")) {
                continue;
            }

            LocalDate existingStart =
                    LocalDate.parse(row[3].trim());

            LocalDate existingEnd =
                    LocalDate.parse(row[4].trim());

            boolean overlaps =
                    !startDate.isAfter(existingEnd)
                    && !endDate.isBefore(existingStart);

            if (overlaps) {
                throw new IllegalArgumentException(
                        "Your requested dates overlap an existing "
                        + existingStatus.toLowerCase()
                        + " leave request."
                );
            }
        }

        // Generate the next leave ID.
        String leaveID = generateLeaveID();

        // Prepare the leave record.
        String[] newRequest = {
            leaveID,
            employeeID.trim(),
            type.trim(),
            startDate.toString(),
            endDate.toString(),
            "PENDING",
            reason.trim()
        };

        // Save through CsvHandler.
        CsvHandler.appendLeave(newRequest);

        return leaveID;
    }

    // Generate the next leave ID, such as L0016.
    private String generateLeaveID() throws IOException {

        int highestID = 0;

        for (String[] row : getAllRequests()) {

            if (row.length == 0) {
                continue;
            }

            String id = row[0].trim();

            if (id.matches("L\\d+")) {

                int number = Integer.parseInt(
                        id.substring(1)
                );

                if (number > highestID) {
                    highestID = number;
                }
            }
        }

        return String.format("L%04d", highestID + 1);
    }
}