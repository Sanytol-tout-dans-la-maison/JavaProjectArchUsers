package org.isep.javaprojectarchusers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

public class TestJson {
    public static void main() throws IOException {
        CryptocurrencyToken token = new CryptocurrencyToken("Bitcoin");
        Stock stock = new Stock("S&P500", 5);
        ArrayList<Asset> assetList = Asset.getAssetList();
        Events event = new Events(EVENT_TYPE.Crash, 0.7, LocalDate.of(2026,01,12).toString(), assetList.getFirst());
        Events event2 = new Events(EVENT_TYPE.Covid_19, 0.5, LocalDate.of(2019,10,1).toString(), assetList.getFirst());
        ArrayList<Events> list= new ArrayList<Events>();
        list.add(event);
        list.add(event2);
        System.out.println(Stock.getStockList().getFirst().getAssetName());
        System.out.println(list.getFirst().getAsset().getAssetName());
        ObjectMapper objectMapper = new ObjectMapper();
        File file = new File("src/main/resources/org/isep/javaprojectarchusers/events.json");
        objectMapper.writeValue(file, list);
//        //File file = new File("src/main/resources/org/isep/javaprojectarchusers/events.json");
//        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        ArrayList<Events> copyList = objectMapper.readValue(file, new TypeReference<ArrayList<Events>>(){});
        for (Events e : copyList) System.out.println(e);
        MainBackEnd.extractPortfolios();
        Blockchain.extractBlockchain();
        PortfolioManager portfolioManager = new PortfolioManager();
        portfolioManager.createPortfolio("test1","first test");
        portfolioManager.createPortfolio("test2", "second test");
        portfolioManager.getPortfolio("test1").createCheckingAccount("testAccount1");
        portfolioManager.getPortfolio("test2").createCheckingAccount("testAccount2");
        Transaction test1 = new Transaction(portfolioManager.getPortfolio("test1").getAddress(),portfolioManager.getPortfolio("test2").getAddress(),portfolioManager.getPortfolio("test1").getAccount("testAccount1").getUserName(),portfolioManager.getPortfolio("test2").getAccount("testAccount2").getUserName(),90);
        System.out.println(Blockchain.getLast().getTransactions());
        Transaction test2 = new Transaction(portfolioManager.getPortfolio("test1").getAddress(),portfolioManager.getPortfolio("test2").getAddress(),portfolioManager.getPortfolio("test1").getAccount("testAccount1").getUserName(),portfolioManager.getPortfolio("test2").getAccount("testAccount2").getUserName(),9000);
        System.out.println(Blockchain.getLast().getTransactions());
        MainBackEnd.savePortfolios();
        Blockchain.saveBlockchain();
    }
}
