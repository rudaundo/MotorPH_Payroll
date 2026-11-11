# Payroll Hub

A Java desktop payroll system for **MotorPH**, built with object-oriented programming. It handles employee records, time tracking, leave and overtime requests, and payroll (single and batch) with payslips, tax reports and deduction reports. The screens are written in Java Swing and all data is stored in CSV files.


> **MO-IT110 Object-Oriented Programming · Terminal Assessment**
> Mapúa-Malayan Digital College · Section S2101 · Group 7

---

## Features

- **Role-based login.** One login screen for everyone. Users sign in with a username, password and role, and the role must match the account's registered role. The dashboard that opens depends on that role (Regular, Human Resources, Accounting or Chief). Wrong passwords, unknown usernames and wrong roles are rejected.
- **User registration.** Create a login account for an existing employee. New accounts are saved with the `Regular` role and a hashed password (PBKDF2).
- **Employee management.** Human Resources adds and updates employee records. Employee IDs are generated automatically.
- **Attendance.** Employees record time in and time out and view their own attendance records.
- **Leave and overtime.** Employees file leave and overtime requests. The Chief role approves or rejects them.
- **Payroll.** Accounting generates payroll for one employee or in batch for a selected period. Payroll covers basic pay, overtime pay, SSS, PhilHealth, Pag-IBIG, withholding tax and take-home pay.
- **Payslips and reports.** Employees view a payslip for a processed period. Accounting generates tax and deduction reports.

## User roles and screens

| Role | What they can do | Main screens |
|---|---|---|
| Regular | Time in/out, view attendance, view payslip, file leave and overtime | `GUIEmpDashboard`, `GUIMyAttendance`, `GUIPaySlip`, `GUILeave`, `GUIOvertime` |
| Human Resources | Add and update employee records, plus the Regular features | `GUIHRDashboard`, `GUIHRAddEmployee`, `GUIHRUpdate` |
| Accounting | Single and batch payroll, tax report, deduction report | `GUIAcctgDashboard`, `GUIAcctgPayrollGen`, `GUIAcctgTaxReport`, `GUIAcctgDedReport` |
| Chief | Approve or reject leave and overtime requests | `GUIMngrDashboard`, `GUIMngrLeaveApproval`, `GUIMngrOvertimeApproval` |

`GUILogin` and `GUIRegistration` are used before login.

## System design

The program is split into layers so that a screen never contains payroll rules or file logic.

| Layer | Package | Responsibility |
|---|---|---|
| Screens | `payrollhub.gui` | Swing windows (extend `JFrame`). They collect input and show results only. |
| Business logic | `payrollhub.service` | `AuthenticationService`, `UserRegistrationService`, `EmployeeService`, `AttendanceService`, `AttendanceHandler`, `LeaveService`, `OvertimeService`, `PayrollService`, `DeductionCalculator`, `LogoutHandler` |
| Data | `payrollhub.data` | `CsvHandler` (the only class that reads and writes the CSV files), `EmployeeData`, `UserData` |
| Result objects | `payrollhub.pojo` | Read-only `Payroll`, `TaxReport` and `DeductionReport` |
| Domain model | `payrollhub.model` | `Employee`, role classes, `Attendance`, `LeaveRequest`, `PayrollBreakdown`, `Position`, `ITTicket` |
| Contracts | `contracts` | Role interfaces (`UserHub`, `AdminHub`, `HRHub`, `FinanceHub`, `SystemSupportHub`, `LeaveHub`, `PayrollProcessor`) |
| Utilities | `util` | `PayrollCalculator` (basic pay, overtime pay, withholding tax), `PasswordUtil` (PBKDF2), `startClock`, `currentDate`, `currentTime` |

### OOP concepts used

| Concept | How it is applied |
|---|---|
| **Encapsulation** | `EmployeeData` keeps its fields private and validates input in its setters. `Payroll`, `TaxReport`, `DeductionReport` and `UserData` are read-only (getters only). |
| **Abstraction** | Screens call service methods and never touch files; `CsvHandler` hides the CSV format from the rest of the program. |
| **Inheritance** | Every screen extends `JFrame`. |
| **Polymorphism** | Overloading: `getTaxReports()` and `getDeductionReports()` work for all employees or one employee. Overriding: `EmployeeData.toString()`. |

### How a screen talks to the backend

For example, the Login button in `GUILogin` calls `AuthenticationService.authenticate()` and opens the dashboard that matches the role. `GUIAcctgPayrollGen` calls `PayrollService.processBatchPayroll()` or `processPayroll()`. `GUIPaySlip`, `GUIAcctgTaxReport` and `GUIAcctgDedReport` call `getPayslip()`, `getTaxReports()` and `getDeductionReports()`. The services read and write data through `CsvHandler`.

## Project structure

```
MotorPH_Payroll/
├── pom.xml
├── README.md
└── src/main/java/
    ├── com/mycompany/motorph_oop_final/
    │   └── Motorph_oop_final.java      # Application entry point; sets up the CSV files and opens the login screen
    ├── contracts/                      # Interfaces
    │   ├── UserHub.java
    │   ├── AdminHub.java
    │   ├── HRHub.java
    │   ├── FinanceHub.java
    │   ├── SystemSupportHub.java
    │   ├── LeaveHub.java
    │   └── PayrollProcessor.java
    ├── data/                           # CSV data files
    │   ├── employee.csv
    │   ├── users.csv
    │   ├── attendance.csv
    │   ├── leaves.csv
    │   ├── overtime.csv
    │   └── payroll.csv
    ├── docs/
    │   └── data-dictionary.md          # Description of the CSV fields
    ├── images/                         # Backgrounds and icons used by the screens
    │   ├── bgmain.png
    │   ├── bluebg.png
    │   ├── face21.jpg
    │   ├── login.jpg
    │   ├── male-face.jpg
    │   └── pbutton.png
    ├── payrollhub/
    │   ├── data/                       # File layer
    │   │   ├── CsvHandler.java
    │   │   ├── EmployeeData.java
    │   │   └── UserData.java
    │   ├── gui/                        # Swing screens
    │   │   ├── GUILogin.java
    │   │   ├── GUIRegistration.java
    │   │   ├── GUIEmpDashboard.java
    │   │   ├── GUIMyAttendance.java
    │   │   ├── GUILeave.java
    │   │   ├── GUIOvertime.java
    │   │   ├── GUIPaySlip.java
    │   │   ├── GUIHRDashboard.java
    │   │   ├── GUIHRAddEmployee.java
    │   │   ├── GUIHRUpdate.java
    │   │   ├── GUIAcctgDashboard.java
    │   │   ├── GUIAcctgPayrollGen.java
    │   │   ├── GUIAcctgTaxReport.java
    │   │   ├── GUIAcctgDedReport.java
    │   │   ├── GUIMngrDashboard.java
    │   │   ├── GUIMngrLeaveApproval.java
    │   │   └── GUIMngrOvertimeApproval.java
    │   ├── model/                      # Employee hierarchy and domain objects
    │   │   ├── Employee.java
    │   │   ├── Admin.java
    │   │   ├── HumanResources.java
    │   │   ├── Accounting.java
    │   │   ├── InfoTech.java
    │   │   ├── Regular.java
    │   │   ├── Position.java
    │   │   ├── LeaveRequest.java
    │   │   ├── Attendance.java
    │   │   ├── PayrollBreakdown.java
    │   │   └── ITTicket.java
    │   ├── pojo/                       # Read-only result objects
    │   │   ├── Payroll.java
    │   │   ├── TaxReport.java
    │   │   └── DeductionReport.java
    │   └── service/                    # Business logic
    │       ├── AuthenticationService.java
    │       ├── UserRegistrationService.java
    │       ├── EmployeeService.java
    │       ├── AttendanceService.java
    │       ├── AttendanceHandler.java
    │       ├── LeaveService.java
    │       ├── OvertimeService.java
    │       ├── PayrollService.java
    │       ├── DeductionCalculator.java
    │       └── LogoutHandler.java
    ├── test/java/                      # Test programs (run with Run File in NetBeans)
    │   ├── CalculatorTest.java
    │   ├── DedCalcTest.java
    │   ├── OvertimeTest.java
    │   ├── PaySlipTest.java
    │   └── PayrollTest.java
    └── util/                           # Helpers
        ├── PayrollCalculator.java
        ├── PasswordUtil.java
        ├── currentDate.java
        ├── currentTime.java
        └── startClock.java
```

## Data files

All reading and writing goes through `CsvHandler`, so no screen parses CSV on its own. The files are in `src/main/java/data/` and are read using a relative path, so the program must be started from the project folder. See `docs/data-dictionary.md` for the fields of each file.

| File | Content |
|---|---|
| `employee.csv` | Employee records |
| `users.csv` | Login accounts and roles |
| `attendance.csv` | Time-in and time-out records |
| `leaves.csv` | Leave requests and their status |
| `overtime.csv` | Overtime requests and their status |
| `payroll.csv` | Generated payroll records used by payslips and reports |

## Getting started

### Prerequisites

- JDK 17 or later
- Apache NetBeans 22 or later
- Maven available to NetBeans (its bundled Maven installation is fine)

### Run in Apache NetBeans

1. Open the project folder that contains `pom.xml` as a Maven project.
2. Make sure the project uses JDK 17 or later.
3. Select **Run Project**. The main class is `com.mycompany.motorph_oop_final.Motorph_oop_final`, which prepares the CSV files and opens the login screen.

Run the project from NetBeans (project folder as the working directory) so that the relative data path resolves.

### Run the tests

The test classes in the `test.java` package (`CalculatorTest`, `DedCalcTest`, `OvertimeTest`, `PaySlipTest`, `PayrollTest`) can be run from NetBeans with **Run File**.

## Design decisions

- **CSV instead of a database.** The source data are spreadsheets and CSV files, and a database would add setup without serving the OOP goals.
- **Payroll rules stay out of the screens.** SSS, PhilHealth and Pag-IBIG are in `DeductionCalculator`, and basic pay, overtime and withholding tax are in `PayrollCalculator`, so a UI change can never alter a computation.
- **`BigDecimal` for money.** All money values use `BigDecimal` to avoid rounding errors.
- **Hashed passwords.** New accounts are stored with PBKDF2 (`PasswordUtil`).
- 
## Known limitations

- The Milestone 1 role classes and the classes in `contracts` are placeholders; role access is handled by the role check in `GUILogin` and by the service classes.
- The leave and overtime approval screens call `CsvHandler` directly instead of going through a service class.
- The sample accounts that were already in `users.csv` still use plain-text passwords; only newly registered accounts are hashed.
- `AuthenticationService` prints login details to the console for debugging.

## Testing

Internal (smoke and unit tests) and external QA results are summarized in the final documentation (Section 4: Testing & Refinement Summary).

Time in/out, employee and user registration, overtime and leave filing, and payroll generation **write to the CSV files** in `src/main/java/data/`. Make a backup copy of that folder before testing so the original dataset can be restored.

## Team

**Group 7, Section S2101**

- Russell Jae Udaundo
- Kristel Austria
- Isabelle Angeli Gallardo

## Academic notice

This project was created for MO-IT110 at Mapúa-Malayan Digital College. The design templates remain the property of the college and are used for educational purposes only.
