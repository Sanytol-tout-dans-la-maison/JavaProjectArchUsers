package org.isep.javaprojectarchusers;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.isep.javaprojectarchusers.Accounts.Account;
import org.isep.javaprojectarchusers.Assets.*;
import org.isep.javaprojectarchusers.Encryption.Encryption;
import org.isep.javaprojectarchusers.Encryption.Hashing;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

/**
 * Manager central gérant l'authentification, les portefeuilles et les ordres de marché.
 */
public class PortfolioManager {
    private static ArrayList<Portfolio> portfolioList = new ArrayList<>();
    private static @JsonProperty("userName") String userName;
    private static ArrayList<String> emailList = new ArrayList<>();
    private static ArrayList<String> passwordList = new ArrayList<>();
    private static ArrayList<String> keyList = new ArrayList<>();

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

    public static boolean login(String inputEmail, String inputPassword) throws IOException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        boolean cond = false;
        LoginExtraction.extract(emailList, passwordList, keyList);
        for(int i = 0; i < emailList.size(); i++) {
            if((Hashing.toHash(inputPassword).equals(passwordList.get(i))) && Encryption.decryptString(emailList.get(i), Encryption.stringToKey(inputPassword)).equals(inputEmail)) {
                userName = inputEmail;
                System.out.println(Encryption.decryptString(keyList.get(i),Encryption.stringToKey(inputPassword)));
                System.out.println(Encryption.keyToString(Encryption.getKey()));
                System.out.println(Encryption.decryptString(emailList.get(i), Encryption.stringToKey(inputPassword)));
                cond = true;
            }
        }
        addPortfolios();
        return cond;
    }

    /**
     * @param inputEmail
     * @param inputPassword
     * @param confirmPassword to confirm inputPassword
     * @return -2 if email is already registered
     * @return -1 if passwords don't match
     * @@return 0 if one field is empty
     * @return 1 if account is registered properly
     * @throws IOException
     * @throws NoSuchAlgorithmException
     * @throws InvalidAlgorithmParameterException
     * @throws NoSuchPaddingException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     * @throws InvalidKeyException
     */
    public static byte register(String inputEmail, String inputPassword, String confirmPassword) throws IOException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        if(!inputPassword.equals(confirmPassword)) return -1;
        else if(inputEmail.isEmpty() || inputPassword.isEmpty()) return 0;
        else{
            LoginExtraction.extract(emailList, passwordList, keyList);
            for(String email : emailList) if(email.equals(inputEmail)) return -2;
            emailList.addLast(Encryption.encryptString(inputEmail,Encryption.stringToKey(inputPassword)));
            passwordList.addLast(Hashing.toHash(inputPassword));
            keyList.add(Encryption.encryptString(Encryption.keyToString(Encryption.getKey()), Encryption.stringToKey(inputPassword)));
            LoginSave.save(emailList,passwordList,keyList);
            return 1;
        }
    }

    public static void createPortfolio(String address, String description) {
        portfolioList.add(new Portfolio(address, description, userName));
    }

    /**
     * @param address name of the portfolio
     * @param asset asset to buy
     * @param account account to debit from
     * @return
     */
    public static boolean buyAsset(String address, Asset asset, Account account) {
        for (Portfolio portfolio : portfolioList) if(portfolio.getAddress().equals(address)) return portfolio.buyAsset(asset, account.getUserName());
        return false;
    }

    /**
     * @param address address of the portfolio
     * @param assetName name of the asset
     * @param asset_type type of the asset
     * @param account account to debit from
     *                allows to buy an asset that doesn't previously exist
     * @return
     */
    public static boolean buyAsset(String address, String assetName ,ASSET_TYPE asset_type, Account account){
        for (Portfolio portfolio : portfolioList) {
            if(portfolio.getAddress().equals(address)) {
                    for(GeneralAssets g : GeneralAssets.getGeneralAssetList()) if(g.getGeneralAssetName().equals(assetName) && g.getGeneralAssetType().equals(asset_type)) return portfolio.buyAsset(new Asset(assetName, asset_type, portfolio.getAddress()), account.getUserName());
                    new GeneralAssets(assetName, asset_type);
                    return portfolio.buyAsset(new Asset(assetName, asset_type, portfolio.getAddress()), account.getUserName());
                }
            }
        return false;
    }

    public static boolean sellAsset(String address, Asset asset, Account account) {
        for (Portfolio portfolio : portfolioList) if(portfolio.getAddress().equals(address)) return portfolio.sellAsset(asset, account.getUserName());
        return false;
    }

    public static boolean transferMoney(String address, Account emitterAccount, Account receiverAccount, double amountOfMoeny) {
        for (Portfolio portfolio : portfolioList) if(portfolio.getAddress().equals(address)) return portfolio.transferMoney(emitterAccount.getUserName(), receiverAccount.getUserName(), amountOfMoeny);
        return false;
    }

    /**
     * @param address address to search
     * @return portfolio bearing the address, else returns null
     */
    public static Portfolio getPortfolio(String address){
        for (Portfolio p : portfolioList) if(p.getAddress().equals(address)) return p;
        return null;
    }

    public static void addPortfolios() throws IOException {
        for(Portfolio p : Portfolio.getPortfolioArrayList()) if(p.getManager().equals(userName)) portfolioList.add(p);
    }

    public static ArrayList<Portfolio> getPortfolioList() {
        return portfolioList;
    }

    public static ArrayList<String> getEmailList() {
        return emailList;
    }

    public static ArrayList<String> getKeyList() {
        return keyList;
    }

    public static ArrayList<String> getPasswordList() {
        return passwordList;
    }
}
