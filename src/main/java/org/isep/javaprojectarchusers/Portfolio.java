package org.isep.javaprojectarchusers;

import org.isep.javaprojectarchusers.Accounts.Account;
import org.isep.javaprojectarchusers.Accounts.CheckingAccount;

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

        this.assetList = new ArrayList<>();
        this.accountList = new ArrayList<>();
        MainBackEnd.addPortfolio(this);
    }

    public PortfolioManager getManager() {
        return manager;
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

    public void createCheckingAccount(String userName){
        CheckingAccount account = new CheckingAccount(userName, 2000, 1000, this, 0, 200);
        accountList.add(account);
    }

    /**
     * @param userName username to search
     * @return account of the user, if not found returns null
     */
    public Account getAccount(String userName){
        for(Account a : accountList) if(a.getUserName().equals(userName)) return a;
        return null;
    }

}
