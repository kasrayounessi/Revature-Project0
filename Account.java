package com.company;

public class Account {
    private int custId;
    private double balance;
    private int actId;

    public Account(int custId, double balance) {
        this.custId = custId;
        this.balance = balance;
        //this.actId = actId;
    }

    public int getCustId() {
        return custId;
    }

    public double getBalance() {
        return balance;
    }

    public int getActId() {
        return actId;
    }



    public void setCustId(int custId) {
        this.custId = custId;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void setActId(int actId) {
        this.actId = actId;
    }




}

