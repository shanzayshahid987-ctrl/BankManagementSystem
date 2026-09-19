import java.util.ArrayList;
import java.util.Scanner;
import java.util.Scanner;
import java.math.BigDecimal;
import account.*;
import customer.Customer;
import exceptions.*;

public class BankManagementSystem {

    public static void main(String[] args){
     Scanner scan = new Scanner(System.in);
     ArrayList<Customer> allCustomers = new ArrayList<>();
     System.out.println("-----VAULTEDGE-----");
     System.out.println("Welcome! Are you a:");
        System.out.println("1. Customer");
        System.out.println("2. Admin");
        System.out.print("Enter choice: ");
        int roleChoice = Integer.parseInt(scan.nextLine());
        if(roleChoice == 1){
           System.out.print("Login / Signup ");
           String choice = scan.nextLine();
           if(choice.equals("Login") || choice.equals("login")){
               boolean isValid = false;
                System.out.print("Enter username/ Email: ");
                String cred1 = scan.nextLine();
                System.out.print("Enter password: ");
                String cred2 = scan.nextLine();
                for(Customer c : allCustomers){
                    if(c.getUsername().equals(cred1) || c.getEmail().equals(cred1)){
                        if(c.checkPassword(cred2)){
                            System.out.println("Login Sucessfully");
                            isValid= true;
                            break;
                        }
                    }
                } if(!isValid){
                       System.out.println("Invalid username/email or password.");
                    }

            }
           }













        } else if(roleChoice == 2){

        }else{
            System.out.println("Invalid choice.");
        }



    }
}