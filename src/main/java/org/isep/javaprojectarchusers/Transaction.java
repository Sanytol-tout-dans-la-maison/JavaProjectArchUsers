package org.isep.javaprojectarchusers;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.isep.javaprojectarchusers.Accounts.Account;

import java.util.ArrayList;

public class Transaction {
    @JsonProperty("emitter")
    private String emitter;
    @JsonProperty("receiver")
    private String receiver;
    @JsonProperty("emitterAccount")
    private String emitterAccount;
    @JsonProperty("receiverAccount")
    private String receiverAccount;
    @JsonProperty("transactionAsset")
    private Asset transactionAsset;
    @JsonProperty("amountOfMoney")
    private double amountOfMoney;
    @JsonProperty("isAccepted")
    private boolean isAccepted = false;
    @JsonIgnore
    private static ArrayList<Transaction> transactionList = new ArrayList<>();


    public Transaction(@JsonProperty("emitter") String emitter, @JsonProperty("receiver") String receiver, @JsonProperty("emitterAccount") String emitterAccount,@JsonProperty("receiverAccount") String receiverAccount, @JsonProperty("transactionAsset") Asset transactionAsset, @JsonProperty("amountOfMoney")double amountOfMoney, @JsonProperty("isAccepted")boolean isAccepted){
        this.emitter = emitter;
        this.receiver = receiver;
        this.emitterAccount = emitterAccount;
        this.receiverAccount = receiverAccount;
        this.transactionAsset = null;
        this.amountOfMoney = amountOfMoney;
        this.transactionAsset = transactionAsset;
        this.isAccepted = isAccepted;
        addToBlockchain();
    }



    public String getEmitter() {
        return emitter;
    }

    public void setEmitter(String emitter) {
        this.emitter = emitter;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getEmitterAccount() {
        return emitterAccount;
    }

    public void setEmitterAccount(String receiverAccount) {
        this.receiverAccount = receiverAccount;
    }

    public String getReceiverAccount() {
        return receiverAccount;
    }

    public void setReceiverAccount(String receiverAccount) {
        this.receiverAccount = receiverAccount;
    }

    public Asset getTransactionAsset() {
        return transactionAsset;
    }

    public void setTransactionAsset(Asset transactionAsset) {
        this.transactionAsset = transactionAsset;
    }

    public double getAmountOfMoney() {
        return amountOfMoney;
    }

    public void setAmountOfMoney(double amountOfMoney) {
        this.amountOfMoney = amountOfMoney;
    }

    public @JsonProperty("isAccepted") boolean isAccepted() {
        return isAccepted;
    }

    public void setAccepted(@JsonProperty("isAccepted") boolean accepted) {
        isAccepted = accepted;
    }


    @JsonIgnore
    public Transaction(String emitter, String receiver, String emitterAccount,String receiverAccount, double amountOfMoney){
        this.emitter = emitter;
        this.receiver = receiver;
        this.emitterAccount = emitterAccount;
        this.receiverAccount = receiverAccount;
        this.transactionAsset = null;
        this.amountOfMoney = amountOfMoney;
        addToBlockchain();
    }

    @JsonIgnore
    public Transaction(String emitter, String emitterAccount, Asset transactionAsset, double amountOfMoney){
        this.emitterAccount = emitterAccount;
        this.receiverAccount = null;
        this.transactionAsset = transactionAsset;
        addToBlockchain();
    }

    @JsonIgnore
    public Transaction(String receiver, Asset transactionAsset, String receiverAccount, double amountOfMoney){
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
            boolean checkWithdraw = MainBackEnd.searchAccount(emitterAccount).withdraw(amountOfMoney);
            if(checkWithdraw && MainBackEnd.searchAccount(receiverAccount) != null) MainBackEnd.searchAccount(receiverAccount).deposit(amountOfMoney);
            isAccepted = checkWithdraw;
            return checkWithdraw;
        }
    }

    @JsonIgnore
    public String getInfo(){
        return emitter.toString()+","+receiver.toString()+","+emitterAccount.toString()+","+receiverAccount.toString()+","+transactionAsset.toString()+","+amountOfMoney+"\n";
    }

    public void addToBlockchain(){
        Blockchain.getLast().add(this);
        System.out.println(Blockchain.getLast().getClass());
    }
}
