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

    public boolean buyAsset(Asset asset, Account emitterAccount){
        Transaction transaction = new Transaction(this, emitterAccount, asset, asset.getValue() );
        if (transaction.validateTransaction()){
            assetList.add(asset);
            return true;
        }
        else return false;
    }

    public boolean sellAsset(Asset asset, Account account){
        Transaction transaction = new Transaction(account.getPortfolio(), asset, account, asset.getValue());
        if(transaction.validateTransaction()){
            for(int i = 0; i < assetList.size(); i++) if(assetList.get(i).equals(asset)) assetList.remove(i);
            return true;
        }
        else return false;
    }

    public boolean transferMoney(Account emitterAccount, Account receiverAccount, double amountOfMoney){
        Transaction transaction = new Transaction(this, receiverAccount.getPortfolio(), emitterAccount, receiverAccount, amountOfMoney);
        return transaction.validateTransaction();
    }


}
