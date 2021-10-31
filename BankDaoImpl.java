package com.company;

import com.mysql.cj.x.protobuf.MysqlxPrepare;

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
    public int addCustomer(Customer customer) throws SQLException {
        String sql = "insert into customer (first_name, last_name, username, password) values (?,?,?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, customer.getFirstName());
        preparedStatement.setString(2, customer.getLastName());
        preparedStatement.setString(3, customer.getUserName());
        preparedStatement.setString(4, customer.getPassword());
        int count = preparedStatement.executeUpdate();
        if(count>0)
            System.out.println("Welcome dear "+customer.getFirstName() + " " + customer.getLastName());
            Statement statement = connection.createStatement();
            String query = "select id from customer WHERE first_name = '" + customer.getFirstName() + "' AND last_name = '" + customer.getLastName() + "'";
            ResultSet resultSet = statement.executeQuery(query);
            resultSet.next();
            if(resultSet != null) {
                System.out.println("Your ID: " + resultSet.getInt(1));
                return resultSet.getInt(1);
            }
        else {
                System.out.println("Oops, something went wrong!");
                return 0;
            }


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
    public List<Account> getAccounts(int custId) throws SQLException {
        List<Account> accounts = new ArrayList<>();
        String query = "select * from account WHERE customer_id = "+custId;
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        while(resultSet.next()){
            accounts.add(new Account(custId, resultSet.getDouble(2)));
        }
        return accounts;

    }

    @Override
    public void personalActions(Account account, double amount, boolean deposit, int actIdx) throws SQLException {


        String query = "update account set balance = ? WHERE (id = ?)";
        System.out.println("Account ID: " + actIdx);
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        if(deposit) {
            preparedStatement.setDouble(1, account.getBalance() + amount);
            preparedStatement.setInt(2, actIdx);
            System.out.println("Amount deposited successfully");
        }

        else {
            if(account.getBalance() >= amount) {
                preparedStatement.setDouble(1, account.getBalance() - amount);
                preparedStatement.setInt(2, actIdx);
                System.out.println("Amount withdrawn successfully");
            }
            else{
                System.out.println("Not enough funds!");
                return;
            }

        }
        int count = preparedStatement.executeUpdate();
        if(count > 0){
            System.out.println("Updated successfully!");
        } else{
            System.out.println("Oops, something went wrong. please try again!");
        }
    }


    @Override
    public void transferMoney(Account account, double amount) {



    }

    @Override
    public List<Account> allAccounts(int custId) throws SQLException {
        List<Account> accounts = new ArrayList<>();
        String query = "select * from account where customer_id = "+ custId;
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        while(resultSet.next()){
            System.out.println("Account ID: " + resultSet.getInt(1));
            Account account = new Account(custId, resultSet.getDouble(2));
            account.setActId(resultSet.getInt(1));
            accounts.add(account);
        }
        return accounts;

    }

    @Override
    public void addEmployee(Employee employee) {

    }

    @Override
    public void deleteAccount(int actID) {

    }
}
