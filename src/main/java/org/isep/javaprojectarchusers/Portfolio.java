package org.isep.javaprojectarchusers;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.isep.javaprojectarchusers.Accounts.Account;
import org.isep.javaprojectarchusers.Accounts.CheckingAccount;
import org.isep.javaprojectarchusers.Assets.Asset;
import org.isep.javaprojectarchusers.Blockchain.Block;
import org.isep.javaprojectarchusers.Blockchain.Blockchain;

import java.util.ArrayList;
import java.util.LinkedList;

public class Portfolio {

    @JsonProperty("address")
    private String address;
    @JsonProperty("description")
    private String description;
    @JsonProperty("manager")
    private String manager;
    @JsonIgnore
    private ArrayList<Asset> assetList;
    @JsonIgnore
    private ArrayList<Account> accountList;
    @JsonProperty("blockchain")
    private LinkedList<Block> blockchain;

    @Override
    public String toString(){
        return this.address + " : " + this.description + ", manager: " + (manager != null ? manager.toString() : "null");
    }

    public Portfolio(@JsonProperty("address") String address, @JsonProperty("description") String description, @JsonProperty("manager") String manager, @JsonProperty("blockchain")LinkedList<Block> blockchain){
        this.address = address;
        this.description = description;
        this.manager = manager;
        this.assetList = new ArrayList<>();
        this.accountList = new ArrayList<>();
        this.blockchain = blockchain;
    }

    @JsonIgnore
    public Portfolio(String address, String description, String manager){
        this.address = address;
        this.description = description;
        this.manager = manager;
        this.assetList = new ArrayList<>();
        this.accountList = new ArrayList<>();
        this.blockchain = Blockchain.getBlockchainList();
        MainBackEnd.addPortfolio(this);
    }

    // Constructeur vide nécessaire
    public Portfolio() {
        this.assetList = new ArrayList<>();
        this.accountList = new ArrayList<>();
    }

    @JsonIgnore
    public boolean buyAsset(Asset asset, String emitterAccount){
        Transaction transaction = new Transaction(address, emitterAccount, asset, asset.getValue() );
        if (transaction.validateTransaction()){
            assetList.add(asset);
            return true;
        }
        else return false;
    }

    @JsonIgnore
    public boolean sellAsset(Asset asset, String account){
        Transaction transaction = new Transaction(MainBackEnd.searchAccount(account).getPortfolio(), account, asset, asset.getValue());
        if(transaction.validateTransaction()){
            for(int i = 0; i < assetList.size(); i++) if(assetList.get(i).equals(asset)) assetList.remove(i);
            return true;
        }
        else return false;
    }

    @JsonIgnore
    public boolean transferMoney(String emitterAccount, String receiverAccount, double amountOfMoney){
        Account receiver = MainBackEnd.searchAccount(receiverAccount);
        if(receiver == null) return false;
        Transaction transaction = new Transaction(this.getAddress(), receiver.getPortfolio(), emitterAccount, receiverAccount, amountOfMoney);
        return transaction.validateTransaction();
    }

    @JsonIgnore
    public void createCheckingAccount(String userName){
        CheckingAccount account = new CheckingAccount(userName, 2000, 1000, this.address, 0, 200);
        accountList.add(account);
    }

    @JsonIgnore
    public Account getAccount(String userName){
        for(Account a : accountList) if(a.getUserName().equals(userName)) return a;
        return null;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address){
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LinkedList<Block> getBlockchain() {
        return blockchain;
    }

    public void setBlockchain(LinkedList<Block> blockchain) {
        this.blockchain = blockchain;
    }

    public ArrayList<Asset> getAssetList() {
        return assetList;
    }

    public ArrayList<Account> getAccountList() {
        return accountList;
    }
}