package org.isep.javaprojectarchusers;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.isep.javaprojectarchusers.Accounts.Account;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class PortfolioManager {
    private ArrayList<Portfolio> portfolioList;
    private @JsonProperty("userName") String userName;
    private static ArrayList<String> emailList = new ArrayList<>();
    private static ArrayList<String> passwordList = new ArrayList<>();

    public PortfolioManager(){
        this.portfolioList = new ArrayList<>();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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
        getPortfolios();
        return cond;
    }

    public static byte register(String inputEmail, String inputPassword, String confirmPassword) throws IOException, NoSuchAlgorithmException {
        if(!inputPassword.equals(confirmPassword)) return -1;
        else if(inputEmail.isEmpty() || inputPassword.isEmpty()) return 0;
        else{
            LoginExtraction.extract(emailList, passwordList);
            emailList.addLast(inputEmail);
            passwordList.addLast(inputPassword);
            LoginSave.save(emailList, passwordList);
            return 1;
        }
    }

    public void createPortfolio(String address, String description) {
        portfolioList.add(new Portfolio(address, description, this));
    }

    public boolean buyAsset(String address, Asset asset, Account account) {
        for (Portfolio portfolio : portfolioList) if(portfolio.getAddress().equals(address)) return portfolio.buyAsset(asset, account.getUserName());
        return false;
    }

    public boolean buyAsset(String address, ASSET_TYPE asset_type, Account account){
        for (Portfolio portfolio : portfolioList) {
            if(portfolio.getAddress().equals(address)) {
                if (asset_type == ASSET_TYPE.CryptocurrencyToken)
                    return portfolio.buyAsset(new CryptocurrencyToken("Bitcoin"), account.getUserName());
                else {
                    return portfolio.buyAsset(new Stock("Action Générique", 0.0), account.getUserName());
                }
            }

        }
        return false;
    }

    public boolean sellAsset(String address, Asset asset, Account account) {
        for (Portfolio portfolio : portfolioList) if(portfolio.getAddress().equals(address)) return portfolio.sellAsset(asset, account.getUserName());
        return false;
    }

    public boolean transferMoney(String address, Account emitterAccount, Account receiverAccount, double amountOfMoeny) {
        for (Portfolio portfolio : portfolioList) if(portfolio.getAddress().equals(address)) return portfolio.transferMoney(emitterAccount.getUserName(), receiverAccount.getUserName(), amountOfMoeny);
        return false;
    }

    /**
     * @param address address to search
     * @return portfolio bearing the address, else returns null
     */
    public Portfolio getPortfolio(String address){
        for (Portfolio p : portfolioList) if(p.getAddress().equals(address)) return p;
        return null;
    }

    public void getPortfolios(){
        for(Portfolio p : MainBackEnd.getPortfolioArrayList()) if(p.getManager().getUserName().equals(userName)) portfolioList.add(p);
    }

    public ArrayList<Portfolio> getPortfolioList() {
        return portfolioList;
    }

}
