package org.isep.javaprojectarchusers;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.isep.javaprojectarchusers.Accounts.Account;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

/**
 * Manager central gérant l'authentification, les portefeuilles et les ordres de marché.
 */
public class PortfolioManager {

    private ArrayList<Portfolio> portfolioList;

    @JsonProperty("userName")
    private String userName;

    // --- Listes statiques pour la gestion des comptes ---
    private static ArrayList<String> emailList = new ArrayList<>();
    private static ArrayList<String> passwordList = new ArrayList<>();
    private static ArrayList<String> keyList = new ArrayList<>();

    public PortfolioManager(){
        this.portfolioList = new ArrayList<>();
    }

    // =========================================================================
    // 1. AUTHENTIFICATION (Login / Register)
    // =========================================================================

    /**
     * Tente d'inscrire un nouvel utilisateur.
     * @return 1 (Succès), 0 (Champs vides), -1 (Mdp différents), -2 (Email existe déjà)
     */
    public static byte register(String inputEmail, String inputPassword, String confirmPassword) throws IOException, NoSuchAlgorithmException {
        if(!inputPassword.equals(confirmPassword)) return -1;
        else if(inputEmail.isEmpty() || inputPassword.isEmpty()) return 0;
        else{
            // 1. On charge l'état actuel pour vérifier si l'email existe
            emailList.clear(); passwordList.clear(); keyList.clear();
            LoginExtraction.extract(emailList, passwordList, keyList);

            if(emailList.contains(inputEmail)) return -2;

            // 2. Ajout des nouvelles données
            emailList.add(inputEmail);

            // IMPORTANT : On ajoute UNIQUEMENT le mot de passe haché
            passwordList.add(Hashing.toHash(inputPassword));

            // CRUCIAL : On met une valeur bidon ("salt") au lieu de vide ""
            // Cela empêche le crash "Index out of bounds" à la lecture du CSV
            keyList.add("salt");

            // 3. Sauvegarde
            LoginSave.save(emailList, passwordList, keyList);
            return 1;
        }
    }

    /**
     * Connecte l'utilisateur.
     */
    public boolean login(String inputEmail, String inputPassword) throws IOException, NoSuchAlgorithmException {
        // Variables locales pour éviter les conflits
        ArrayList<String> email = new ArrayList<>();
        ArrayList<String> password = new ArrayList<>();
        ArrayList<String> keys = new ArrayList<>();

        boolean cond = false;

        // Extraction des 3 colonnes (Email, Hash, Key)
        LoginExtraction.extract(email, password, keys);

        for(int i = 0; i < email.size(); i++) {
            if(inputEmail.equals(email.get(i))) {
                // Vérification sécurisée avec trim() pour éviter les erreurs d'espaces
                if(Hashing.toHash(inputPassword).equals(password.get(i).trim())) {
                    userName = inputEmail;
                    cond = true;
                    break;
                }
            }
        }

        if(cond) {
            addPortfolios();
        }
        return cond;
    }

    // =========================================================================
    // 2. GESTION DES PORTFOLIOS
    // =========================================================================

    public void createPortfolio(String address, String description) {
        Portfolio portfolio = new Portfolio(address, description, this);
        portfolioList.add(portfolio);
        MainBackEnd.addPortfolio(portfolio);
    }

    public Portfolio getPortfolio(String address){
        for (Portfolio p : portfolioList) if(p.getAddress().equals(address)) return p;
        return MainBackEnd.searchPortfolio(address);
    }

    public void addPortfolios(){
        if(MainBackEnd.getPortfolioArrayList() != null) {
            for(Portfolio p : MainBackEnd.getPortfolioArrayList()) {
                if(p.getManager() != null &&
                        p.getManager().getUserName() != null &&
                        p.getManager().getUserName().equals(userName)) {

                    if(!portfolioList.contains(p)) portfolioList.add(p);
                }
            }
        }
    }

    public void removePortfolio(Portfolio portfolio) {
        portfolioList.remove(portfolio);
    }

    // =========================================================================
    // 3. ACHAT / VENTE / TRANSFERT
    // =========================================================================

    public boolean buyAsset(String address, Asset asset, Account account) {
        Portfolio portfolio = getPortfolio(address);
        if(portfolio == null) return false;

        boolean assetExists = false;
        for(Asset existingAsset : portfolio.getAssetList()) {
            if(existingAsset.getAssetName().equals(asset.getAssetName())) {
                existingAsset.setValue(existingAsset.getValue() + asset.getValue());
                assetExists = true;
                break;
            }
        }
        if(!assetExists) {
            portfolio.getAssetList().add(asset);
        }

        Transaction t = new Transaction("Market", account.getUserName(), "MarketAccount", account.getUserName(), asset, 0.0, true);
        t.addToBlockchain();
        return true;
    }

    // Surcharge pour compatibilité avec l'ancien code (ASSET_TYPE)
    public boolean buyAsset(String address, ASSET_TYPE asset_type, Account account){
        if (asset_type == ASSET_TYPE.CryptocurrencyToken)
            return buyAsset(address, new CryptocurrencyToken("Bitcoin"), account);
        else {
            return buyAsset(address, new Stock("Action Générique", 0.0), account);
        }
    }

    public boolean sellAsset(String address, Asset asset, Account account) {
        Portfolio portfolio = getPortfolio(address);
        if(portfolio == null) return false;

        Asset possessedAsset = null;
        for(Asset a : portfolio.getAssetList()) {
            if(a.getAssetName().equals(asset.getAssetName())) {
                possessedAsset = a;
                break;
            }
        }

        if(possessedAsset == null) return false;

        double qtyToSell = asset.getValue();
        if(possessedAsset.getValue() < qtyToSell) return false;

        possessedAsset.setValue(possessedAsset.getValue() - qtyToSell);
        if(possessedAsset.getValue() <= 0.000001) {
            portfolio.getAssetList().remove(possessedAsset);
        }

        Transaction t = new Transaction(account.getUserName(), "Market", account.getUserName(), "MarketAccount", asset, 0.0, true);
        t.addToBlockchain();
        return true;
    }

    public boolean transferMoney(String address, Account emitterAccount, Account receiverAccount, double amountOfMoney) {
        Portfolio portfolio = getPortfolio(address);
        if(portfolio != null) {
            return portfolio.transferMoney(emitterAccount.getUserName(), receiverAccount.getUserName(), amountOfMoney);
        }
        return false;
    }

    // =========================================================================
    // 4. GETTERS / SETTERS
    // =========================================================================

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public ArrayList<Portfolio> getPortfolioList() {
        return portfolioList;
    }

    @Override
    public String toString() {
        return userName;
    }

    // Getters statiques pour les tests
    public static ArrayList<String> getEmailList() { return emailList; }
    public static ArrayList<String> getPasswordList() { return passwordList; }
    public static ArrayList<String> getKeyList() { return keyList; }
}