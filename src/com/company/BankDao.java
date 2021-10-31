package com.company;

import java.sql.SQLException;
import java.util.List;

public interface BankDao {

    List<String> viewCustomer(String userName) throws SQLException;

    int addCustomer(Customer customer) throws SQLException;

    void addAccount(Account account) throws SQLException;

    void getAccounts(int custId) throws SQLException;

    void depositMoney(Account account, double amount);

    void transferMoney(Account account, double amount);

    void addEmployee(Employee employee);

    void deleteAccount(int actID);


}
