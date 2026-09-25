package department;

import admin.Admin;
import exceptions.*;
import security.PasswordUtil;

import java.util.ArrayList;

public class Department {
    private PasswordUtil masterPassword; 
    private ArrayList<Admin> allAdmins;

    public Department(String masterPasswordRaw) throws InvalidPasswordFormatException {
        this.masterPassword = new PasswordUtil(masterPasswordRaw);
        this.allAdmins = new ArrayList<>();
    }

    public boolean verifyMasterPassword(String inputPassword) {
        return this.masterPassword.toVerify(inputPassword);
    }

    public void registerAdmin(String username, String password) throws InvalidPasswordFormatException {
        Admin newAdmin = new Admin(username, password);
        this.allAdmins.add(newAdmin);
    }

    public ArrayList<Admin> getAllAdmins() {
        return this.allAdmins;
    }

    public void setAllAdmins(ArrayList<Admin> admins) {
    this.allAdmins = admins;
}
}

