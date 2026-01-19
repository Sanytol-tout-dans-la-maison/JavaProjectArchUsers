package org.isep.javaprojectarchusers;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.isep.javaprojectarchusers.Accounts.Account;
import org.isep.javaprojectarchusers.Blockchain.Block;
import org.isep.javaprojectarchusers.Blockchain.Blockchain;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.File;
import java.util.ArrayList;

public class MainBackEnd {
    private static ArrayList<Portfolio> portfolioArrayList =  new ArrayList<>();
    private static ArrayList<Account> accountArrayList = new ArrayList<>();
    private static LinkedList blockchain;

//    public static ArrayList<Portfolio> getPortfolioArrayList() throws IOException {
//        extractPortfolios();
//        return portfolioArrayList;
//    }


    public static void savePortfolios(ArrayList<Portfolio> portfolios) {
        try {
            portfolioArrayList = Portfolio.getPortfolioArrayList();
            ObjectMapper mapper = new ObjectMapper();
            // Pour que le JSON soit joli et lisible (optionnel)
            mapper.enable(SerializationFeature.INDENT_OUTPUT);

            // Le chemin vers ton fichier portfolios.json
            File file = new File("src/main/resources/org/isep/javaprojectarchusers/portfolios.json");

            // Écriture dans le fichier
            mapper.writeValue(file, portfolios);
            System.out.println("[Backend] Sauvegarde des portfolios réussie !");

        } catch (Exception e) {
            System.err.println("[Backend] Erreur lors de la sauvegarde : " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void extractPortfolios() throws IOException{
        ObjectMapper objectMapper = new ObjectMapper();
        portfolioArrayList = objectMapper.readValue(new File("src/main/resources/org/isep/javaprojectarchusers/portfolios.json"), new TypeReference<ArrayList<Portfolio>>() {
        });
    }

    public static void addPortfolio(Portfolio portfolio){
        portfolioArrayList.add(portfolio);
    }

    public static void addAccount(Account account){
        accountArrayList.add(account);
    }

    public static Account searchAccount(String address){
        for(Account account: accountArrayList) if(account.getUserName().equals(address)) return account;
        return null;
    }

    public static Portfolio searchPortfolio(String address){
        for(Portfolio portfolio: portfolioArrayList) if(portfolio.getAddress().equals(address)) return portfolio;
        return null;
    }

    @JsonIgnore
    public static LinkedList<Block> getBlockchain() {
        blockchain = Blockchain.getBlockchainList();
        return blockchain;
    }

    @JsonIgnore
    public void setBlockchain(LinkedList<Block> blockchain) {
        this.blockchain = blockchain;
    }





}
