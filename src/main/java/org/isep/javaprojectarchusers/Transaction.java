package org.isep.javaprojectarchusers;

import java.util.ArrayList;

public class Transaction {
    private Portfolio emitter;
    private Portfolio receiver;
    private Account emitterAccount;
    private Account receiverAccount;
    private Asset transactionAsset;
    private double amountOfMoney;
    private static ArrayList<Transaction> transactionList = new ArrayList<>();

    public Transaction(Portfolio emitter, Portfolio receiver, Account emitterAccount, Account receiverAccount, double amountOfMoney){
        this.emitter = emitter;
        this.receiver = receiver;
        this.emitterAccount = emitterAccount;
        this.receiverAccount = receiverAccount;
        this.transactionAsset = null;
        this.amountOfMoney = amountOfMoney;
    }

    public Transaction(Account emitterAccount, Asset transactionAsset){
        this.emitterAccount = emitterAccount;
        this.receiverAccount = null;
        this.transactionAsset = transactionAsset;

    }

    public Transaction(Asset transactionAsset, Account receiverAccount){
        this.emitterAccount = null;
        this.receiverAccount = receiverAccount;
        this.transactionAsset = transactionAsset;
    }

    public boolean validateTransaction(){
        if(emitterAccount == null) return true;
        else {
            boolean checkWithdraw = emitterAccount.withdraw(amountOfMoney);
            if(checkWithdraw && receiverAccount != null) receiverAccount.deposit(amountOfMoney);
            return checkWithdraw;
        }
    }

    public String getInfo(){
        return emitter.toString()+","+receiver.toString()+","+emitterAccount.toString()+","+receiverAccount.toString()+","+transactionAsset.toString()+","+amountOfMoney+"\n";
    }
}
