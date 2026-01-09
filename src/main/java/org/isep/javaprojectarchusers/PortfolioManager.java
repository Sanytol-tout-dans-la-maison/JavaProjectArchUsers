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


}
