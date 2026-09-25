import java.util.ArrayList;
import java.util.Scanner;
import java.math.BigDecimal;
import account.*;
import admin.Admin;
import beneficiary.Beneficiary;
import customer.Customer;
import exceptions.*;
import transaction.*;
import department.*;
import dao.*;

public class BankManagementSystem {

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        printLogo();

        ArrayList<Customer> registeredCustomers = new CustomerDAO().findAll();
        Department department = null;

        try {
            department = new Department("tracK9982^^");
            department.setAllAdmins(new AdminDAO().findAll());

        } catch (InvalidPasswordFormatException e) {
            System.out.println("Failed to initialize department: " + e.getMessage());
            return;
        }

        boolean programRunning = true;

        while (programRunning) {
            System.out.println("Welcome! Are you a:");
            System.out.println("1. Customer");
            System.out.println("2. Admin");
            System.out.println("3. Department (Register new Admin)");
            System.out.println("4. Exit");
            System.out.print("Enter choice(1-4): ");
            int roleChoice = 0;
            try {
                roleChoice = Integer.parseInt(scan.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
                continue;
            }

            if (roleChoice == 1) {
                handleCustomerFlow(scan, registeredCustomers);

            } else if (roleChoice == 2) {
                handleAdminFlow(scan, department, registeredCustomers);

            } else if (roleChoice == 3) {
                handleDepartmentFlow(scan, department);

            } else if (roleChoice == 4) {
                programRunning = false;
                System.out.println("Thank you for using VaultEdge!");
            } else {
                System.out.println("Invalid choice.");
            }
        }

    }

    public static void printLogo() {
        System.out.println("██╗   ██╗ █████╗ ██╗   ██╗██╗  ████████╗███████╗██████╗  ██████╗ ███████╗");
        System.out.println("██║   ██║██╔══██╗██║   ██║██║  ╚══██╔══╝██╔════╝██╔══██╗██╔════╝ ██╔════╝");
        System.out.println("██║   ██║███████║██║   ██║██║     ██║   █████╗  ██║  ██║██║  ███╗█████╗  ");
        System.out.println("╚██╗ ██╔╝██╔══██║██║   ██║██║     ██║   ██╔══╝  ██║  ██║██║   ██║██╔══╝  ");
        System.out.println(" ╚████╔╝ ██║  ██║╚██████╔╝███████╗██║   ███████╗██████╔╝╚██████╔╝███████╗");
        System.out.println("  ╚═══╝  ╚═╝  ╚═╝ ╚═════╝ ╚══════╝╚═╝   ╚══════╝╚═════╝  ╚═════╝ ╚══════╝");
        System.out.println("                     Your Trusted Banking Partner");
        System.out.println();
    }

    public static void handleCustomerFlow(Scanner scan, ArrayList<Customer> registeredCustomers) {
        System.out.print("Login / Signup: ");
        String choice = scan.nextLine();

        if (choice.equalsIgnoreCase("Login")) {
            handleLogin(scan, registeredCustomers);

        } else if (choice.equalsIgnoreCase("Signup")) {
            handleSignUp(scan, registeredCustomers);
        } else {
            System.out.println("Enter valid option.");
        }

    }

    public static void handleLogin(Scanner scan, ArrayList<Customer> registeredCustomers) {

        boolean isValid = false;
        Customer trackCustomer = null;

        while (!isValid) {
            System.out.print("Enter username/ Email: ");
            String cred1 = scan.nextLine();

            System.out.print("Enter password: ");
            String cred2 = scan.nextLine();

            for (Customer c : registeredCustomers) {
                if (c.getUsername().equals(cred1) || c.getEmail().equals(cred1)) {
                    if (c.checkPassword(cred2)) {
                        System.out.println("Login Sucessfully");
                        trackCustomer = c;
                        isValid = true;
                        break;
                    }
                }
            }
            if (!isValid) {
                System.out.println("Invalid username/email or password. Try again? (yes/no)");
                String retry = scan.nextLine();

                if (!retry.equalsIgnoreCase("yes")) {
                    break;
                }
            }
        }
        if (isValid) {
            ArrayList<Account> allAccounts = trackCustomer.getAccounts();
            Account activeAccount = null;

            if (allAccounts.size() == 1) {
                activeAccount = allAccounts.get(0);

            } else if (allAccounts.size() > 1) {
                for (int i = 0; i < allAccounts.size(); i++) {
                    System.out.println((i + 1) + ". " + allAccounts.get(i).getAccountNumber());
                }

                System.out.println("Select account: ");
                int select = Integer.parseInt(scan.nextLine());

                activeAccount = allAccounts.get(select - 1);
            } else {
                System.out.println("You don't have any accounts yet.");
                System.out.print("Would you like to create one now? (yes/no): ");
                String createNow = scan.nextLine();
                if (createNow.equalsIgnoreCase("yes")) {
                    Account newAcc = accountCreation(scan, trackCustomer, registeredCustomers);
                    activeAccount = newAcc;
                }
            }
            if (activeAccount != null) {
                showAccountMenu(scan, trackCustomer, activeAccount, registeredCustomers);
            }
        }

    }

    public static void handleSignUp(Scanner scan, ArrayList<Customer> registeredCustomers) {
        boolean signUpTry = false;
        Customer newCustomer = null;

        while (!signUpTry) {
            System.out.print("Enter full name: ");
            String name = scan.nextLine();

            System.out.print("Enter DOB (YYYY-MM-DD): ");
            String dob = scan.nextLine();

            System.out.print("Enter email: ");
            String email = scan.nextLine();

            System.out.print("Enter CNIC (XXXXX-XXXXXXX-X): ");
            String cnic = scan.nextLine();
            Customer existing = null;
            for (Customer c : registeredCustomers) {
                if (c.getCNIC().equals(cnic)) {
                    existing = c;
                    break;
                }
            }

            if (existing != null) {
                System.out.println("An account with this CNIC already exists. Redirecting to verify: ");
                System.out.print("Enter your existing username: ");
                String verifyUser = scan.nextLine();
                System.out.print("Enter your existing password: ");
                String verifyPass = scan.nextLine();

                if (existing.getUsername().equals(verifyUser) && existing.checkPassword(verifyPass)) {
                    Account newAccount = accountCreation(scan, existing, registeredCustomers);
                    if (newAccount != null) {
                        showAccountMenu(scan, existing, newAccount, registeredCustomers);
                    }
                } else {

                    System.out.println("Identity verification failed. Cannot proceed.");

                }
                return;
            }
            System.out.print("Enter contact number (XXXX-XXXXXXX): ");
            String contact = scan.nextLine();

            System.out.print("Enter home address: ");
            String address = scan.nextLine();

            System.out.print("Choose a username: ");
            String username = scan.nextLine();
            boolean usernameTaken = false;
            for (Customer c : registeredCustomers) {
                if (c.getUsername().equalsIgnoreCase(username)) {
                    usernameTaken = true;
                    break;
                }
            }
            if (usernameTaken) {
                System.out.println("This username is already taken. Please choose another.");
                System.out.print("Choose a username: ");
                username = scan.nextLine();
            }
            System.out.print("Choose a password: ");
            String password = scan.nextLine();

            try {
                newCustomer = new Customer(name, dob, password, email, cnic, contact, address, username);
                int newId = new CustomerDAO().save(newCustomer);
                newCustomer.setDbId(newId);
                registeredCustomers.add(newCustomer);
                System.out.println("Signup successful!");
                signUpTry = true;
            } catch (InvalidNameException | InvalidDateException | InvalidPasswordFormatException
                    | InvalidEmailException | InvalidAgeException | InvalidCNICException
                    | InvalidContactNumberException e) {
                System.out.println("Signup Failed: " + e.getMessage());
                System.out.println("Try again? (yes/no)");
                String retry = scan.nextLine();

                if (!retry.equalsIgnoreCase("yes")) {
                    return;
                }
            }
        }
        Account newAccount = null;
        while (newAccount == null) {
            newAccount = accountCreation(scan, newCustomer, registeredCustomers);
            if (newAccount == null) {
                System.out.println("Try creating an account again? (yes/no)");
                String retry = scan.nextLine();
                if (!retry.equalsIgnoreCase("yes")) {
                    System.out.println("Signup saved. You can add an account later after logging in.");
                    return;
                }
            }
        }
        showAccountMenu(scan, newCustomer, newAccount, registeredCustomers);
    }

    public static Account accountCreation(Scanner scan,
            Customer newCustomer, ArrayList<Customer> registeredCustomers) {
        System.out.println("Choose account type: 1) Savings  2) Current");
        int accType = Integer.parseInt(scan.nextLine());

        System.out.print("Set a 4-digit transaction PIN: ");
        String pin = scan.nextLine();

        System.out.print("Enter opening balance: ");
        BigDecimal balance = new BigDecimal(scan.nextLine());

        try {
            Account newAccount;
            if (accType == 1) {
                newAccount = new SavingAccount(newCustomer, registeredCustomers, pin, balance);
            } else {
                newAccount = new CurrentAccount(newCustomer, registeredCustomers, pin, balance);
            }
            newCustomer.addAccount(newAccount);
            int newAccountId = new AccountDAO().save(newAccount, newCustomer.getdbId());
            newAccount.setDbId(newAccountId);
            System.out.println("Account created! Your account number: " + newAccount.getAccountNumber());
            return newAccount;
        } catch (InvalidPinException | MinimumBalanceException e) {
            System.out.println("Accout creation failed: " + e.getMessage());
            return null;

        }

    }

    public static void showAccountMenu(Scanner scan, Customer customer, Account activeAccount,
            ArrayList<Customer> registeredCustomers) {
        boolean running = true;
        while (running) {
            System.out.print("\n1- Deposit" + "\n2- WithDraw"
                    + "\n3- Transfer" + "\n4- ViewBalance" +
                    "\n5- Create new Account" + "\n6- Add Beneficiary"
                    + "\n7- Transfer to Beneficiary" + "\n8- Account Setting"
                    + "\n9- Logout" + "\n");
            System.out.print("Choose option (1-9): ");

            int option;
            try {
                option = Integer.parseInt(scan.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
                continue;
            }

            switch (option) {
                case 1: {
                    System.out.println("Enter amount to deposit: ");
                    BigDecimal amountDep = new BigDecimal(scan.nextLine());
                    try {
                        activeAccount.deposit(amountDep);
                        new AccountDAO().updateBalance(activeAccount.getAccountNumber(),
                                activeAccount.getCurrentBalance());

                        System.out.println("Deposit Sucessfull!");
                        ArrayList<Transaction> transactionList = activeAccount.getTransactionHistory();
                        Transaction latest = transactionList.get(transactionList.size() - 1);
                        new TransactionDAO().save(latest, activeAccount.getDbId(), null);

                        System.out.println("View receipt as :" + "\n1-  Brief " + "\n2- Detailed");
                        int receipt;
                        try {
                            receipt = Integer.parseInt(scan.nextLine());
                        } catch (NumberFormatException e) {
                            System.out.println("Please enter a valid number.");
                            continue;
                        }
                        if (receipt == 1) {
                            System.out.println(latest.generateBreifReceipt());
                        } else {
                            System.out.println(latest.generateDetailedReceipt());
                        }
                    } catch (AccountNotActiveException | InvalidAmountException e) {
                        System.out.println("Deposit failed:" + e.getMessage());
                    }
                    break;
                }

                case 2: {
                    System.out.println("Enter amount to WithDraw : ");
                    BigDecimal amountWithdraw = new BigDecimal(scan.nextLine());
                    if (!confirmPin(scan, activeAccount)) {
                        break;
                    }
                    try {
                        activeAccount.withdraw(amountWithdraw);
                        new AccountDAO().updateBalance(activeAccount.getAccountNumber(),
                                activeAccount.getCurrentBalance());
                        System.out.println("WithDrawal Sucessfull!");

                        ArrayList<Transaction> transactionList = activeAccount.getTransactionHistory();
                        Transaction latest = transactionList.get(transactionList.size() - 1);
                        new TransactionDAO().save(latest, activeAccount.getDbId(), null);

                        System.out.println("View receipt as :" + "\n1-  Brief " + "\n2- Detailed");
                        int receipt;
                        try {
                            receipt = Integer.parseInt(scan.nextLine());
                        } catch (NumberFormatException e) {
                            System.out.println("Please enter a valid number.");
                            continue;
                        }
                        if (receipt == 1) {
                            System.out.println(latest.generateBreifReceipt());
                        } else {
                            System.out.println(latest.generateDetailedReceipt());
                        }
                    } catch (AccountNotActiveException | InvalidAmountException | InsufficientBalanceException
                            | TransactionLimitExceededException
                            | MinimumBalanceException e) {
                        System.out.println("WithDrawal failed:" + e.getMessage());
                    }
                    break;
                }

                case 3: {
                    if (activeAccount instanceof CurrentAccount) {
                        CurrentAccount currentAcc = (CurrentAccount) activeAccount;

                        System.out.print("Enter target account number: ");
                        String targetAccNum = scan.nextLine();
                        Account givenAccount = null;
                        for (Customer c : registeredCustomers) {
                            for (Account a : c.getAccounts()) {
                                if (a.getAccountNumber().equals(targetAccNum)) {
                                    givenAccount = a;
                                    break;
                                }
                            }
                        }
                        if (givenAccount == null) {
                            System.out.println("Account not found");
                        } else {
                            System.out.print("Enter amount to transfer: ");
                            BigDecimal amount = new BigDecimal(scan.nextLine());
                            if (!confirmPin(scan, activeAccount)) {
                                break;
                            }
                            try {
                                currentAcc.transfer(givenAccount, amount);
                                new AccountDAO().updateBalance(currentAcc.getAccountNumber(),
                                        currentAcc.getCurrentBalance());
                                new AccountDAO().updateBalance(givenAccount.getAccountNumber(),
                                        givenAccount.getCurrentBalance());

                                System.out.println("Transfer sucessfull!");
                                ArrayList<Transaction> transactionList = currentAcc.getTransactionHistory();
                                Transaction latest = transactionList.get(transactionList.size() - 1);
                                new TransactionDAO().save(latest, currentAcc.getDbId(), givenAccount.getDbId());

                                System.out.println("View receipt as :" + "\n1-  Brief " + "\n2- Detailed");
                                int receipt;
                                try {
                                    receipt = Integer.parseInt(scan.nextLine());
                                } catch (NumberFormatException e) {
                                    System.out.println("Please enter a valid number.");
                                    continue;
                                }
                                if (receipt == 1) {
                                    System.out.println(latest.generateBreifReceipt());
                                } else {
                                    System.out.println(latest.generateDetailedReceipt());
                                }
                            } catch (AccountNotActiveException | InvalidAmountException | MinimumBalanceException
                                    | InsufficientBalanceException | TransactionLimitExceededException e) {
                                System.out.println("Transfer failed: " + e.getMessage());
                            }
                        }
                    } else {
                        System.out.println("Transfer is avalaible only for current account.");
                    }
                    break;
                }

                case 4: {
                    System.out.println("Current Balance: " + activeAccount.getCurrentBalance());
                    break;
                }

                case 5: {
                    Account newAccount = accountCreation(scan, customer, registeredCustomers);
                    if (newAccount != null) {
                        showAccountMenu(scan, customer, newAccount, registeredCustomers);
                    }
                    break;
                }
                case 6: {
                    System.out.print("Enter beneficiary name: ");
                    String benName = scan.nextLine();
                    System.out.print("Enter beneficiary account number: ");
                    String benAccNum = scan.nextLine();
                    System.out.print("Enter nickname (optional, press Enter to skip): ");
                    String nickname = scan.nextLine();

                    Beneficiary newBen = new Beneficiary(benName, benAccNum, nickname);
                    customer.addBeneficiary(newBen);
                    new BeneficiaryDAO().save(newBen, customer.getdbId());

                    System.out.println("Beneficiary added successfully!");
                    break;
                }
                case 7: {
                    if (activeAccount instanceof CurrentAccount) {
                        CurrentAccount currentAcc = (CurrentAccount) activeAccount;
                        ArrayList<Beneficiary> benList = customer.getBeneficiaries();

                        if (benList.isEmpty()) {
                            System.out.println("No beneficiaries saved yet.");
                        } else {
                            System.out.println("Your beneficiaries:");
                            for (int i = 0; i < benList.size(); i++) {
                                System.out.println((i + 1) + ". " + benList.get(i).getBeneficiaryName() +
                                        " (" + benList.get(i).getAccountNumber() + ")");
                            }
                            System.out.print("Select beneficiary: ");
                            int benChoice = Integer.parseInt(scan.nextLine());
                            Beneficiary selectedBen = benList.get(benChoice - 1);

                            Account targetAccount = null;
                            for (Customer c : registeredCustomers) {
                                for (Account a : c.getAccounts()) {
                                    if (a.getAccountNumber().equals(selectedBen.getAccountNumber())) {
                                        targetAccount = a;
                                        break;
                                    }
                                }
                            }

                            if (targetAccount == null) {
                                System.out.println("Beneficiary's account no longer exists.");
                            } else {
                                System.out.print("Enter amount to transfer: ");
                                BigDecimal amount = new BigDecimal(scan.nextLine());
                                if (!confirmPin(scan, activeAccount)) {
                                    break;
                                }
                                try {
                                    currentAcc.transfer(targetAccount, amount);
                                    new AccountDAO().updateBalance(currentAcc.getAccountNumber(),
                                            currentAcc.getCurrentBalance());
                                    new AccountDAO().updateBalance(targetAccount.getAccountNumber(),
                                            targetAccount.getCurrentBalance());
                                    System.out.println("Transfer successful!");
                                    ArrayList<Transaction> transactionList = currentAcc.getTransactionHistory();
                                    Transaction latest = transactionList.get(transactionList.size() - 1);
                                    new TransactionDAO().save(latest, currentAcc.getDbId(), targetAccount.getDbId());
                                    System.out.println("View receipt as :" + "\n1-  Brief " + "\n2- Detailed");
                                    int receipt;
                                    try {
                                        receipt = Integer.parseInt(scan.nextLine());
                                    } catch (NumberFormatException e) {
                                        System.out.println("Please enter a valid number.");
                                        continue;
                                    }
                                    if (receipt == 1) {
                                        System.out.println(latest.generateBreifReceipt());
                                    } else {
                                        System.out.println(latest.generateDetailedReceipt());
                                    }
                                } catch (AccountNotActiveException | InvalidAmountException | MinimumBalanceException
                                        | InsufficientBalanceException | TransactionLimitExceededException e) {
                                    System.out.println("Transfer failed: " + e.getMessage());
                                }
                            }
                        }
                    } else {
                        System.out.println("Transfer is only available for Current accounts.");
                    }
                    break;
                }
                case 8: {
                    accountSetting(scan, activeAccount, customer);
                    break;
                }
                case 9: {
                    System.out.println("Logged out.");
                    running = false;
                    break;
                }
            }
        }

    }

    public static void accountSetting(Scanner scan, Account account, Customer custom) {
        boolean settingsRunning = true;
        while (settingsRunning) {
            System.out.print("\n1- Change Password" + "\n2- Reset Transaction PIN" +
                    "\n3- Update Contact Number" + "\n4- Update Home Address" + "\n5- Back");
            System.out.print("\nChoose option (1-5): ");
            int settingChoice;
            try {
                settingChoice = Integer.parseInt(scan.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
                continue;
            }

            switch (settingChoice) {
                case 1: {
                    System.out.println("Enter Current Password: ");
                    String passKey1 = scan.nextLine();
                    System.out.println("Enter new Password: ");
                    String passKey2 = scan.nextLine();
                    try {
                        custom.changePassword(passKey1, passKey2);
                        new CustomerDAO().updatePassword(custom.getdbId(), custom.getHashedPassword());

                    } catch (InvalidPasswordFormatException e) {
                        System.out.println("Update fsiled: " + e.getMessage());
                    }
                    break;
                }
                case 2: {
                    System.out.println("Enter current Transaction PIN: ");
                    String pin1 = scan.nextLine();
                    System.out.println("Enter new Transaction PIN: ");
                    String pin2 = scan.nextLine();
                    try {
                        account.setTransactionPIN(pin1, pin2);
                        new AccountDAO().updatePin(account.getAccountNumber(), account.getHashedPin());
                        System.out.println("PIN updated sucessfully.");
                    } catch (InvalidPinException e) {
                        System.out.println("Update failed: " + e.getMessage());

                    }
                    break;
                }
                case 3: {
                    System.out.println("Current contact number: " + custom.getContactNumber());
                    System.out.println("Enter new Contact Number (XXXX-XXXXXXX): ");
                    String number = scan.nextLine();
                    try {
                        custom.setContactNumber(number);
                        new CustomerDAO().updateContact(custom.getdbId(), custom.getContactNumber());

                        System.out.println("Contact Number updated sucessfully.");
                    } catch (InvalidContactNumberException e) {
                        System.out.println("UPdate failed: " + e.getMessage());

                    }

                    break;
                }
                case 4: {
                    System.out.println("Current home address: " + custom.getHomeAddress());
                    System.out.println("Enter new Home Address: ");
                    String address = scan.nextLine();
                    custom.setHomeAddress(address);
                    new CustomerDAO().updateAddress(custom.getdbId(), custom.getHomeAddress());

                    System.out.println("Address updated sucessfully.");

                    break;
                }
                case 5: {
                    settingsRunning = false;
                    break;
                }
            }
        }

    }

    public static void handleAdminFlow(Scanner scan, Department department, ArrayList<Customer> registeredCustomers) {
        boolean validAdmin = false;
        Admin trackAdmin = null;
        System.out.print("Enter admin username: ");
        String adminUser = scan.nextLine();

        System.out.print("Enter admin password: ");
        String adminPass = scan.nextLine();

        for (Admin i : department.getAllAdmins()) {
            if (i.getUsername().equals(adminUser)) {
                if (i.login(adminPass)) {
                    validAdmin = true;
                    trackAdmin = i;
                    break;

                }
            }
        }
        if (!validAdmin) {
            System.out.println("Inavlid Admin credentials.");
        } else {
            System.out.println("Admin login successful!");
            showAdminMenu(scan, trackAdmin, registeredCustomers);
        }

    }

    public static void showAdminMenu(Scanner scan, Admin admin, ArrayList<Customer> registeredCustomers) {
        boolean running = true;
        while (running) {
            System.out.print("\n1- View All Customers" + "\n2- Freeze Account" +
                    "\n3- Close Account" + "\n4- Reactivate Account" +
                    "\n5- Total Bank Balance" + "\n6- Credit Monthly Interest" +
                    "\n7- Logout" + "\n");
            System.out.print("Choose (1-7): ");
            int option;
            try {
                option = Integer.parseInt(scan.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
                continue;
            }
            switch (option) {
                case 1: {
                    admin.viewAllCustomers(registeredCustomers);
                    break;
                }
                case 2: {
                    System.out.print("Enter account number to freeze: ");
                    String accNum = scan.nextLine();
                    try {
                        admin.freezeAccount(accNum, registeredCustomers);
                        Account acc = admin.findAccountByNum(accNum, registeredCustomers);
                        new AccountDAO().updateStatus(accNum, acc.getAccountStatus());

                    } catch (InvalidStatusChangeException e) {
                        System.out.println("Account Freezing failed: " + e.getMessage());
                    }
                    break;
                }
                case 3: {
                    System.out.print("Enter account number to close: ");
                    String accNum = scan.nextLine();
                    try {
                        admin.closeAccount(accNum, registeredCustomers);
                        Account acc = admin.findAccountByNum(accNum, registeredCustomers);
                        new AccountDAO().updateStatus(accNum, acc.getAccountStatus());
                    } catch (InvalidStatusChangeException e) {
                        System.out.println("Account closing failed: " + e.getMessage());

                    }
                    break;
                }
                case 4: {
                    System.out.print("Enter account number to reactivate: ");
                    String accNum = scan.nextLine();
                    try {
                        admin.reactiveAccount(accNum, registeredCustomers);
                        Account acc = admin.findAccountByNum(accNum, registeredCustomers);
                        new AccountDAO().updateStatus(accNum, acc.getAccountStatus());
                    } catch (InvalidStatusChangeException e) {
                        System.out.println("Reactivation failed: " + e.getMessage());

                    }
                    break;
                }
                case 5: {
                    System.out.println("Total Bank Balance: " + admin.getTotalBankBalance(registeredCustomers));
                    break;
                }
                case 6: {
                    System.out.print("Enter Savings account number: ");
                    String accNum = scan.nextLine();
                    Account acc = admin.findAccountByNum(accNum, registeredCustomers);

                    if (acc == null) {
                        System.out.println("Account not found.");
                    } else if (acc instanceof SavingAccount) {
                        SavingAccount savAcc = (SavingAccount) acc;
                        try {
                            savAcc.calculateMonthlyInterest();
                            new AccountDAO().updateBalance(savAcc.getAccountNumber(), savAcc.getCurrentBalance());
                        } catch (AccountNotActiveException e) {
                            System.out.println(e.getMessage());
                        }
                        System.out.println("Monthly interest credited. New balance: " + acc.getCurrentBalance());
                        ArrayList<Transaction> transactionList = savAcc.getTransactionHistory();
                        Transaction latest = transactionList.get(transactionList.size() - 1);
                        new TransactionDAO().save(latest, acc.getDbId(), null);

                        System.out.println("View receipt as :" + "\n1-  Brief " + "\n2- Detailed");
                        int receipt = Integer.parseInt(scan.nextLine());
                        if (receipt == 1) {
                            System.out.println(latest.generateBreifReceipt());
                        } else {
                            System.out.println(latest.generateDetailedReceipt());
                        }

                    } else {
                        System.out.println("Interest only applies to Savings accounts.");
                    }
                    break;
                }
                case 7: {
                    running = false;
                    System.out.println("Admin logged out.");
                    break;
                }
            }
        }
    }

    public static void handleDepartmentFlow(Scanner scan, Department department) {
        boolean departTry = true;

        while (departTry) {
            System.out.print("Enter master password: ");
            String masterPass = scan.nextLine();

            if (department.verifyMasterPassword(masterPass)) {
                boolean registerAdmin = true;
                while (registerAdmin) {
                    System.out.print("Enter new admin username: ");
                    String newAdminUser = scan.nextLine();
                    System.out.print("Enter new admin password: ");
                    String newAdminPass = scan.nextLine();

                    try {
                        department.registerAdmin(newAdminUser, newAdminPass);
                        Admin newAdmin = department.getAllAdmins().get(department.getAllAdmins().size() - 1);
                        new AdminDAO().save(newAdmin);

                        System.out.println("Admin registered successfully!");
                        registerAdmin = false;
                        departTry = false;
                    } catch (InvalidPasswordFormatException e) {
                        System.out.println("Registration failed: " + e.getMessage());
                        System.out.println("Try again? (Yes/No)");
                        String retry = scan.nextLine();
                        if (!retry.equalsIgnoreCase("Yes")) {
                            break;
                        }
                    }
                }
            } else {
                System.out.println("Incorrect master password.Try again? (Yes/No)");
                String retry = scan.nextLine();
                if (!retry.equalsIgnoreCase("Yes")) {
                    break;
                }
            }
        }
    }

    public static boolean confirmPin(Scanner scan, Account account) {
        System.out.print("Enter transaction PIN: ");
        String enteredPin = scan.nextLine();
        if (!account.verifyPin(enteredPin)) {
            System.out.println("Incorrect PIN. Transaction cancelled.");
            return false;
        }
        return true;
    }
}