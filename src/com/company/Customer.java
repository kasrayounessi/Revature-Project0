package com.company;

public class Customer {
    private int id;
    private String fName;
    private String lName;
    private String userName;
    private String password;

    public Customer(String fName, String lName, String userName, String password) {
        this.fName = fName;
        this.lName = lName;
        this.userName = userName;
        this.password = password;
    }

    public Customer(){}

    public String getFirstName() {
        return fName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLastName() {
        return lName;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public void setFirstName(String fName) {
        this.fName = fName;
    }

    public void setLastName(String lName) {
        this.lName = lName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
