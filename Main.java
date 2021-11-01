package com.company;

import java.util.List;
import java.util.Scanner;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) throws SQLException {
        BankDao dao = BankDaoFactory.getBankDao();
        Scanner scanner = new Scanner(System.in);
        final int STAR_COUNTER = 40;
        final int SPACE_COUNTER = 5;
        String username;
        String firstName;
        String lastName;
        String password;
        List customerInfo;
        int customerId;
        //List<Account> accounts;


        System.out.println("Welcome.");
        System.out.println("ENTER 1 if you are an employee");
        System.out.println("ENTER 2 if you are a customer");
        for(int i = 1; i <= STAR_COUNTER; i++) System.out.print("*");
        System.out.println();
        String universalInd = scanner.next();

        switch(universalInd){
            case "1":
                for (int i = 0; i < SPACE_COUNTER; ++i) System.out.println();
                System.out.println("Welcome dear employee!");
                boolean employeeFlag = true;
                while(employeeFlag) {
                    for(int i = 1; i <= STAR_COUNTER; i++) System.out.print("*");
                    System.out.println("Choose an option!");
                }
                break;
            case "2":
                for (int i = 1; i <= SPACE_COUNTER; ++i) System.out.println();
                System.out.println("Welcome dear customer");
                for(int i = 1; i <= STAR_COUNTER; i++) System.out.print("*");
                System.out.println();
                System.out.println("Enter 1 if you are an existing customer");
                System.out.println("Enter 2 if you are a new customer");
                System.out.println("Enter any other button to exit");
                for(int i = 1; i <= STAR_COUNTER; i++) System.out.print("*");
                System.out.println();
                String customerInd = scanner.next();

                switch(customerInd){
                    case "1":
                        System.out.print("Username: ");
                        username = scanner.next();
                        customerInfo = dao.viewCustomer(username);
                        customerId = Integer.parseInt((String) customerInfo.get(0));

                        boolean userPassed = false;
                        if(customerInfo.isEmpty()){
                            System.out.println("Invalid username, exiting ...");
                            return;
                        }

                        int errors = 0;
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
                        System.out.print("Enter your first name: ");
                        firstName = scanner.next();
                        System.out.print("Enter your last name: ");
                        lastName = scanner.next();
                        System.out.print("Enter your username: ");
                        username = scanner.next();
                        System.out.print("Enter your password: ");
                        password = scanner.next();
                        Customer customer = new Customer(firstName, lastName, username, password);
                        dao.addCustomer(customer);
                        customerId = Integer.parseInt(dao.viewCustomer(username).get(0));
                        break;

                    default:
                        System.out.println("Such username does not exist, exiting...");
                        return;
                }

                boolean customerFlag = true;
                while(customerFlag){

                    for(int i = 1; i <= STAR_COUNTER; i++) System.out.print("*");
                    System.out.println();
                    System.out.println("Choose an option!");
                    System.out.println("Enter 1 to create a new account");
                    System.out.println("Enter 2 to view balances of your accounts");
                    System.out.println("Enter 3 to deposit/withdraw money");
                    System.out.println("Enter 4 to transfer money");
                    System.out.println("Enter 5 to exit");
                    for(int i = 1; i <= STAR_COUNTER; i++) System.out.print("*");
                    System.out.println();
                    String customerMenuInd = scanner.next();

                    switch(customerMenuInd){
                        case "1":
                            System.out.print("Enter the balance: ");
                            double balance = scanner.nextDouble();
                            Account account = new Account(customerId, balance);
                            dao.addAccount(account);
                            break;
                        case "2":
                            List<Account> accounts = dao.allAccounts(customerId);
                            int y = 1;
                            for(int i = 1; i <= STAR_COUNTER; i++) System.out.print("*");
                            System.out.println();
                            for(Account a:accounts){
                                System.out.println("Account " + y + ": $"+a.getBalance());
                                y++;
                            }
                            for(int i = 1; i <= STAR_COUNTER; i++) System.out.print("*");
                            System.out.println();
                            break;
                        case "3":
                            accounts = dao.allAccounts(customerId);
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
                                System.out.println("Account "+z+": "+a.getBalance());
                                z++;
                            }
                            for(int i = 1; i <= STAR_COUNTER; i++) System.out.print("*");
                            System.out.println();

                            System.out.print("Enter the number of your account: ");
                            int idx = scanner.nextInt()-1;
                            int accountIdx = accounts.get(idx).getActId();

                            System.out.print("Enter the amount: ");
                            double amountChosen = scanner.nextDouble();
                            dao.personalActions(accounts.get(idx), amountChosen, deposit, accountIdx);
                            break;
                        case "4":
                            System.out.println("Select which account you want to transfer from");
                            List<Account> myAccount = dao.allAccounts(customerId);
                            int w = 1;
                            for(Account d:myAccount){
                                System.out.println("Account "+w+", balance: $"+d.getBalance());
                            }
                            int myAccountIdx = scanner.nextInt();
                            Account accountFrom = myAccount.get(myAccountIdx-1);
                            System.out.print("Enter the username you want to transfer to: ");
                            String usernameTo =  scanner.next();
                            List<String> chosenCustomer = dao.viewCustomer(usernameTo);
                            if(chosenCustomer.isEmpty()){
                                System.out.println("No such user name exists");
                                break;
                            }
                            int customerIdTo = Integer.parseInt(chosenCustomer.get(0));
                            List<Account> accountsPossible = dao.allAccounts(customerIdTo);
                            for(int i = 1; i <= STAR_COUNTER; i++) System.out.print("*");
                            System.out.println();
                            System.out.println("Enter the number of the account you want to transfer to.");
                            int q = 1;
                            for(Account c:accountsPossible){
                                System.out.println("Account "+q);
                                q++;
                            }
                            for(int i = 1; i <= STAR_COUNTER; i++) System.out.print("*");
                            System.out.println();
                            int accountInd = scanner.nextInt();
                            Account accountTo = accountsPossible.get(accountInd-1);
                            System.out.print("Enter the amount you want to transfer: ");
                            double transferredAmount = scanner.nextDouble();
                            dao.personalActions(accountFrom, transferredAmount, false, accountFrom.getActId());
                            dao.personalActions(accountTo, transferredAmount, true, accountTo.getActId());
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
