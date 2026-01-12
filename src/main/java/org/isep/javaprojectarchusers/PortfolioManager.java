package org.isep.javaprojectarchusers;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class PortfolioManager {
    private ArrayList<Portfolio> portfolioList;
    private String userName;
    ArrayList<String> emailList = new ArrayList<>();
    ArrayList<String> passwordList = new ArrayList<>();

    public PortfolioManager(){
        this.portfolioList = new ArrayList<>();
    }

    @Override
    public String toString() {
        return userName;
    }

    public boolean login(String inputEmail, String inputPassword) throws IOException, NoSuchAlgorithmException {
        ArrayList<String> email = new ArrayList<>();
        ArrayList<String> password = new ArrayList<>();
        String name = "";
        boolean cond = false;
        LoginExtraction.extract(email,password);
        for(int i = 0; i < email.size(); i++) {
            if(inputEmail.equals(email.get(i)) && Hashing.toHash(password.get(i)).equals(password.get(i))) {
                userName = inputEmail;
                cond = true;
            }
        }
        return cond;
    }

    public boolean register(String inputEmail, String inputPassword, String confirmPassword) throws IOException, NoSuchAlgorithmException {
        if(!inputPassword.equals(confirmPassword)) return false;
        else if(inputEmail.isEmpty() || inputPassword.isEmpty()) return false;
        else{
            LoginExtraction.extract(emailList, passwordList);
            emailList.addLast(inputEmail);
            passwordList.addLast(inputPassword);
            LoginSave.save(emailList, passwordList);
            return true;
        }
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
                if (asset_type == ASSET_TYPE.CryptocurrencyToken)
                    return portfolio.buyAsset(new CryptocurrencyToken(), account);
                else {
                    return portfolio.buyAsset(new Stock("Action Générique", 0.0), account);
                }
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
