package org.isep.javaprojectarchusers;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.isep.javaprojectarchusers.Accounts.Account;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

/**
 * Manager central gérant l'authentification, les portefeuilles et les ordres de marché.
 * Combine les outils de gestion utilisateur et la logique de trading.
 */
public class PortfolioManager {

    private ArrayList<Portfolio> portfolioList;

    @JsonProperty("userName")
    private String userName;

    // Listes statiques pour la vérification rapide lors du login/register
    private static ArrayList<String> emailList = new ArrayList<>();
    private static ArrayList<String> passwordList = new ArrayList<>();

    public PortfolioManager(){
        this.portfolioList = new ArrayList<>();
    }


    /**
     * Tente d'inscrire un nouvel utilisateur.
     * @return 1 si succès, 0 si champs vides, -1 si mdp différents, -2 si email pris.
     */
    public static int register(String email, String password, String confirmPassword) throws IOException, NoSuchAlgorithmException {
        if(email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()){
            return 0;
        }
        if(!password.equals(confirmPassword)){
            return -1;
        }

        ArrayList<String> existingEmails = new ArrayList<>();
        ArrayList<String> existingPwds = new ArrayList<>();
        LoginExtraction.extract(existingEmails, existingPwds);

        if(existingEmails.contains(email)){
            return -2;
        }

        existingEmails.add(email);
        existingPwds.add(password);
        LoginSave.save(existingEmails, existingPwds);

        return 1;
    }

    /**
     * Connecte l'utilisateur en vérifiant le hash du mot de passe.
     * Charge les portfolios associés en cas de succès.
     */
    public boolean login(String inputEmail, String inputPassword) throws IOException, NoSuchAlgorithmException {
        ArrayList<String> email = new ArrayList<>();
        ArrayList<String> password = new ArrayList<>();
        boolean cond = false;

        LoginExtraction.extract(email, password);

        for(int i = 0; i < email.size(); i++) {
            if(inputEmail.equals(email.get(i))) {
                // Vérification sécurisée : Hash(Input) == HashStocké
                String inputHash = Hashing.toHash(inputPassword);
                if (inputHash.equals(password.get(i))) {
                    this.userName = inputEmail;
                    cond = true;
                    break;
                }
            }
        }

        if (cond) {
            addPortfolios(); // Récupère les données du Backend
        }
        return cond;
    }

    public void createPortfolio(String address, String description){
        Portfolio portfolio = new Portfolio(address, description, this);
        portfolioList.add(portfolio);
        // Ajout au backend pour la persistance mémoire (RAM)
        MainBackEnd.addPortfolio(portfolio);
    }

    /**
     * Recherche un portfolio par son adresse.
     * Regarde d'abord localement, puis dans le backend global.
     */
    public Portfolio getPortfolio(String address){
        for (Portfolio p : portfolioList) {
            if(p.getAddress().equals(address)) return p;
        }
        return MainBackEnd.searchPortfolio(address);
    }

    /**
     * Synchronise la liste locale avec les portfolios du Backend appartenant à cet utilisateur.
     */
    public void addPortfolios(){
        ArrayList<Portfolio> allPortfolios = MainBackEnd.getPortfolioArrayList();
        if(allPortfolios != null) {
            for (Portfolio p : allPortfolios) {
                // Vérification stricte du propriétaire
                if (p.getManager() != null &&
                        p.getManager().getUserName() != null &&
                        p.getManager().getUserName().equals(this.userName)) {

                    if(!portfolioList.contains(p)){
                        portfolioList.add(p);
                    }
                }
            }
        }
    }

    /**
     * Achète un actif. Si l'actif existe déjà, on cumule la quantité.
     */
    public boolean buyAsset(String address, Asset asset, Account account) {
        Portfolio portfolio = getPortfolio(address);
        if (portfolio != null && account != null) {

            // Logique cumulative : on cherche si l'asset existe déjà
            boolean assetExists = false;
            for(Asset existingAsset : portfolio.getAssetList()) {
                if(existingAsset.getAssetName().equals(asset.getAssetName())) {
                    existingAsset.setValue(existingAsset.getValue() + asset.getValue());
                    assetExists = true;
                    break;
                }
            }

            // Si c'est un nouvel asset, on l'ajoute à la liste
            if(!assetExists) {
                portfolio.getAssetList().add(asset);
            }

            // Enregistrement dans la Blockchain
            Transaction t = new Transaction("Market", account.getUserName(), "MarketAccount", account.getUserName(), asset, 0.0, true);
            t.addToBlockchain();
            return true;
        }
        return false;
    }

    /**
     * Vend un actif. Si la quantité tombe à 0, l'actif est supprimé de la liste.
     */
    public boolean sellAsset(String address, Asset assetToSell, Account account) {
        Portfolio portfolio = getPortfolio(address);
        if (portfolio != null && account != null) {

            Asset possessedAsset = null;
            for(Asset a : portfolio.getAssetList()) {
                if(a.getAssetName().equals(assetToSell.getAssetName())) {
                    possessedAsset = a;
                    break;
                }
            }

            if(possessedAsset == null) return false; // L'utilisateur ne possède pas cet asset

            double qtyToSell = assetToSell.getValue();
            if(possessedAsset.getValue() < qtyToSell) {
                return false; // Pas assez de quantité
            }

            // Soustraction
            possessedAsset.setValue(possessedAsset.getValue() - qtyToSell);

            // Nettoyage si vide (ou presque vide à cause des doubles)
            if(possessedAsset.getValue() <= 0.000001) {
                portfolio.getAssetList().remove(possessedAsset);
            }

            Transaction t = new Transaction(account.getUserName(), "Market", account.getUserName(), "MarketAccount", assetToSell, 0.0, true);
            t.addToBlockchain();

            return true;
        }
        return false;
    }

    /**
     * Transfère de l'argent entre deux comptes via un portfolio.
     * (Réintégré depuis ton ancien code)
     */
    public boolean transferMoney(String address, Account emitterAccount, Account receiverAccount, double amountOfMoney) {
        Portfolio portfolio = getPortfolio(address);
        if(portfolio != null) {
            return portfolio.transferMoney(emitterAccount.getUserName(), receiverAccount.getUserName(), amountOfMoney);
        }
        return false;
    }

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
}