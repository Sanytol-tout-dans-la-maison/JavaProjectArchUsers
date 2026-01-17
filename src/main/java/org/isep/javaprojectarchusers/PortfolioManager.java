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

    // --- Listes statiques (Mise à jour avec keyList pour la sécurité) ---
    private static ArrayList<String> emailList = new ArrayList<>();
    private static ArrayList<String> passwordList = new ArrayList<>();
    private static ArrayList<String> keyList = new ArrayList<>(); // Ajouté pour compatibilité 3 arguments

    public PortfolioManager(){
        this.portfolioList = new ArrayList<>();
    }

    // =========================================================================
    // 1. AUTHENTIFICATION (Login / Register)
    // =========================================================================

    /**
     * Tente d'inscrire un nouvel utilisateur.
     */
    public static byte register(String inputEmail, String inputPassword, String confirmPassword) throws IOException, NoSuchAlgorithmException {
        if(!inputPassword.equals(confirmPassword)) return -1;
        else if(inputEmail.isEmpty() || inputPassword.isEmpty()) return 0;
        else{
            // Correction : On passe 3 listes pour respecter la signature de la méthode extract
            emailList.clear(); passwordList.clear(); keyList.clear(); // Nettoyage préventif
            LoginExtraction.extract(emailList, passwordList, keyList);

            if(emailList.contains(inputEmail)) return -2;

            emailList.add(inputEmail);
            passwordList.add(inputPassword);
            keyList.add(""); // On ajoute une entrée vide pour garder l'alignement des listes

            // Correction : On passe 3 listes à save
            LoginSave.save(emailList, passwordList, keyList);
            return 1;
        }
    }

    /**
     * Connecte l'utilisateur.
     */
    public boolean login(String inputEmail, String inputPassword) throws IOException, NoSuchAlgorithmException {
        // On s'assure que les listes sont vides avant extraction
        ArrayList<String> email = new ArrayList<>();
        ArrayList<String> password = new ArrayList<>();
        ArrayList<String> keys = new ArrayList<>(); // 3ème liste

        boolean cond = false;

        // Correction : 3 arguments
        LoginExtraction.extract(email, password, keys);

        for(int i = 0; i < email.size(); i++) {
            if(inputEmail.equals(email.get(i))) {
                // Vérification du hash
                if(Hashing.toHash(inputPassword).equals(password.get(i))) {
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
        MainBackEnd.addPortfolio(portfolio); // Ajout au backend pour la cohérence
    }

    /**
     * @param address address to search
     * @return portfolio bearing the address, else returns null
     */
    public Portfolio getPortfolio(String address){
        // Recherche locale
        for (Portfolio p : portfolioList) if(p.getAddress().equals(address)) return p;
        // Recherche globale (backup)
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

    /**
     * Version principale de l'achat (Cumulative).
     */
    public boolean buyAsset(String address, Asset asset, Account account) {
        Portfolio portfolio = getPortfolio(address);
        if(portfolio == null) return false;

        // On utilise la logique interne du Portfolio si elle existe,
        // ou on implémente la logique cumulative ici pour l'affichage graphique.

        // Logique cumulative (Fusionnée)
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

    /**
     * Version surchargée avec ASSET_TYPE (Restaurée de ton ancien code).
     */
    public boolean buyAsset(String address, ASSET_TYPE asset_type, Account account){
        // On délègue à la méthode principale buyAsset ci-dessus
        if (asset_type == ASSET_TYPE.CryptocurrencyToken)
            return buyAsset(address, new CryptocurrencyToken("Bitcoin"), account);
        else {
            return buyAsset(address, new Stock("Action Générique", 0.0), account);
        }
    }

    public boolean sellAsset(String address, Asset asset, Account account) {
        Portfolio portfolio = getPortfolio(address);
        if(portfolio == null) return false;

        // Logique de vente sécurisée
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

    /**
     * Transfert d'argent (Restauré de ton ancien code).
     */
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

    // --- GETTERS STATIQUES (Requis pour TestJson) ---

    public static ArrayList<String> getEmailList() {
        return emailList;
    }

    public static ArrayList<String> getPasswordList() {
        return passwordList;
    }

    public static ArrayList<String> getKeyList() {
        return keyList;
    }
}