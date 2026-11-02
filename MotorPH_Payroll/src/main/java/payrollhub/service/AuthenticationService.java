/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package payrollhub.service;

import java.io.IOException;
import java.util.List;
import javax.swing.JOptionPane;
import payrollhub.data.CsvHandler;
import payrollhub.data.UserData;
import payrollhub.gui.GUIEmpDashboard;
import payrollhub.gui.GUILogin;

/**
 *
 * @author Jae
 */
public class AuthenticationService {

    public AuthenticationService(CsvHandler csvHandler) {
    }
    

    
    public UserData authenticate(
            String userName,
            String password,
            String role) throws IOException {

        List<String[]> users = CsvHandler.readUsers();

        System.out.println("CSV path: "
                + CsvHandler.USERS_FILE.toAbsolutePath());

        System.out.println("Rows loaded: " + users.size());
        System.out.println("Entered username: [" + userName + "]");
        System.out.println("Entered role: [" + role + "]");

        for (String[] user : users) {

            if (user == null || user.length < 4) {
                System.out.println("Skipping invalid CSV row.");
                continue;
            }

            String storedEmpNo = user[0].trim();
            String storedUsername = user[1].trim();
            String storedPassword = user[2].trim();
            String storedRole = user[3].trim();

            System.out.println(
                "CSV username: [" + storedUsername + "]"
                + " | CSV role: [" + storedRole + "]"
            );

            boolean usernameMatches =
                    userName.trim().equals(storedUsername);

            boolean passwordMatches =
                    password.equals(storedPassword);

            boolean roleMatches =
                    role.trim().equals(storedRole);

            System.out.println("Username matches: "
                    + usernameMatches);
            System.out.println("Password matches: "
                    + passwordMatches);
            System.out.println("Role matches: "
                    + roleMatches);

            if (usernameMatches
                    && passwordMatches
                    && roleMatches) {

                System.out.println("LOGIN SUCCESSFUL");

                return new UserData(
                    storedEmpNo,
                    storedUsername,
                    storedPassword,
                    storedRole
                );
            }
        }

        System.out.println("LOGIN FAILED: No matching user.");
        return null;
    }
    
    

}
