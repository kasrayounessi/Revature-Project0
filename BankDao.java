package com.company;

import java.sql.SQLException;
import java.util.List;

public interface BankDao {

    List<String> viewCustomer(String userName) throws SQLException;

    int addCustomer(Customer customer) throws SQLException;

    void addAccount(Account account) throws SQLException;

    List<Account> getAccounts(int custId) throws SQLException;

    void personalActions(Account account, double amount, boolean deposit, int actIdx) throws SQLException;

    void transferMoney(Account account, double amount);

    List<Account> allAccounts(int custId) throws SQLException;

    void addEmployee(Employee employee);

    void deleteAccount(int actID);


}
