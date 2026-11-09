/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package payrollhub.data;

/**
 *
 * @author Jae
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import payrollhub.service.AuthenticationService;

public final class CsvHandler {

    // Folder containing the CSV files
    public static final Path DATA_DIR = Paths.get("src\\main\\java\\data");

    // CSV file paths
    public static final Path EMPLOYEE_FILE =
            DATA_DIR.resolve("employee.csv");

    public static final Path USERS_FILE =
            DATA_DIR.resolve("users.csv");

    public static final Path ATTENDANCE_FILE =
            DATA_DIR.resolve("attendance.csv");
    
    public static final Path LEAVE_FILE =
            DATA_DIR.resolve("leaves.csv");
    
    public static final Path OVERTIME_FILE =
        DATA_DIR.resolve("overtime.csv");
    
    public static final Path PAYROLL_FILE =
        DATA_DIR.resolve("payroll.csv");

    // CSV headers
    private static final String[] EMPLOYEE_HEADER = {
        "empNo",
        "lastName",
        "firstName",
        "birthday",
        "address",
        "phone",
        "sss",
        "philhealth",
        "tin",
        "pagibig",
        "status",
        "position",
        "supervisor",
        "basicSalary",
        "riceSubsidy",
        "phoneAllowance",
        "clothingAllowance"
    };

    private static final String[] USERS_HEADER = {
        "empNo",
        "userName",
        "password",
        "role"
    };

    private static final String[] ATTENDANCE_HEADER = {
        "empNo",
        "date",
        "logIn",
        "logOut"
    };
    
    private static final String[] LEAVE_HEADER = {
        "leaveID",
        "employeeID",
        "type",
        "startDate",
        "endDate",
        "status",
        "reason"
    };
    
    private static final String[] OVERTIME_HEADER = {
        "overtimeID",
        "employeeID",
        "overtimeDate",
        "startTime",
        "endTime",
        "totalHours",
        "reason",
        "status"
    };
    
    private static final String[] PAYROLL_HEADER = {
        "empNo",
        "employeeName",
        "position",
        "periodStart",
        "periodEnd",
        "monthlySalary",
        "dailyRate",
        "daysWorked",
        "overtimePay",
        "grossIncome",
        "riceSubsidy",
        "phoneAllowance",
        "clothingAllowance",
        "totalBenefits",
        "sss",
        "philhealth",
        "pagibig",
        "withholdingTax",
        "otherDeductions",
        "totalDeductions",
        "takeHomePay"
    };

    // Prevent creating objects of this class
    public CsvHandler() {
    }
    


    // =====================================================
    // INITIALIZATION
    // =====================================================

    /**
     * Creates the data directory and initializes CSV files
     * with their headers if they do not exist.
     */
    public static void initialize() throws IOException {

        Files.createDirectories(DATA_DIR);

        initializeFile(EMPLOYEE_FILE, EMPLOYEE_HEADER);
        initializeFile(USERS_FILE, USERS_HEADER);
        initializeFile(ATTENDANCE_FILE, ATTENDANCE_HEADER);
        initializeFile(LEAVE_FILE, LEAVE_HEADER);
        initializeFile(OVERTIME_FILE, OVERTIME_HEADER);
        initializeFile(PAYROLL_FILE, PAYROLL_HEADER);
    }

    private static void initializeFile(
            Path file,
            String[] header
    ) throws IOException {

        if (!Files.exists(file) || Files.size(file) == 0) {

            writeAll(file, header, new ArrayList<>());
        }
    }
    
    

    // =====================================================
    // EMPLOYEE OPERATIONS
    // =====================================================

    /**
     * Reads all employee records.
     */
    public static List<String[]> readEmployees()
            throws IOException {

        return readAll(EMPLOYEE_FILE);
    }

    /**
     * Replaces all employee records.
     */
    public static void writeEmployees(
            List<String[]> employees
    ) throws IOException {

        writeAll(
                EMPLOYEE_FILE,
                EMPLOYEE_HEADER,
                employees
        );
    }

    /**
     * Adds a new employee record.
     */
    public static void appendEmployee(
            String[] employee
    ) throws IOException {

        appendRecord(
                EMPLOYEE_FILE,
                EMPLOYEE_HEADER,
                employee
        );
    }

    /**
     * Updates an employee using empNo as the identifier.
     */
    public static boolean updateEmployee(
            String empNo,
            String[] updatedEmployee
    ) throws IOException {

        return updateRecord(
                EMPLOYEE_FILE,
                EMPLOYEE_HEADER,
                empNo,
                updatedEmployee
        );
    }

    // =====================================================
    // USER OPERATIONS
    // =====================================================

    /**
     * Reads all user records.
     */
    public static List<String[]> readUsers()
            throws IOException {

        return readAll(USERS_FILE);
    }

    /**
     * Replaces all user records.
     */
    public static void writeUsers(
            List<String[]> users
    ) throws IOException {

        writeAll(
                USERS_FILE,
                USERS_HEADER,
                users
        );
    }

    /**
     * Adds a new user record.
     */
    public static void appendUser(
            String[] user
    ) throws IOException {

        appendRecord(
                USERS_FILE,
                USERS_HEADER,
                user
        );
    }

    /**
     * Updates a user using empNo as the identifier.
     */
    public static boolean updateUser(
            String empNo,
            String[] updatedUser
    ) throws IOException {

        return updateRecord(
                USERS_FILE,
                USERS_HEADER,
                empNo,
                updatedUser
        );
    }

    // =====================================================
    // ATTENDANCE OPERATIONS
    // =====================================================

    /**
     * Reads all attendance records.
     */
    public static List<String[]> readAttendance()
            throws IOException {

        return readAll(ATTENDANCE_FILE);
    }

    /**
     * Replaces all attendance records.
     */
    public static void writeAttendance(
            List<String[]> attendance
    ) throws IOException {

        writeAll(
                ATTENDANCE_FILE,
                ATTENDANCE_HEADER,
                attendance
        );
    }

    /**
     * Adds an attendance record.
     */
    public static void appendAttendance(
            String[] attendance
    ) throws IOException {

        appendRecord(
                ATTENDANCE_FILE,
                ATTENDANCE_HEADER,
                attendance
        );
    }
    
    // =====================================================
    // LEAVE OPERATIONS
    // =====================================================

    public static List<String[]> readLeaves() throws IOException {

        return readAll(LEAVE_FILE);
    }

    public static void writeLeaves(
            List<String[]> leaves
    ) throws IOException {

        writeAll(
                LEAVE_FILE,
                LEAVE_HEADER,
                leaves
        );
    }

    public static void appendLeave(
            String[] leave
    ) throws IOException {

        appendRecord(
                LEAVE_FILE,
                LEAVE_HEADER,
                leave
        );
    }

    public static boolean updateLeave(
            String leaveID,
            String[] updatedLeave
    ) throws IOException {

        return updateRecord(
                LEAVE_FILE,
                LEAVE_HEADER,
                leaveID,
                updatedLeave
        );
    }
    
    // =====================================================
    // OVERTIME OPERATIONS
    // =====================================================

    public static List<String[]> readOvertime() throws IOException {
        return readAll(OVERTIME_FILE);
    }

    public static void appendOvertime(String[] overtime) throws IOException {
        appendRecord(
                OVERTIME_FILE,
                OVERTIME_HEADER,
                overtime
        );
    }
    
    public static boolean updateOvertime(
            String overtimeID,
            String[] updatedOvertime
    ) throws IOException {

        return updateRecord(
                OVERTIME_FILE,
                OVERTIME_HEADER,
                overtimeID,
                updatedOvertime
        );
    }
    
    // =====================================================
    // PAYROLL OPERATIONS
    // =====================================================

    /**
     * Reads all payroll records.
     */
    public static List<String[]> readPayroll() throws IOException {

        return readAll(PAYROLL_FILE);
    }

    /**
     * Adds a completed payroll record.
     */
    public static void appendPayroll(String[] payroll) throws IOException {

        appendRecord(
                PAYROLL_FILE,
                PAYROLL_HEADER,
                payroll
        );
    }

    // =====================================================
    // GENERIC CSV READING
    // =====================================================

    /**
     * Reads CSV records, excluding the first line (header).
     *
     * Each record is returned as a String array.
     */
    public static List<String[]> readAll(
            Path file
    ) throws IOException {

        List<String[]> records = new ArrayList<>();

        if (!Files.exists(file) || Files.size(file) == 0) {
            return records;
        }

        try (BufferedReader reader = Files.newBufferedReader(
                file,
                StandardCharsets.UTF_8
        )) {

            String line;
            boolean firstLine = true;

            while ((line = reader.readLine()) != null) {

                if (firstLine) {
                    firstLine = false;
                    continue;
                }

                if (line.trim().isEmpty()) {
                    continue;
                }

                records.add(parseLine(line));
            }
        }

        return records;
    }

    // =====================================================
    // GENERIC CSV WRITING
    // =====================================================

    /**
     * Rewrites a CSV file with its header and all records.
     */
    public static void writeAll(
            Path file,
            String[] header,
            List<String[]> records
    ) throws IOException {

        Objects.requireNonNull(file, "File cannot be null");
        Objects.requireNonNull(header, "Header cannot be null");
        Objects.requireNonNull(records, "Records cannot be null");

        Path parent = file.getParent();

        if (parent != null) {
            Files.createDirectories(parent);
        }

        try (BufferedWriter writer = Files.newBufferedWriter(
                file,
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING,
                StandardOpenOption.WRITE
        )) {

            writer.write(toCsvLine(header));
            writer.newLine();

            for (String[] record : records) {

                writer.write(toCsvLine(record));
                writer.newLine();
            }
        }
    }

    // =====================================================
    // GENERIC CSV APPENDING
    // =====================================================

    /**
     * Appends one record to a CSV file.
     * Creates the file and header if needed.
     */
    public static void appendRecord(
            Path file,
            String[] header,
            String[] record
    ) throws IOException {

        Objects.requireNonNull(record, "Record cannot be null");

        validateRecordLength(header, record);

        Path parent = file.getParent();

        if (parent != null) {
            Files.createDirectories(parent);
        }

        boolean needsHeader =
                !Files.exists(file) || Files.size(file) == 0;

        // Check whether the existing file ends with a newline.
        boolean needsNewLine = false;

        if (!needsHeader) {

            try (var input = Files.newInputStream(file)) {

                long size = Files.size(file);

                input.skip(size - 1);

                int lastByte = input.read();

                needsNewLine =
                        lastByte != '\n' && lastByte != '\r';
            }
        }

        try (BufferedWriter writer = Files.newBufferedWriter(
                file,
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND,
                StandardOpenOption.WRITE
        )) {

            if (needsHeader) {
                writer.write(toCsvLine(header));
                writer.newLine();

            } else if (needsNewLine) {
                writer.newLine();
            }

            writer.write(toCsvLine(record));
            writer.newLine();
        }
    }

    // =====================================================
    // GENERIC CSV UPDATING
    // =====================================================

    /**
     * Updates the first record whose first column matches
     * the supplied identifier.
     *
     * Returns true if a record was updated.
     */
    public static boolean updateRecord(
            Path file,
            String[] header,
            String identifier,
            String[] updatedRecord
    ) throws IOException {

        Objects.requireNonNull(
                identifier,
                "Identifier cannot be null"
        );

        validateRecordLength(header, updatedRecord);

        List<String[]> records = readAll(file);

        for (int i = 0; i < records.size(); i++) {

            String[] record = records.get(i);

            if (record.length > 0
                    && record[0].trim().equals(identifier.trim())) {

                records.set(i, updatedRecord);

                writeAll(file, header, records);

                return true;
            }
        }

        return false;
    }

    // =====================================================
    // CSV PARSING
    // =====================================================

    /**
     * Converts a CSV line into a String array.
     *
     * Supports commas and escaped quotation marks
     * inside quoted fields.
     */
    public static String[] parseLine(String line) {

        List<String> fields = new ArrayList<>();
        StringBuilder field = new StringBuilder();

        boolean insideQuotes = false;

        for (int i = 0; i < line.length(); i++) {

            char current = line.charAt(i);

            if (current == '"') {

                if (insideQuotes
                        && i + 1 < line.length()
                        && line.charAt(i + 1) == '"') {

                    field.append('"');
                    i++;

                } else {
                    insideQuotes = !insideQuotes;
                }

            } else if (current == ',' && !insideQuotes) {

                fields.add(field.toString());
                field.setLength(0);

            } else {
                field.append(current);
            }
        }

        fields.add(field.toString());

        return fields.toArray(new String[0]);
    }

    // =====================================================
    // CSV FORMATTING
    // =====================================================

    /**
     * Converts a String array into a CSV-formatted line.
     *
     * Fields containing commas, quotes, or newlines
     * are enclosed in quotation marks.
     */
    public static String toCsvLine(String[] fields) {

        StringBuilder line = new StringBuilder();

        for (int i = 0; i < fields.length; i++) {

            if (i > 0) {
                line.append(',');
            }

            String value = fields[i] == null ? "" : fields[i];

            boolean needsQuotes =
                    value.contains(",")
                    || value.contains("\"")
                    || value.contains("\n")
                    || value.contains("\r");

            if (needsQuotes) {

                line.append('"');

                line.append(
                        value.replace("\"", "\"\"")
                );

                line.append('"');

            } else {
                line.append(value);
            }
        }

        return line.toString();
    }

    // =====================================================
    // VALIDATION
    // =====================================================

    private static void validateRecordLength(
            String[] header,
            String[] record
    ) {

        Objects.requireNonNull(header, "Header cannot be null");
        Objects.requireNonNull(record, "Record cannot be null");

        if (record.length != header.length) {

            throw new IllegalArgumentException(
                    "Expected " + header.length
                    + " columns, but received "
                    + record.length + "."
            );
        }
    }
    
    public static List<String> readLines(Path file) throws IOException {

        if (!Files.exists(file) || Files.size(file) == 0) {

            Path parent = file.toAbsolutePath().getParent();

            if (parent != null) {
                Files.createDirectories(parent);
            }

            Files.write(
                    file,
                    List.of("empNo,date,logIn,logOut"),
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING
            );
        }

        return Files.readAllLines(file, StandardCharsets.UTF_8);
    }
    
    public static void writeLines(Path file, List<String> lines) throws IOException {

        Path parent = file.toAbsolutePath().getParent();

        if (parent != null) {
            Files.createDirectories(parent);
        }

        Files.write(
                file,
                lines,
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING
        );
    }
    
   
}