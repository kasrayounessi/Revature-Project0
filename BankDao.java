package com.company;

import java.sql.SQLException;
import java.util.List;

public interface BankDao {
    List<String> viewCustomer(String userName) throws SQLException;

    void addCustomer(Customer customer) throws SQLException;

    boolean isExistingCustomer(String username) throws SQLException;

    void addAccount(Account account) throws SQLException;

    boolean personalActions(Account account, double amount, boolean deposit, int actIdx) throws SQLException;

    List<Account> allAccounts(int custId) throws SQLException;

    void deleteAccount(int actID) throws SQLException;

    List<Account> getUnverifiedAccounts() throws SQLException;

    void verifyAccount(int actId) throws SQLException;

    void insertDepOrWit(int actId, double balance, boolean deposit) throws SQLException;

    void insertTransaction(int actIdFrom, int actIdTo, double balance) throws SQLException;

    void showTransActions() throws SQLException;
}
