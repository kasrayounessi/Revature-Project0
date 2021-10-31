package com.company;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws SQLException {
        BankDao dao = BankDaoFactory.getBankDao();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome.");
        System.out.println("ENTER 1 if you are an employee");
        System.out.println("ENTER 2 if you are a customer");
        System.out.println("*********************************");
        String universalInd = scanner.next();

        switch(universalInd){
            case "1":
                for (int i = 0; i < 5; ++i) System.out.println();
                System.out.println("Welcome dear employee!");
                boolean employeeFlag = true;
                while(employeeFlag) {
                    System.out.println("**************************************");
                    System.out.println("Choose an option!");
                }
                break;
            case "2":
                for (int i = 0; i < 5; ++i) System.out.println();
                System.out.println("Welcome dear customer");
                System.out.println("Enter 1 if you are an existing customer");
                System.out.println("Enter 2 if you are a new customer");
                System.out.println("Enter any other button to exit");
                String customerInd = scanner.next();
                int customerId = 0;
                switch(customerInd){

                    case "1":
                        System.out.print("Username: ");
                        String userNameE = scanner.next();
                        List custInfo = dao.viewCustomer(userNameE);
                        customerId = (int) custInfo.get(0);
                        boolean userPassed = false;
                        if(custInfo.isEmpty()){
                            System.out.println("Invalid username, exiting ...");
                            return;
                        }

                        while(!userPassed){
                            System.out.print("Password: ");
                            String passwordE = scanner.next();
                            System.out.println(passwordE);

                            if(passwordE.equals(custInfo.get(4)))
                                userPassed = true;
                            else
                                System.out.println("Wrong password!");
                        }
                        System.out.println("Welcome " + custInfo.get(2) + " " + custInfo.get(3));
                        break;
                    case "2":
                        System.out.print("Enter your first name: ");
                        String fName = scanner.next();
                        System.out.print("Enter your last name: ");
                        String lName = scanner.next();
                        System.out.print("Enter your username: ");
                        String userNameN = scanner.next();
                        System.out.print("Enter your password: ");
                        String passwordN = scanner.next();
                        Customer customer = new Customer(fName, lName, userNameN, passwordN);
                        customerId = dao.addCustomer(customer);
                        break;

                    default:
                        System.out.println("Exiting...");
                        return;
                }

                boolean customerFlag = true;
                while(customerFlag){

                    System.out.println("**************************************");
                    System.out.println("Choose an option!");
                    System.out.println("Enter 1 to create a new account");
                    System.out.println("Enter 2 to view balances of your accounts");
                    System.out.println("Enter 3 to deposit/withdraw money");
                    System.out.println("Enter 5 to transfer money");
                    System.out.println("Enter 6 to receive money");
                    System.out.println("Enter 7 to exit");
                    System.out.println("**************************************");
                    String customerMenuInd = scanner.next();
                    switch(customerMenuInd){
                        case "1":
                            System.out.println("Include the cents amount as well!");
                            System.out.print("Enter the balance: ");
                            double balance = scanner.nextDouble();
                            Account account = new Account(customerId, balance);
                            dao.addAccount(account);
                            break;
                        case "2":
                            List<Account> accounts = dao.getAccounts(customerId);
                            int y = 1;
                            for(Account a:accounts){
                                System.out.println("Account " + y + ": $"+a.getBalance());
                                y++;
                            }
                            break;
                        case "3":
                            List<Account> accountsAvailable = dao.getAccounts(customerId);
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
                            for(Account a:accountsAvailable){
                                System.out.println("Account "+z+": "+a.getBalance());
                                z++;
                            }

                            //int firstRecord = dao.getFirstRecordIndex(customerId);
                            List<Account> allAccounts = dao.allAccounts(customerId);
                            System.out.print("Enter the number of your account: ");
                            int idx = scanner.nextInt()-1;
                            Account desiredAccount = allAccounts.get(idx);
                            int accountIdx = desiredAccount.getActId();
                            //int accountIdx = firstRecord + incrementer;

                            System.out.print("Enter the amount: ");
                            double amountChosen = scanner.nextDouble();
                            System.out.println("Final Account ID: " + accountIdx);
                            dao.personalActions(accountsAvailable.get(idx), amountChosen, deposit, accountIdx);

                            break;
                        case "5":
                            System.out.println("Transferring money...");
                            break;
                        case "6":
                            System.out.println("Receiving money...");
                            break;
                        case "7":
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
        /*
        try{
            int universalInd = scanner.nextInt();
        }
        catch(((Object)a).getClass().getName()!= "java.lang.Integer"){

         */






