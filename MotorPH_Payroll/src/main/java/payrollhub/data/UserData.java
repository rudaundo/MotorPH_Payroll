/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package payrollhub.data;

/**
 *
 * @author Jae
 */
public class UserData {
    
    public String empNo;
    public String userName;
    public String password;
    public String role;
    
    public UserData(String empNo, String userName, String password, String role){
        this.empNo = empNo;
        this.userName = userName;
        this.password = password;
        this.role = role;       
        
    }
    
    public String getempNo(){
        return empNo;
    }
    
    public String getuserName(){
        return userName;
    }
    
    public String getpassword(){
        return password;
    }
    
    public String getrole(){
        return role;
    }
    
    
}
