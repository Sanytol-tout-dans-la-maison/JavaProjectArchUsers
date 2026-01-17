package org.isep.javaprojectarchusers.Assets;

import java.util.ArrayList;

public class Stock extends Asset {
    private String companyName;
    private static ArrayList<Stock> stockList = new ArrayList<>();
    private final ASSET_TYPE assetType = ASSET_TYPE.Stock;

    public Stock(String companyName,  String portfolio){
        super(companyName, value, ASSET_TYPE.Stock ,portfolio);
        stockList.add(this);
        }

    public static ArrayList<Stock> getStockList() {
        return stockList;
    }

    public Stock(String companyName, String portfolio){
        super(companyName, ASSET_TYPE.Stock, portfolio);
        stockList.add(this);
        stockList.add(this);
    }

    @Override
    public String toString(){
        return this.companyName;
    }
}
