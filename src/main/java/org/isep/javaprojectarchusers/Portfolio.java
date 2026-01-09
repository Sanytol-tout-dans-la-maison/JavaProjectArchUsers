package org.isep.javaprojectarchusers;

import java.util.ArrayList;

public class Portfolio {

    private String address;
    private String description;
    private PortfolioManager manager;
    private ArrayList<Asset> assetList;
    private ArrayList<Account> accountList;

    @Override
    public String toString(){
        return this.address + " : " + this.description + ", manager: " + manager.toString();
    }

    public String getAddress() {
        return address;
    }

    public Portfolio(String address, String description, PortfolioManager manager){
        this.address = address;
        this.description = description;
        this.manager = manager;
    }

    public boolean buyAsset(Asset asset, Account account){
        Transaction transaction = new Transaction(account, asset);
        return transaction.validateTransaction();
    }

}
