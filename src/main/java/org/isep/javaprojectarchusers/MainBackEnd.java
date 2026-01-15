package org.isep.javaprojectarchusers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainBackEnd {
    private static ArrayList<Portfolio> portfolioArrayList= new ArrayList<>();

    public static ArrayList<Portfolio> getPortfolioArrayList() {
        return portfolioArrayList;
    }

    public static void savePortfolios() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(new File("src/main/resources/org/isep/javaprojectarchusers/portfolios.csv"), portfolioArrayList);
    }

    public static void extractPortfolios() throws IOException{
        ObjectMapper objectMapper = new ObjectMapper();
        portfolioArrayList = objectMapper.readValue("src/main/resources/org/isep/javaprojectarchusers/portfolios.csv", new TypeReference<ArrayList<Portfolio>>(){});
    }

    public static void addPortfolio(Portfolio portfolio){
        portfolioArrayList.add(portfolio);
    }
}
