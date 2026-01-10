package org.isep.javaprojectarchusers;

import java.util.ArrayList;

public class PortfolioManager {
    private String userName;
    private String password;
    private ArrayList<Portfolio> portfolioList;

    @Override
    public String toString() {
        return userName;
    }

    public boolean login() {
        return true;
    }

    public void createPortfolio(String address, String description) {
        portfolioList.add(new Portfolio(address, description, this));
    }

    public boolean buyAsset(String address, Asset asset, Account account) {
        for (Portfolio portfolio : portfolioList) if(portfolio.getAddress().equals(address)) return portfolio.buyAsset(asset, account);
        return false;
    }

    public boolean buyAsset(String address, ASSET_TYPE asset_type, Account account){
        for (Portfolio portfolio : portfolioList) {
            if(portfolio.getAddress().equals(address)) {
                if(asset_type == ASSET_TYPE.CryptocurrencyToken)  return portfolio.buyAsset(new CryptocurrencyToken(), account);
                else return portfolio.buyAsset(new Stock(), account);
            }
        }
        return false;
    }

    public boolean sellAsset(String address, Asset asset, Account account) {
        for (Portfolio portfolio : portfolioList) if(portfolio.getAddress().equals(address)) return portfolio.sellAsset(asset, account);
        return false;
    }

    public boolean transferMoney(String address, Account emitterAccount, Account receiverAccount, double amountOfMoeny) {
        for (Portfolio portfolio : portfolioList) if(portfolio.getAddress().equals(address)) return portfolio.transferMoney(emitterAccount, receiverAccount, amountOfMoeny);
        return false;
    }
    

}
