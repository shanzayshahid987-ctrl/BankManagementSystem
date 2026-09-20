import java.util.ArrayList;
import java.util.Scanner;
import java.math.BigDecimal;
import account.*;
import admin.Admin;
import customer.Customer;
import exceptions.*;
import transaction.*;

public class BankManagementSystem {

    public static void main(String[] args){
     Scanner scan = new Scanner(System.in);
     ArrayList<Customer> registeredCustomers = new ArrayList<>();
     System.out.println("-----VAULTEDGE-----");
     System.out.println("Welcome! Are you a:");
        System.out.println("1. Customer");
        System.out.println("2. Admin");
        System.out.print("Enter choice: ");
        int roleChoice = Integer.parseInt(scan.nextLine());
        if(roleChoice == 1){
           System.out.print("Login / Signup ");
           String choice = scan.nextLine();
           if(choice.equals("Login") || 
           choice.equals("login") || choice.equals("LOGIN") ){
               boolean isValid = false;
               Customer trackCustomer = null;
                System.out.print("Enter username/ Email: ");
                String cred1 = scan.nextLine();
                System.out.print("Enter password: ");
                String cred2 = scan.nextLine();
                for(Customer c : registeredCustomers){
                    if(c.getUsername().equals(cred1) || c.getEmail().equals(cred1)){
                        if(c.checkPassword(cred2)){
                            System.out.println("Login Sucessfully");
                            trackCustomer=c;
                            isValid= true;
                            break;
                        }
                    }
                
                } if(!isValid){
                       System.out.println("Invalid username/email or password.");
                    } else{
                        ArrayList<Account> allAccounts = trackCustomer.getAccounts();
                        Account activeAccount = null;
                        if(allAccounts.size() == 1){
                            activeAccount= allAccounts.get(0);
                        } else if(allAccounts.size() > 1){
                             for(int i=0;i < allAccounts.size();i++){
                                System.out.println((i+1) + ". " + allAccounts.get(i).getAccountNumber());
                             }
                             System.out.println("Select account: ");
                             int select = Integer.parseInt(scan.nextLine());
                             activeAccount = allAccounts.get(select - 1); 
                        } else{
                            System.out.println("No account found.");
                        }
                        if(activeAccount!=null){
                            showAccountMenu(scan, activeAccount, registeredCustomers);

                             }
                        }
           } else if(choice.equals("Signup") || 
           choice.equals("signup") || choice.equals("SIGNUP")){

             System.out.print("Enter full name: ");
             String name = scan.nextLine();
    
             System.out.print("Enter DOB (YYYY-MM-DD): ");
            String dob = scan.nextLine();
    
             System.out.print("Enter email: ");
             String email = scan.nextLine();

            System.out.print("Enter CNIC (XXXXX-XXXXXXX-X): ");
            String cnic = scan.nextLine();
    
            System.out.print("Enter contact number (XXXX-XXXXXXX): ");
            String contact = scan.nextLine();
    
            System.out.print("Enter home address: ");
            String address = scan.nextLine();

            System.out.print("Choose a username: ");
            String username = scan.nextLine();
    
            System.out.print("Choose a password: ");
            String password = scan.nextLine();
 
            try{
                Customer newCustomer = new Customer(name, dob, password, email, cnic, contact, address, username);
                System.out.println("Signup successful!");
                registeredCustomers.add(newCustomer);

                System.out.println("Choose account type: 1) Savings  2) Current");
                int accType = Integer.parseInt(scan.nextLine());
    
                 System.out.print("Set a 4-digit transaction PIN: ");
                 String pin = scan.nextLine();
    
                System.out.print("Enter opening balance: ");
                 BigDecimal balance = new BigDecimal(scan.nextLine());

                try{
                    Account newAccount;
                    if(accType==1){
                        newAccount = new SavingAccount(newCustomer, registeredCustomers, pin, balance);
                    } else{
                         newAccount = new CurrentAccount(newCustomer, registeredCustomers, pin, balance);
                    }
                    newCustomer.addAccount(newAccount);
                     System.out.println("Account created! Your account number: " + newAccount.getAccountNumber());
                     showAccountMenu(scan, newAccount, registeredCustomers);
                }catch(InvalidPinException| MinimumBalanceException e){
                    System.out.println("Accout creation failed: " + e.getMessage());

                }

            }catch(InvalidNameException| InvalidDateException| InvalidPasswordFormatException| 
            InvalidEmailException|  InvalidAgeException|
             InvalidCNICException| InvalidContactNumberException e ){
                System.out.println("Signup Failed: " + e.getMessage());
                
            }

        }













        } else if(roleChoice == 2){

        }else{
            System.out.println("Invalid choice.");
        }



    }

    public static void showAccountMenu(Scanner scan, Account activeAccount,
        ArrayList<Customer> registeredCustomers){
                         boolean running = true;
                            while(running){
                                System.out.print("\n1- Deposit" + "\n2- WithDraw"
                                    + "\n3- Transfer" + "\n4- ViewBalance" + "\n5- Logout" );
                                int option = Integer.parseInt(scan.nextLine());
                                switch(option){
                                    case 1: {
                                        System.out.println("Enter amount to deposit: ");
                                        BigDecimal amountDep = new BigDecimal(scan.nextLine());
                                        try{
                                            activeAccount.deposit(amountDep);
                                            System.out.println("Deposit Sucessfull!");
                                            ArrayList<Transaction> transactionList = activeAccount.getTransactionHistory();
                                            Transaction latest = transactionList.get(transactionList.size() - 1);
                                                 System.out.println("View receipt as :" + "\n1-  Brief " + "\n2- Detailed");
                                                 int receipt = Integer.parseInt(scan.nextLine());
                                                 if(receipt == 1){
                                                    System.out.println(latest.generateBreifReceipt());
                                                 } else{
                                                    System.out.println(latest.generateDetailedReceipt());
                                                 }
                                        }catch( AccountNotActiveException | InvalidAmountException e ){
                                             System.out.println("Deposit failed:" + e.getMessage());
                                        }
                                        break;
                                    }

                                    case 2: {
                                          System.out.println("Enter amount to WithDraw : ");
                                        BigDecimal amountWithdraw = new BigDecimal(scan.nextLine());
                                        try{
                                            activeAccount.withdraw(amountWithdraw);
                                            System.out.println("WithDrawal Sucessfull!");
                                            ArrayList<Transaction> transactionList = activeAccount.getTransactionHistory();
                                            Transaction latest = transactionList.get(transactionList.size() - 1);
                                                 System.out.println("View receipt as :" + "\n1-  Brief " + "\n2- Detailed");
                                                 int receipt = Integer.parseInt(scan.nextLine());
                                                 if(receipt == 1){
                                                    System.out.println(latest.generateBreifReceipt());
                                                 } else{
                                                    System.out.println(latest.generateDetailedReceipt());
                                                 }
                                        }catch( AccountNotActiveException | 
                                        InvalidAmountException |   InsufficientBalanceException |
                                         TransactionLimitExceededException
                                         | MinimumBalanceException  e ){
                                             System.out.println("WithDrawal failed:" + e.getMessage());
                                        }
                                        break;
                                    }

                                    case 3: {
                                        if(activeAccount instanceof CurrentAccount){
                                            CurrentAccount currentAcc = (CurrentAccount) activeAccount;

                                            System.out.print("Enter target account number: ");
                                            String targetAccNum = scan.nextLine();
                                            Account givenAccount = null;
                                            for(Customer c: registeredCustomers){
                                                for(Account a : c.getAccounts()){
                                                    if(a.getAccountNumber().equals(targetAccNum)){
                                                        givenAccount=a;
                                                        break;
                                                    }
                                                }
                                            } if(givenAccount == null){
                                                System.out.println("Account not found");
                                            }else{
                                                System.out.print("Enter amount to transfer: ");
                                                BigDecimal amount = new BigDecimal(scan.nextLine());

                                                try{
                                                    currentAcc.transfer(givenAccount,amount);
                                                    System.out.println("Transfer sucessfull!");
                                                 ArrayList<Transaction> transactionList = currentAcc.getTransactionHistory();
                                                 Transaction latest = transactionList.get(transactionList.size() - 1);
                                                 System.out.println("View receipt as :" + "\n1-  Brief " + "\n2- Detailed");
                                                 int receipt = Integer.parseInt(scan.nextLine());
                                                 if(receipt == 1){
                                                    System.out.println(latest.generateBreifReceipt());
                                                 } else{
                                                    System.out.println(latest.generateDetailedReceipt());
                                                 }
                                                }catch( AccountNotActiveException |InvalidAmountException |
                                                    MinimumBalanceException | InsufficientBalanceException | 
                                                    TransactionLimitExceededException e){
                                                        System.out.println("Transfer failed: " + e.getMessage());
                                                }
                                            }
                                        } else{
                                            System.out.println("Transfer is avalaible only for current account.");
                                        }
                                        break;
                                    }

                                    case 4: {
                                        System.out.println("Current Balance: " + activeAccount.getCurrentBalance());
                                       break;
                                    }

                                    case 5: {
                                        System.out.println("Logged out.");
                                        running=false;
                                        break;
                                    }
                                }       
    }
}

public static void showAdminMenu(Scanner scan, Admin admin, ArrayList<Customer> registeredCustomers){
    boolean running = true;
    while(running){
        System.out.print("\n1- View All Customers" + "\n2- Freeze Account" + 
                          "\n3- Close Account" + "\n4- Reactivate Account" + 
                          "\n5- Total Bank Balance" + "\n6- Logout");
        int option = Integer.parseInt(scan.nextLine());
        switch(option){
            case 1: {
                admin.viewAllCustomers(registeredCustomers);
                break;
            }
            case 2: {
                System.out.print("Enter account number to freeze: ");
                String accNum = scan.nextLine();
                admin.freezeAccount(accNum, registeredCustomers);
                break;
            }
            case 3: {
                System.out.print("Enter account number to close: ");
                String accNum = scan.nextLine();
                admin.closeAccount(accNum, registeredCustomers);
                break;
            }
            case 4: {
                System.out.print("Enter account number to reactivate: ");
                String accNum = scan.nextLine();
                admin.reactiveAccount(accNum, registeredCustomers);
                break;
            }
            case 5: {
                System.out.println("Total Bank Balance: " + admin.getTotalBankBalance(registeredCustomers));
                break;
            }
            case 6: {
                running = false;
                System.out.println("Admin logged out.");
                break;
            }
        }
    }
}
}