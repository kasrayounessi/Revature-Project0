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
    public void addAccount(Account account) throws SQLException {
        String query = "insert into account (balance, customer_id) values (?,?)";
        System.out.println("Account ID: " + account.getCustId());
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
    public void personalActions(Account account, double amount, boolean deposit, int actIdx) throws SQLException {


        String query = "update account set balance = ? WHERE (id = ?)";
        //System.out.println("Account ID: " + actIdx);
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
                } else{
                    System.out.println("Oops, something went wrong. please try again!");
                }
                //return amount;
            }
            else{
                System.out.println("Not enough funds!");
                //return -2;
            }

        }

    }

    @Override
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
    public void insertTransaction(int actIdFrom, int actIdTo, double balance) throws SQLException {
        String query = "insert into transaction (account_from, account_to, balance) values (?,?,?);";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1,actIdFrom);
        preparedStatement.setInt(2, actIdTo);
        preparedStatement.setDouble(3, balance);
        int count = preparedStatement.executeUpdate();
        if(count>0)
            System.out.println("Transaction inserted");
        else
            System.out.println("Oops,something went wrong");

    }

    @Override
    public void showTransActions() throws SQLException {
        String query = "select * from transaction";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        int i = 1;
        while(resultSet.next()){
            System.out.println("Transaction "+i+": $"+resultSet.getDouble(3)+" transferred from account "+resultSet.getInt(1)+ " to account "+resultSet.getInt(2));
            i++;
        }

    }
}