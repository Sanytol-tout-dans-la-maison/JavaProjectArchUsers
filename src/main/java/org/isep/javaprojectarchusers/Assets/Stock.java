package org.isep.javaprojectarchusers.Assets;

import java.util.ArrayList;

public class Stock extends Asset {
    private String companyName;
    private static ArrayList<Stock> stockList = new ArrayList<>();
    private final ASSET_TYPE assetType = ASSET_TYPE.Stock;

    public Stock(String companyName, double value, String portfolio){
        super(companyName, value, portfolio);
        stockList.add(this);
        }

    public static ArrayList<Stock> getStockList() {
        return stockList;
    }

    public Stock(String companyName, String portfolio){
        super(companyName,0.0, portfolio);
        stockList.add(this);
        stockList.add(this);
    }

    @Override
    public String toString(){
        return this.companyName;
    }
}
