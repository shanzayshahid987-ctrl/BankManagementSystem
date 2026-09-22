package customer;

import java.util.ArrayList;
import account.Account;
import security.PasswordUtil;
import exceptions.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.Period;
import beneficiary.*;
import java.time.format.DateTimeParseException;

public class Customer  implements Serializable {
    private static final long serialVersionUID = 1L;
    private ArrayList<Account> accounts;
    private String fullName;
    private String username;
    private String dob;
    private PasswordUtil password;
    private String email;
    private int age;
    private String cnicNumber;
    private String contactNumber;
    private String homeAddress;
    private ArrayList<Beneficiary> totalBeneficiary;

    public Customer(String name, String dob, String password, String email, String cnic,
            String number, String address, String username) throws InvalidNameException, InvalidDateException,
            InvalidPasswordFormatException, InvalidEmailException,
            InvalidAgeException, InvalidCNICException, InvalidContactNumberException {
        this.accounts = new ArrayList<>();
        this.totalBeneficiary = new ArrayList<>();
        this.username = username;
        if (!(this.isValidFullName(name))) {
            throw new InvalidNameException(
                    "Name must be 2 to 30 character, no special character allowed.");
        }
        this.fullName = name;
        if (!(this.isValidDob(dob))) {
            throw new InvalidDateException("Invalid date.");
        }
        this.dob = dob;
        
         try {
        this.password = new PasswordUtil(password);
    } catch (InvalidPasswordFormatException e) {
        throw new InvalidPasswordFormatException(e.getMessage());
    }
        if (!(this.isValidEmail(email))) {
            throw new InvalidEmailException("Invalid Email entered.");
        }
        this.email = email;
        if (this.getAge() < 18) {
            throw new InvalidAgeException("Age must be greater or equal to 18");
        }
        this.age = this.getAge();
        if (!(this.isValidCNIC(cnic))) {
            throw new InvalidCNICException("Invalid CNIC entered");
        }
        this.cnicNumber = cnic;
        if (!(this.isValidContactNumber(number))) {
            throw new InvalidContactNumberException("Invalid number entered");
        }
        this.contactNumber = number;
        this.homeAddress = address;

    }

    public String getEmail(){
        return this.email;
    }

    public String getHomeAddress(){
        return this.homeAddress;
    }

    public String getContactNumber(){
        return this.contactNumber;
    }

    public String getUsername() {
        return this.username;

    }

    public String getFullName() {
        return this.fullName;
    }

    public String getCNIC() {
        return this.cnicNumber;
    }

    public ArrayList<Account> getAccounts() {
        return this.accounts;
    }

    public void setContactNumber(String number) throws InvalidContactNumberException {
        if (!(this.isValidContactNumber(number))) {
            throw new InvalidContactNumberException("Invalid number entered");
        }
        this.contactNumber = number;
    }

    public void setEmailAddress(String email) throws InvalidEmailException {
        if (!(this.isValidEmail(email))) {
            throw new InvalidEmailException("Invalid email entered");
        }
        this.email = email;
    }

    public void setHomeAddress(String address) {
        this.homeAddress = address;
    }

    public void changePassword(String old, String newP) throws InvalidPasswordFormatException{
        try{
        if(this.password.equals(old)){
            this.password = new PasswordUtil(newP);
            System.out.println("Passeord updated sucessfully.");
        } else{
            System.out.println("Enter valid password.");
        }
        }catch(InvalidPasswordFormatException e){
            throw new InvalidPasswordFormatException("Incorrect current Password.");
        }
        
    }

    public boolean isValidFullName(String name) {
        String pattern = "^[A-Za-z ]{2,30}$";
        return name != null && name.matches(pattern);
    }

    public boolean isValidDob(String dob) {
        if (dob == null) {
            return false;
        }
        try {
        LocalDate birthDate = LocalDate.parse(dob);
        LocalDate currentDate = LocalDate.now();
        return !birthDate.isAfter(currentDate);
    } catch (DateTimeParseException e) {
        return false;
    }
    }

    public boolean isValidEmail(String email) {
        String pattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return email != null && email.matches(pattern);
    }

    public int getAge() {
        LocalDate currentDate = LocalDate.now();
        LocalDate birthDate = LocalDate.parse(this.dob);
        int period = Period.between(birthDate, currentDate).getYears();
        return period;

    }

    public boolean isValidCNIC(String cnic) {
        String pattern = "^[0-9]{5}-[0-9]{7}-[0-9]{1}$";
        return cnic != null && cnic.matches(pattern);
    }

    public boolean isValidContactNumber(String number) {
        String pattern = "^[0-9]{4}-[0-9]{7}";
        return number != null && number.matches(pattern);
    }

    public void addAccount(Account acc) {
        if (!(accounts.contains(acc))) {
            this.accounts.add(acc);
        }
    }

    public void removeAccount(Account acc) {
        if (accounts.contains(acc)) {
            this.accounts.remove(acc);
        }
    }

    public boolean checkPassword(String inputPassword) {
        return this.password.toVerify(inputPassword);
    }

    public Customer checkCustomerExist(String userName, String passWord, ArrayList<Customer> customers) {
        for (Customer c : customers) {
            if (c.getUsername().equals(userName)) {
                if (c.checkPassword(passWord)) {
                    return c;
                }
            }
        }
        return null;
    }

    public Customer findByCNIC(ArrayList<Customer> customers, String cardNumber) {
        for (Customer c : customers) {
            if (c.getCNIC().equals(cardNumber)) {
                return c;
            }
        }
        return null;
    }

    public void addBeneficiary(Beneficiary bene){
        this.totalBeneficiary.add(bene);
    }

    public ArrayList<Beneficiary> getBeneficiaries(){
        return this.totalBeneficiary;
    }
}
