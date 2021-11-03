package com.company;

/*

MySQL tables


create table account (id INTEGER PRIMARY KEY AUTO_INCREMENT, balance DECIMAL(12,2), customer_id INTEGER, verified boolean DEFAULT false, CONSTRAINT fk_customer FOREIGN KEY (customer_id) REFERENCES customer(id));

create table customer (id INTEGER PRIMARY KEY AUTO_INCREMENT, username char(30) NOT NULL UNIQUE, first_name char(30), last_name char(30),password char(24) NOT NULL);

create table transaction (id INTEGER PRIMARY KEY AUTO_INCREMENT, act_id_from INTEGER, act_id_to INTEGER, balance DECIMAL(10,2), type char(20));

 */


import java.util.List;
import java.util.Scanner;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    public static void main(String[] args) throws SQLException {
        BankDao dao = BankDaoFactory.getBankDao();
        Scanner scanner = new Scanner(System.in);
        final int STAR_COUNTER = 40;
        final int SPACE_COUNTER = 5;
        final int WRONG_ENTRY_LIMIT = 5;
        String username;
        String firstName;
        String lastName;
        String password;
        List customerInfo;
        int customerId;


        //main menu that enables the user to identify as an employee or a customer
        for(int i = 1; i <= STAR_COUNTER; i++) System.out.print("*");
        System.out.println();
        System.out.println("Welcome.");
        System.out.println("ENTER 1 if you are an employee");
        System.out.println("ENTER 2 if you are a customer");
        for(int i = 1; i <= STAR_COUNTER; i++) System.out.print("*");
        System.out.println();
        String universalInd = scanner.next();


        // the switch statement handles the different input that the user might insert
        // Also accounts for invalid entries such as words and numbers that aren't available on the menu
        switch(universalInd){
            case "1":
                for (int i = 0; i < SPACE_COUNTER; ++i) System.out.println();
                for(int i = 1; i <= STAR_COUNTER; i++) System.out.print("*");
                System.out.println();
                System.out.println("Welcome dear employee!");
                System.out.print("Username: ");
                String empUsername = scanner.next();
                // Did not implement the database for this one since it would not make sense if users can register as employees
                //As of this moment, only one pair of username and password enables the employee to login
                if(!(empUsername.equals("revemployee"))){
                    System.out.println("You are not a registered employee. exiting ...");
                    break;
                }

                boolean empPassFlag = true;
                int empError = 0;
                // checking whether the user is inserting the correct password; they have 5 attempts
                // If inserted falsely, more than 5 times, the app will exit
                while(empPassFlag){
                    System.out.print("Password: ");
                    String empPass = scanner.next();
                    if(empPass.equals("1234")){
                        empPassFlag = false;
                    } else {
                        empError++;
                        if(empError >= 5){
                            System.out.println("Too many false insertions, exiting...");
                            return;
                        }
                        System.out.println("Wrong password!");
                        System.out.println("You have " + (WRONG_ENTRY_LIMIT-empError) + " attempts left!");
                    }
                }
                boolean employeeFlag = true;
                while(employeeFlag) {

                    // Employee's main menu
                    for(int i = 1; i <= STAR_COUNTER; i++) System.out.print("*");
                    System.out.println();
                    System.out.println("Choose an option!");
                    System.out.println("Enter 1 to approve/reject new accounts");
                    System.out.println("Enter 2 to view a customer's bank accounts");
                    System.out.println("Enter 3 to view transactions");
                    System.out.println("Enter 4 to exit");
                    for(int i = 1; i <= STAR_COUNTER; i++) System.out.print("*");
                    System.out.println();
                    String employeeInd = scanner.next();

                    switch(employeeInd){
                        // The employee has the ability to reject or approve nre accounts that the customers create
                        // Until approved, the customer may not use the account in any shape or form
                        // I have implemented this using an additional column, verified, that stores a boolean type
                        // When the employee approves, the verified column of the row is true; accounts' verified column is false on default
                        case "1":
                            List<Account> unverifiedAccounts = dao.getUnverifiedAccounts();
                            int o = 1;
                            for(Account e:unverifiedAccounts){
                                System.out.println("Enter "+o+" to determine the status of account " + e.getActId() + ", balance: " + e.getBalance());
                                o++;
                            }

                            boolean verificationFlag = true;
                            AtomicInteger verificationActId = new AtomicInteger();
                            int u = 0;
                            while(verificationFlag){
                                String verificationActIdString = scanner.next();
                                try{
                                    verificationActId.set(Integer.parseInt(verificationActIdString));
                                    if(verificationActId.get()<=0||verificationActId.get()>=o){
                                        u++;
                                        System.out.println("Enter a valid number. " + (WRONG_ENTRY_LIMIT-u) + " attempts left");
                                        if((u+1)>WRONG_ENTRY_LIMIT){
                                            System.out.println("Too many wrong entries");
                                            System.out.println("Exiting...");
                                            return;
                                        }
                                        continue;
                                    }
                                    verificationFlag = false;
                                } catch(NumberFormatException exp){
                                    System.out.println("Invalid entry. Enter a number. " + (WRONG_ENTRY_LIMIT-u) + " attempts left");
                                }
                            }

                            Account unverifiedAccount = unverifiedAccounts.get(verificationActId.get() -1);
                            //After choosing the account, the employee can decide whether to approve or reject
                            System.out.println("Enter 1 to approve");
                            System.out.println("Enter 2 to reject");
                            System.out.println("Enter any other button to return to the main menu");
                            String appRejInd = scanner.next();

                            switch(appRejInd){
                                case "1":
                                    //if approved, it will be verified
                                    dao.verifyAccount(unverifiedAccount.getActId());
                                    break;
                                case "2":
                                    // if rejected, it will be deleted from the table account
                                    dao.deleteAccount(unverifiedAccount.getActId());
                                    break;
                                default:
                                    System.out.println("returning to main menu...");
                                    break;

                            }
                            break;
                        case "2":
                            // retrieving all the accounts a customer has through their custId
                            System.out.print("Enter the customer ID: ");
                            int customerIdView = scanner.nextInt();
                            // allAccounts() will only return the verified accounts
                            List<Account> accountsToView = dao.allAccounts(customerIdView);
                            if(accountsToView.isEmpty()){
                                System.out.println("No customer with this ID exists.");
                            }
                            else {
                                for (Account f : accountsToView) {
                                    System.out.println("Account " + f.getActId() + ", balance : " + f.getBalance());
                                }
                            }
                            System.out.println("Press any button to return to main menu");
                            scanner.next();
                            break;
                        case "3":
                            //prints all three types of transactions(deposit, withdraw, and transfer)
                            dao.showTransActions();
                            System.out.println("Press any button to return to main menu");
                            scanner.next();
                            break;
                        case "4":
                            //Exit command
                            System.out.println("Exiting ...");
                            employeeFlag = false;
                            break;
                        default:
                            //anything other than 1,2,3,4
                            System.out.println("Please enter a valid number!");

                    }
                }
                break;
            case "2":
                for (int i = 1; i <= SPACE_COUNTER; ++i) System.out.println();
                System.out.println("Welcome dear customer");
                for(int i = 1; i <= STAR_COUNTER; i++) System.out.print("*");
                System.out.println();
                // This section is customer login
                // Firstly, customer is either new or existing and the user can choose
                System.out.println("Enter 1 if you are an existing customer");
                System.out.println("Enter 2 if you are a new customer");
                System.out.println("Enter any other button to exit");
                for(int i = 1; i <= STAR_COUNTER; i++) System.out.print("*");
                System.out.println();
                String customerInd = scanner.next();

                switch(customerInd){
                    case "1":
                        //if customer is existing, they have to insert their unique username
                        //if the username exists on the database, it will go to password section, else it will quit
                        System.out.print("Username: ");
                        username = scanner.next();
                        boolean isAvailableUsername = dao.isExistingCustomer(username);
                        if(!(isAvailableUsername)){
                            System.out.println("This username does not exist. Create an account first.");
                            return;
                        }
                        customerInfo = dao.viewCustomer(username);
                        customerId = Integer.parseInt((String) customerInfo.get(0));

                        boolean userPassed = false;
                        if(customerInfo.isEmpty()){
                            System.out.println("Invalid username, exiting ...");
                            return;
                        }

                        int errors = 0;
                        //5 Attempts to enter the correct password
                        while(!userPassed){
                            System.out.print("Password: ");
                            password = scanner.next();

                            if(errors >= 4){
                                System.out.println("Too many false insertions, exiting...");
                                return;
                            }

                            if(password.equals(customerInfo.get(4)))
                                userPassed = true;
                            else {
                                System.out.println("Wrong password, " +(4-errors)+ " attempts left!");
                                errors++;
                            }

                        }
                        System.out.println("Welcome " + customerInfo.get(2) + " " + customerInfo.get(3));
                        break;
                    case "2":
                        //As a new customer, you can create an account by giving your name, username(must be unique), and password
                        System.out.print("Enter your first name: ");
                        firstName = scanner.next();
                        System.out.print("Enter your last name: ");
                        lastName = scanner.next();
                        System.out.print("Enter your username: ");
                        username = scanner.next();
                        boolean isExistingCustomer = dao.isExistingCustomer(username);
                        if(isExistingCustomer) {
                            System.out.println("this username already exists. Chooser another username or attempt to login.");
                            return;
                        }
                        System.out.print("Enter your password: ");
                        password = scanner.next();
                        Customer customer = new Customer(firstName, lastName, username, password);
                        dao.addCustomer(customer);
                        customerId = Integer.parseInt(dao.viewCustomer(username).get(0));
                        break;

                    default:
                        System.out.println("exiting...");
                        return;
                }

                boolean customerFlag = true;
                while(customerFlag){


                    //Customer menu
                    for(int i = 1; i <= STAR_COUNTER; i++) System.out.print("*");
                    System.out.println();
                    System.out.println("Choose an option!");
                    System.out.println("Enter 1 to create a new account");
                    System.out.println("Enter 2 to view balances of your active accounts");
                    System.out.println("Enter 3 to deposit/withdraw money");
                    System.out.println("Enter 4 to transfer money");
                    System.out.println("Enter 5 to exit");
                    for(int i = 1; i <= STAR_COUNTER; i++) System.out.print("*");
                    System.out.println();
                    String customerMenuInd = scanner.next();

                    switch(customerMenuInd){
                        case "1":
                            // takes balance as argument to insert the properties of a new account in the account table
                            System.out.print("Enter the balance: ");
                            double balance = scanner.nextDouble();
                            Account account = new Account(customerId, balance);
                            dao.addAccount(account);
                            break;
                        case "2":
                            //views all the verified accounts the customer has
                            List<Account> accounts = dao.allAccounts(customerId);
                            if(accounts.isEmpty()){
                                System.out.println("No verified accounts exist.");
                                break;
                            }

                            for(int i = 1; i <= STAR_COUNTER; i++) System.out.print("*");
                            System.out.println();
                            for(Account a:accounts){
                                System.out.println("Account " + a.getActId() + ": $"+a.getBalance());
                            }
                            for(int i = 1; i <= STAR_COUNTER; i++) System.out.print("*");
                            System.out.println();
                            break;
                        case "3":
                            accounts = dao.allAccounts(customerId);
                            //accounts for customers with no active accounts
                            if(accounts.isEmpty()){
                                System.out.println("You do not have any active accounts.");
                                break;
                            }
                            //choose whether you want to deposit or withdraw
                            int z = 1;
                            System.out.println("Choose one of the two.");
                            System.out.println("Enter 1 to deposit");
                            System.out.println("Enter 2 to withdraw");
                            System.out.println("Enter any other button to exit");
                            String depWitInd = scanner.next();
                            boolean deposit = false;
                            switch(depWitInd){
                                case "1":
                                    deposit = true;
                                    break;
                                case "2":
                                    deposit = false;
                                    break;
                                default:
                                    System.out.println("Exiting...");
                                    return;

                            }

                            for(int i = 1; i <= STAR_COUNTER; i++) System.out.print("*");
                            System.out.println();
                            for(Account a:accounts){
                                System.out.println("Enter "+ z+" to use " + "account "+a.getActId()+": "+a.getBalance());
                                z++;
                            }
                            for(int i = 1; i <= STAR_COUNTER; i++) System.out.print("*");
                            System.out.println();
                            boolean operationHappened;

                            int idx = scanner.nextInt()-1;
                            int accountIdx = accounts.get(idx).getActId();

                            System.out.print("Enter the amount: ");
                            double amountChosen = scanner.nextDouble();

                            //accounts for negative entries
                            if (amountChosen <= 0){
                                System.out.println("Negative amount is invalid. Transaction did not occur.");
                                break;
                            }
                            operationHappened = dao.personalActions(accounts.get(idx), amountChosen, deposit, accountIdx);
                            if (operationHappened)
                                dao.insertDepOrWit(accountIdx, amountChosen, deposit);
                            break;
                        case "4":
                            List<Account> myAccount = dao.allAccounts(customerId);
                            if (myAccount.isEmpty()){
                                System.out.println("You do not have any active accounts.");
                                break;
                            }
                            System.out.println("Select which account you want to transfer from");
                            int w = 1;
                            for(Account d:myAccount){
                                System.out.println("Enter "+w+" to transfer from account "+d.getActId()+", balance: $"+d.getBalance());
                                w++;
                            }
                            int myAccountIdx = scanner.nextInt();
                            Account accountFrom = myAccount.get(myAccountIdx-1);
                            System.out.print("Enter the username you want to transfer to: ");
                            String usernameTo =  scanner.next();
                            boolean checkForUser = dao.isExistingCustomer(usernameTo);
                            if(checkForUser==false){
                                System.out.println("No such user exists, returning to main menu");
                                break;
                            }
                            List<String> chosenCustomer = dao.viewCustomer(usernameTo);
                            if(chosenCustomer.isEmpty()){
                                System.out.println("No such user name exists");
                                break;
                            }
                            int customerIdTo = Integer.parseInt(chosenCustomer.get(0));
                            List<Account> accountsPossible = dao.allAccounts(customerIdTo);
                            if(accountsPossible.isEmpty()){
                                System.out.println("This person has no active accounts.");
                                break;
                            }
                            for(int i = 1; i <= STAR_COUNTER; i++) System.out.print("*");
                            System.out.println();

                            int q = 1;
                            for(Account c:accountsPossible){
                                System.out.println("Enter " + q + " to transfer to account "+c.getActId());
                                q++;
                            }
                            for(int i = 1; i <= STAR_COUNTER; i++) System.out.print("*");
                            System.out.println();
                            int accountInd = scanner.nextInt();
                            Account accountTo = accountsPossible.get(accountInd-1);
                            System.out.print("Enter the amount you want to transfer: ");
                            double transferredAmount = scanner.nextDouble();
                            if (transferredAmount <= 0){
                                System.out.println("Negative amount is invalid. Transaction did not occur.");
                                break;
                            }

                            //Implemented transfer using a withdraw and then a deposit method
                            // If withdraw does not go thru, neither will deposit
                            boolean allowableTransfer = dao.personalActions(accountFrom, transferredAmount, false, accountFrom.getActId());
                            if (allowableTransfer){
                                dao.personalActions(accountTo, transferredAmount, true, accountTo.getActId());
                                dao.insertTransaction(accountFrom.getActId(), accountTo.getActId(), transferredAmount);
                            }
                            break;
                        case "5":
                            System.out.println("Exiting...");
                            customerFlag = false;
                            break;
                        default:
                            System.out.println("Please enter a valid number!");

                    }
                }

                break;
            default:
                System.out.println("Please choose either 1 or 2!");
        }
    }
}
