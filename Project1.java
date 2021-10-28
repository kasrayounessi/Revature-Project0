package com.company;

import java.util.LinkedList;



class User{
    public void login(){
        System.out.println("user logged in!");
    }
}

class Account{
    private double balance;
    public Account(double balance){
        this.balance = balance;
        System.out.println("Called the constructor of Account...");
    }
    public double getBalance(){
        return balance;
    }

    public void withdraw(double amount){
        if (amount < 0){
            System.out.println("You may not enter negative amount");
            return;
        }
        if(balance >= amount) {
            balance -= amount;
            System.out.println("You have withdrawn " + amount);
        }else
            System.out.println("Not enough funds for the withdrawal");

    }
    public void deposit(double amount){
        if (amount < 0){
            System.out.println("You may not enter negative amount");
            return;
        }
        balance += amount;
        System.out.println("You have deposited " + amount);

    }
}

class Customer{
    LinkedList accounts = null;

    public void newBank(double balance){
        if (accounts.isEmpty())
            accounts = new LinkedList();
        accounts.add(new Account(100.00));
        System.out.println("customer wants to start a new account with $" + balance);
    }

    public void checkBalance(Account account){
        System.out.println("Current balance: "+ account.getBalance());
    }

    public void withdraw(Account account, double amount){
        account.withdraw(amount);

    }

    public void deposit(Account account, double amount){
        account.deposit(amount);
    }

    public void transferMoney(Account accountWithdrawn, Account accountDeposited){
        //call withdraw that would return a value
        // call deposit for the amount that was registered to the accountDeposited
        // need an accept method that would most likely need to connect
    }
}

class Employee {
    public void approve(boolean approvedAccount) {
        if (approvedAccount)
            System.out.println("Account approved...");
        else
            System.out.println("Account rejected...");

    }
    public void checkAccounts(){
        System.out.println("Employee checking accounts (needs to be implemented)");
    }

}

public class Project1 {
    public static void main(String[] args) {


    }
}
