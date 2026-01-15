package org.isep.javaprojectarchusers.Accounts;

import org.isep.javaprojectarchusers.Portfolio;

/**
 * The {@code Account} superclass represent bank accounts.
 * <p>
 *     It contains the majors methods to interact with the account.<br>
 *     It has subclass that has different additional functions.
 * </p>
 *
 */
public class Account {
    /** Name of the user of this account. */
    private String userName;

    /** The {@link Portfolio} object the account is in. */
    private final Portfolio portfolio;


    /** Amount of money this account has. */
    private double balance;

    /** Type of the account */
    private final AccountType accountType;

    /**I don't know what this is*/
    final float OVERDRAW_LIMIT; //moved here cause present in both subclass


    /** Temporary constructor
     * @param userName Name of the user.
     * @param accountType The type of the account.
     * @param OVERDRAW_LIMIT I don't know what is this supposed to be.
     * @param balance initial account balance.
     * @param portfolio The {@link Portfolio} object to this account.
     */
    public Account(String userName,AccountType accountType, float OVERDRAW_LIMIT, double balance, Portfolio portfolio) {
        this.userName = userName;
        this.OVERDRAW_LIMIT = OVERDRAW_LIMIT;
        this.balance = balance;
        this.portfolio = portfolio;
        this.accountType = accountType;
    }


    /** Get the balance of the account.
     * @return balance
     */
    public double getBalance(){
        return balance;
    }



    /** Function to withdraw money from this account.
     *
     * @param amount amount of money to withdraw; must not be {@code null}
     * @return Tells if withdraw has been successful or not.
     */
    public boolean withdraw(double amount){
        if(amount < balance){
            balance -= amount;
            return true;
        }
        else return false;
    }



    /** Add the amount specified into the account.
     *
     * @param amount Amount of money to add.
     */
    public void deposit(double amount){
        balance += amount;
    }



    /**
     * @return The {@link Portfolio} object this account is present in.
     */
    public Portfolio getPortfolio() {
        return portfolio;
    }

    /**
     * @return userName of the account
     */
    public String getUserName() {
        return userName;
    }

    /**Custom toString
     * @return "{@code userName} : {@code accountType}"
     */
    @Override
    public String toString(){
        return this.userName + " : " + accountType;
    }
}
