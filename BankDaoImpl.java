package com.company;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BankDaoImpl implements BankDao{

    Connection connection;

    public BankDaoImpl(){
        this.connection = ConnectionFactory.getConnection();
    }

    @Override
    // this method receives the username, checks the database for such username, and then returns the customer info as a list
    public List<String> viewCustomer(String userName) throws SQLException {
        List<String> customerInfo = new ArrayList<>();
        String query = "select * from customer WHERE username = '" + userName + "'";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        resultSet.next();
        if(resultSet != null){
            customerInfo.add(Integer.toString(resultSet.getInt(1)));
            customerInfo.add(resultSet.getString(2));
            customerInfo.add(resultSet.getString(3));
            customerInfo.add(resultSet.getString(4));
            customerInfo.add(resultSet.getString(5));
        } else{
            System.out.println("No record found");
        }
        return customerInfo;
    }

    @Override
    // this method adds customers by receiving a customer object that provides first and last name, username, and password
    public void addCustomer(Customer customer) throws SQLException {
        String sql = "insert into customer (first_name, last_name, username, password) values (?,?,?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, customer.getFirstName());
        preparedStatement.setString(2, customer.getLastName());
        preparedStatement.setString(3, customer.getUserName());
        preparedStatement.setString(4, customer.getPassword());
        int count = preparedStatement.executeUpdate();
        if (count > 0)
            System.out.println("Welcome dear " + customer.getFirstName() + " " + customer.getLastName());
        else
            System.out.println("Oops, something went wrong!");
    }

    @Override
    // quicker way to check for a customer
    public boolean isExistingCustomer(String username) throws SQLException {
        String query = "select * from customer where username = '"+ username+"'";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        if(resultSet.next())
            return true;
        else
            return false;
    }

    @Override
    //Adds accounts to the account table
    public void addAccount(Account account) throws SQLException {
        String query = "insert into account (balance, customer_id) values (?,?)";
        System.out.println("Customer ID: " + account.getCustId());
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setDouble(1, account.getBalance());
        preparedStatement.setInt(2, account.getCustId());
        int count = preparedStatement.executeUpdate();
        if(count > 0)
            System.out.println("Account added successfully!");
        else
            System.out.println("Oops, something went wrong.");

    }

    @Override
    // It would be deposit or withdraw. If deposit == true, then it will be a deposit, else it will be a withdrawal
    // Withdraw method first checks whether we have sufficient balance to do the transaction, if not possible, it will return false
    public boolean personalActions(Account account, double amount, boolean deposit, int actIdx) throws SQLException {
        String query = "update account set balance = ? WHERE (id = ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        if(deposit) {
            preparedStatement.setDouble(1, account.getBalance() + amount);
            preparedStatement.setInt(2, actIdx);
            System.out.println("Amount deposited successfully");
            int count = preparedStatement.executeUpdate();
            if(count > 0){
                System.out.println("Updated successfully!");
            } else{
                System.out.println("Oops, something went wrong. please try again!");
            }
            return true;
            //return -1;
        }

        else {
            if(account.getBalance() >= amount) {
                preparedStatement.setDouble(1, account.getBalance() - amount);
                preparedStatement.setInt(2, actIdx);
                System.out.println("Amount withdrawn successfully");
                int count = preparedStatement.executeUpdate();
                if(count > 0){
                    System.out.println("Updated successfully!");
                    return true;
                } else{
                    System.out.println("Oops, something went wrong. please try again!");
                    return false;
                }
                //return amount;
            }
            else{
                System.out.println("Not enough funds!");
                return false;
                //return -2;
            }

        }

    }

    @Override
    // List of all verified accounts that a customer has
    public List<Account> allAccounts(int custId) throws SQLException {
        List<Account> accounts = new ArrayList<>();
        String query = "select * from account where customer_id = "+ custId + " and verified = true";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        while(resultSet.next()){
            //System.out.println("Account ID: " + resultSet.getInt(1));
            Account account = new Account(custId, resultSet.getDouble(2));
            account.setActId(resultSet.getInt(1));
            accounts.add(account);
        }
        return accounts;

    }



    @Override
    // Employee can reject accounts through this
    public void deleteAccount(int actId) throws SQLException {
        String query = "delete from account where id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, actId);
        int count = preparedStatement.executeUpdate();
        if(count>0)
            System.out.println("Account " + actId + " successfully deleted");
        else
            System.out.println("Oops, something went wrong");

    }

    @Override
    // list of all the unverified accounts that the employee needs to approve or reject
    public List<Account> getUnverifiedAccounts() throws SQLException {
        List<Account> unverifiedAccounts = new ArrayList<>();
        String query = "select * from account where verified = "+false;
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        while(resultSet.next()){
            Account account = new Account(resultSet.getInt(3), resultSet.getDouble(2));
            account.setActId(resultSet.getInt(1));
            unverifiedAccounts.add(account);
        }

        return unverifiedAccounts;
    }

    @Override
    // Employee can verify accounts through this
    public void verifyAccount(int actId) throws SQLException {
        String query = "update account set verified = true where id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, actId);
        int count = preparedStatement.executeUpdate();
        if(count > 0)
            System.out.println("Account " + actId + " verified!");
        else
            System.out.println("Oops, something went wrong!");

    }

    @Override
    // this method inserts deposits and withdrawals inside the transaction table
    public void insertDepOrWit(int actId, double balance, boolean deposit) throws SQLException {
        String type;
        if (deposit)
            type = "deposit";
        else
            type = "withdraw";
        String query = "insert into transaction (act_id_from, balance, type) values (?,?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, actId);
        preparedStatement.setDouble(2, balance);
        preparedStatement.setString(3, type);
        int count = preparedStatement.executeUpdate();
        if(count>0)
            System.out.println("Withdraw inserted successfully");
        else
            System.out.println("Oops, transaction not inserted");


    }

    @Override
    public void insertTransaction(int actIdFrom, int actIdTo, double balance) throws SQLException {
        // this method inserts the transfers inside the transaction table
        String query = "insert into transaction (act_id_from, act_id_to, balance, type) values (?,?,?,?);";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1,actIdFrom);
        preparedStatement.setInt(2, actIdTo);
        preparedStatement.setDouble(3, balance);
        preparedStatement.setString(4, "transfer");
        int count = preparedStatement.executeUpdate();
        if(count>0)
            System.out.println("Transaction inserted");
        else
            System.out.println("Oops,something went wrong");

    }

    @Override
    public void showTransActions() throws SQLException {
        // a method for the employee to view all transactions
        String query = "select * from transaction";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        int i = 1;
        while(resultSet.next()){
            if(resultSet.getString(5).equals("deposit"))
                System.out.println("Transaction " + i + ", account "+resultSet.getInt(2)+ " deposited $" + resultSet.getDouble(4) + " to itself." );
            else if(resultSet.getString(5).equals("withdraw"))
                System.out.println("Transaction " + i + ", account "+resultSet.getInt(2)+ " withdrew $" + resultSet.getDouble(4) + " from itself." );
            else
                System.out.println("Transaction "+i+": account "+resultSet.getInt(2)+ " transferred $"+resultSet.getDouble(4)+" to account "+resultSet.getInt(3));


            i++;
        }

    }
}