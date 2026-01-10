package org.isep.javaprojectarchusers;

public class Account {
    private String userName;
    private Portfolio portfolio;
    private double balance;
    private static final String accountType = "account";

    public double getBalance(){
        return balance;
    }

    public boolean withdraw(double amount){
        if(amount < balance){
            balance -= amount;
            return true;
        }
        else return false;
    }

    public void deposit(double amount){
        balance += amount;
    }

    public Portfolio getPortfolio() {
        return portfolio;
    }

    @Override
    public String toString(){
        return this.userName + " : " + accountType;
    }
}
