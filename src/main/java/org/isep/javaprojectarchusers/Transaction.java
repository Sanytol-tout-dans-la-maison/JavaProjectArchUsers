package org.isep.javaprojectarchusers;

import org.isep.javaprojectarchusers.Accounts.Account;

import java.util.ArrayList;

public class Transaction {
    private Portfolio emitter;
    private Portfolio receiver;
    private Account emitterAccount;
    private Account receiverAccount;
    private Asset transactionAsset;
    private double amountOfMoney;
    private boolean isAccepted = false;
    private static ArrayList<Transaction> transactionList = new ArrayList<>();

    public Transaction(Portfolio emitter, Portfolio receiver, Account emitterAccount, Account receiverAccount, double amountOfMoney){
        this.emitter = emitter;
        this.receiver = receiver;
        this.emitterAccount = emitterAccount;
        this.receiverAccount = receiverAccount;
        this.transactionAsset = null;
        this.amountOfMoney = amountOfMoney;
        addToBlockchain();
    }

    public Transaction(Portfolio emitter, Account emitterAccount, Asset transactionAsset, double amountOfMoney){
        this.emitterAccount = emitterAccount;
        this.receiverAccount = null;
        this.transactionAsset = transactionAsset;
        addToBlockchain();
    }

    public Transaction(Portfolio receiver, Asset transactionAsset, Account receiverAccount, double amountOfMoney){
        this.emitterAccount = null;
        this.receiverAccount = receiverAccount;
        this.transactionAsset = transactionAsset;
        addToBlockchain();
    }

    public boolean validateTransaction(){
        if(emitterAccount == null){
            isAccepted = true;
            return true;
        }
        else {
            boolean checkWithdraw = emitterAccount.withdraw(amountOfMoney);
            if(checkWithdraw && receiverAccount != null) receiverAccount.deposit(amountOfMoney);
            isAccepted = checkWithdraw;
            return checkWithdraw;
        }
    }

    public String getInfo(){
        return emitter.toString()+","+receiver.toString()+","+emitterAccount.toString()+","+receiverAccount.toString()+","+transactionAsset.toString()+","+amountOfMoney+"\n";
    }

    public void addToBlockchain(){
        Blockchain.getLast().add(this);
    }
}
