package com.company;

public class BankDaoFactory {
    private static BankDao dao;

    private BankDaoFactory(){}

    public static BankDao getBankDao(){
        if(dao==null){
            dao = new BankDaoImpl();
        }
        return dao;
    }
}
