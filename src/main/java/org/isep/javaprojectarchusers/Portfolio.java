package org.isep.javaprojectarchusers;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.isep.javaprojectarchusers.Accounts.Account;
import org.isep.javaprojectarchusers.Accounts.CheckingAccount;

import java.util.ArrayList;
import java.util.LinkedList;

public class Portfolio {

    @JsonProperty("address")
    private String address;
    @JsonProperty("description")
    private String description;
    @JsonProperty("manager")
    private PortfolioManager manager;
    @JsonIgnore
    private ArrayList<Asset> assetList;
    @JsonIgnore
    private ArrayList<Account> accountList;
    @JsonProperty("blockchain")
    private LinkedList<Block> blockchain;

    @Override
    public String toString(){
        return this.address + " : " + this.description + ", manager: " + manager.toString();
    }

    public Portfolio(@JsonProperty("address") String address, @JsonProperty("description") String description, @JsonProperty("manager") PortfolioManager manager, @JsonProperty("blockchain")LinkedList<Block> blockchain){
        this.address = address;
        this.description = description;
        this.manager = manager;
        this.assetList = new ArrayList<>();
        this.accountList = new ArrayList<>();
        this.blockchain = blockchain;
        MainBackEnd.addPortfolio(this);
    }

    @JsonIgnore
    public Portfolio(String address, String description, PortfolioManager manager){
        this.address = address;
        this.description = description;
        this.manager = manager;
        this.assetList = new ArrayList<>();
        this.accountList = new ArrayList<>();
        this.blockchain = Blockchain.getBlockchainList();
        MainBackEnd.addPortfolio(this);
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
        Transaction transaction = new Transaction(this.getAddress(), MainBackEnd.searchAccount(receiverAccount).getPortfolio(), emitterAccount, receiverAccount, amountOfMoney);
        return transaction.validateTransaction();
    }

    @JsonIgnore
    public void createCheckingAccount(String userName){
        CheckingAccount account = new CheckingAccount(userName, 2000, 1000, this, 0, 200);
        accountList.add(account);
    }

    /**
     * @param userName username to search
     * @return account of the user, if not found returns null
     */
    @JsonIgnore
    public Account getAccount(String userName){
        for(Account a : accountList) if(a.getUserName().equals(userName)) return a;
        return null;
    }

    public PortfolioManager getManager() {
        return manager;
    }

    public void setManager(PortfolioManager manager) {
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
