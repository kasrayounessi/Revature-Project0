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
                    System.out.println("Enter 3 to withdraw money");
                    System.out.println("Enter 4 to deposit money");
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
                            dao.getAccounts(customerId);
                            break;
                        case "3":
                            System.out.println("withdrawing money ...");
                            break;
                        case "4":
                            System.out.println("depositing money...");
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






