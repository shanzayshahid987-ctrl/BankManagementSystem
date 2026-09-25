# VaultEdge — Java Bank Management System

A banking application written in core java which emulates real world operations such as account management, transactions, managing beneficiaries, etc.

## Features

- **Account Management**
  - Two types of accounts i.e. `SavingAccount` (which earns interest, allows up to 3 withdrawals per month) and `CurrentAccount` (has minimum balance requirements, direct transfers allowed)
  - Signup and Login of customers securely using hashed passwords
  - Transactions require the PIN of user

- **Transactions**
  - Deposit and withdrawal
  - Transfer money from one account to another
  - Auto generated unique ID for every transaction
  - Digital receipt provided in short and long form after every transaction

- **Beneficiaries**
  - Users can save and manage beneficiary accounts for fast transfers

- **Admin & Department Controls**
  - Admin creation is done in a layer called 'Department', restricted with the master password (No open registration of admin)
  - Every month interest crediting and receipt generation by Admin

- **Account Settings**
  - Change password (Old password required)
  - Change PIN (Old PIN required)
  - Update phone number and home address

- **Data Persistence**
  - All the data of customers, admins and their respective accounts will be saved using java serialization
  - Migration to PostgreSQL currently in progress using JDBC

## Tech Stack

- **Language:** Java
- **Persistence:** Java Serialization (current) -> PostgreSQL via JDBC (currently in progress)
- **Tools:** VS Code, Git/GitHub

## Project Structure

```
src/
├── account/       # Account, SavingAccount, CurrentAccount
├── admin/         # Admin logic
├── beneficiary/   # Beneficiary management
├── customer/      # Customer logic
├── department/    # Department/admin management
├── exceptions/     # Exception classes
├── security/       # Password Hashing/Verification, PIN
├── transaction/     # Transactions and receipts
├── FileManager.java          # Serialization based Save/Load operation
└── BankManagementSystem.java # Entry point of program
```

## Running Instructions

1. Clone the repository
   ```
   git clone https://github.com/shanzayshahid987-ctrl/BankManagementSystem.git
   ```
2. Open the project in VS Code (Java extension pack installed)
3. Run `BankManagementSystem.java` file (containing `main()` method)
4. Use the options from the console menu to register as customer or login as admin/department

## Future Plans

- [x] Basic banking operations implemented
- [x] Admin and Department functionality
- [x] Data persistence using java serialization
- [ ] Data persistence to PostgreSQL using JDBC
- [ ] Additional DSA and design pattern improvements

## Author

Shanzay — BSCS student, working his way towards Backend Java Development.
