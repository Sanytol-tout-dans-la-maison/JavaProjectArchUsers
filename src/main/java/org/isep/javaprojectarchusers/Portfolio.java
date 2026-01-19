package org.isep.javaprojectarchusers;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.isep.javaprojectarchusers.Accounts.Account;
import org.isep.javaprojectarchusers.Accounts.CheckingAccount;
import org.isep.javaprojectarchusers.Accounts.SavingAccount;
import org.isep.javaprojectarchusers.Assets.Asset;
import org.isep.javaprojectarchusers.Blockchain.Block;
import org.isep.javaprojectarchusers.Blockchain.Blockchain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;

public class Portfolio {

    @JsonProperty("address")
    private String address;
    @JsonProperty("description")
    private String description;
    @JsonProperty("manager")
    private String manager;
    @JsonProperty("assets")
    private ArrayList<Asset> assetList;
    @JsonProperty("accounts")
    private ArrayList<Account> accountList;
    @JsonProperty("blockchain")
    private LinkedList<Block> blockchain;
    @JsonIgnore
    private static ArrayList<Portfolio> portfolioArrayList = new ArrayList<>();

    @Override
    public String toString() {
        return this.address + " : " + this.description + ", manager: " + (manager != null ? manager.toString() : "null");
    }

    @JsonIgnore
    public Portfolio(@JsonProperty("address") String address, @JsonProperty("description") String description, @JsonProperty("manager") String manager, @JsonProperty("blockchain") LinkedList<Block> blockchain) {
        this.address = address;
        this.description = description;
        this.manager = manager;
        this.assetList = new ArrayList<>();
        this.accountList = new ArrayList<>();
        this.blockchain = blockchain;
        addToListIfPossible(this);
    }


    public Portfolio(@JsonProperty("address") String address, @JsonProperty("description") String description, @JsonProperty("manager") String manager, @JsonProperty("blockchain") LinkedList<Block> blockchain, @JsonProperty("assets") ArrayList<Asset> assetList, @JsonProperty("accounts") ArrayList<Account> accountList) {
        this.address = address;
        this.description = description;
        this.manager = manager;
        this.assetList = new ArrayList<>();
        this.accountList = new ArrayList<>();
        this.blockchain = blockchain;
        this.assetList = assetList;
        this.accountList = accountList;
        addToListIfPossible(this);
    }

    @JsonIgnore
    public Portfolio(String address, String description, String manager) {
        this.address = address;
        this.description = description;
        this.manager = manager;
        this.assetList = new ArrayList<>();
        this.accountList = new ArrayList<>();
        this.blockchain = Blockchain.getBlockchainList();
        addToListIfPossible(this);
    }


    @JsonIgnore
    public boolean buyAsset(Asset asset, String emitterAccount) {
        Transaction transaction = new Transaction(address, emitterAccount, asset, asset.getValue(), LocalDate.now());
        if (transaction.validateTransaction()) {
            assetList.add(asset);
            return true;
        } else return false;
    }

    @JsonIgnore
    public boolean sellAsset(String assetName, String account) {
        Asset asset = getAssetFromName(assetName);
        Transaction transaction = new Transaction(MainBackEnd.searchAccount(account).getPortfolio(), account, asset, asset.getValue(), LocalDate.now());
        if (transaction.validateTransaction()) {
            for (int i = 0; i < assetList.size(); i++) if (assetList.get(i).equals(asset)) assetList.remove(i);
            return true;
        } else return false;
    }

    @JsonIgnore
    public boolean transferMoney(String emitterAccount, String receiverAccount, double amountOfMoney) {
        Account receiver = MainBackEnd.searchAccount(receiverAccount);
        if (receiver == null) return false;
        Transaction transaction = new Transaction(this.getAddress(), receiver.getPortfolio(), emitterAccount, receiverAccount, amountOfMoney, LocalDate.now());
        return transaction.validateTransaction();
    }

    @JsonIgnore
    public boolean createCheckingAccount(String userName) {
        for(CheckingAccount checkingAccount : CheckingAccount.getCheckingAccountArrayList()) if(checkingAccount.getUserName().equals(userName)) return false;
        CheckingAccount account = new CheckingAccount(userName, 2000, 1000, this.address, 0, 200);
        accountList.add(account);
        return true;
    }

    @JsonIgnore
    public boolean createSavingAccount(String userName) {
        for(SavingAccount savingAccount : SavingAccount.getSavingAccountArrayList()) if(savingAccount.getUserName().equals(userName)) return false;
        SavingAccount account = new SavingAccount(userName, 2000, 1000, this.address, 500);
        accountList.add(account);
        return true;
    }

    @JsonIgnore
    public Account getAccount(String userName) {
        for (Account a : accountList) if (a.getUserName().equals(userName)) return a;
        return null;
    }

    public @JsonProperty("manager") String getManager() {
        return manager;
    }

    public void setManager(@JsonProperty("manager") String manager) {
        this.manager = manager;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
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

    public @JsonProperty("assets") ArrayList<Asset> getAssetList() {
        return assetList;
    }

    public void setAssetList(@JsonProperty("assets") ArrayList<Asset> assetList) {
        this.assetList = assetList;
    }

    public void setAccountList(@JsonProperty("accounts") ArrayList<Account> accountList) {
        this.accountList = accountList;
    }

    public @JsonProperty("accounts") ArrayList<Account> getAccountList() {
        return accountList;
    }

    public static ArrayList<Portfolio> getPortfolioArrayList() {
        return portfolioArrayList;
    }

    public static void setPortfolioArrayList(ArrayList<Portfolio> portfolioArrayList) {
        Portfolio.portfolioArrayList = portfolioArrayList;
    }

    @JsonIgnore
    public boolean checkAssetExistence(String assetName){
        for(Asset asset : assetList) if(asset.getAssetName().equals(assetName)) return true;
        return false;
    }

    public Asset getAssetFromName(String assetName){
        for(Asset asset : assetList) if(asset.getAssetName().equals(assetName)) return asset;
        return null;
    }

    public void updateBlockchain(){
        blockchain = Blockchain.getBlockchainList();
    }

    private void addToListIfPossible(Portfolio portfolio){
        for(Portfolio p : portfolioArrayList) if(p.getAddress() == portfolio.getAddress()) return;
        portfolioArrayList.add(portfolio);
    }
}