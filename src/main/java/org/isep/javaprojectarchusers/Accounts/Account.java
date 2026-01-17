package org.isep.javaprojectarchusers.Accounts;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.isep.javaprojectarchusers.Portfolio;

import java.util.ArrayList;

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
    private String portfolio;


    /** Amount of money this account has. */
    private double balance;

    /** Type of the account */
    private AccountType accountType;

    /**I don't know what this is*/
    float OVERDRAW_LIMIT; //moved here cause present in both subclass

    private static ArrayList<Account> accountArrayList = new ArrayList<>();


    /** Temporary constructor
     * @param userName Name of the user.
     * @param accountType The type of the account.
     * @param OVERDRAW_LIMIT I don't know what is this supposed to be.
     * @param balance initial account balance.
     * @param portfolioAddress The address of the {@link Portfolio} object linked to this account.
     */
    public Account(@JsonProperty("userName") String userName, @JsonProperty("accountType") AccountType accountType, @JsonProperty("OVERDRAW_LIMIT") float OVERDRAW_LIMIT, @JsonProperty("balance") double balance, @JsonProperty("portfolio") String portfolioAddress) {
        this.userName = userName;
        this.OVERDRAW_LIMIT = OVERDRAW_LIMIT;
        this.balance = balance;
        this.portfolio = portfolioAddress;
        this.accountType = accountType;
        accountArrayList.add(this);
    }

    public void setOVERDRAW_LIMIT(float OVERDRAW_LIMIT) {
        this.OVERDRAW_LIMIT = OVERDRAW_LIMIT;
    }

    /** Get the balance of the account.
     * @return balance
     */
    public double getBalance(){
        return balance;
    }



    /**
     * Tente de retirer de l'argent du compte.
     * @param amount Le montant à retirer.
     * @return true si succès, false si fonds insuffisants.
     */
    public boolean withdraw(double amount) {
        // On ajoute une petite tolérance (epsilon) pour corriger les bugs de précision
        // Si la différence est minuscule (0.001), on considère que c'est bon.
        if (amount > balance + 0.001) {
            return false; // Vraiment pas assez d'argent
        }

        balance -= amount;

        // Sécurité : Si le solde devient négatif à cause de la tolérance (ex: -0.00001), on le remet à 0
        if (balance < 0) {
            balance = 0.0;
        }

        return true;
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
    public String getPortfolio() {
        return portfolio;
    }


    public void setPortfolio(String portfolio){
        this.portfolio = portfolio;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public float getOVERDRAW_LIMIT() {
        return OVERDRAW_LIMIT;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }


    /**Custom toString
     * @return "{@code userName} : {@code accountType}"
     */
    @Override
    public String toString(){
        return this.userName + " : " + accountType;
    }
}
