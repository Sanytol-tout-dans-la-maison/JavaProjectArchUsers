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

public class MainBackEnd {
    private static ArrayList<Portfolio> portfolioArrayList=  new ArrayList<>();
    private static ArrayList<Account> accountArrayList = new ArrayList<>();
    private static LinkedList blockchain;

    public static ArrayList<Portfolio> getPortfolioArrayList() {
        return portfolioArrayList;
    }

    public static void savePortfolios() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(new File("src/main/resources/org/isep/javaprojectarchusers/portfolios.json"), portfolioArrayList);
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
