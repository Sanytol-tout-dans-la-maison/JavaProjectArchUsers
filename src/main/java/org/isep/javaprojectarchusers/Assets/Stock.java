package org.isep.javaprojectarchusers.Assets;

import java.util.ArrayList;

public class Stock extends Asset {
    private String companyName;
    private static ArrayList<Stock> stockList = new ArrayList<>();
    private final ASSET_TYPE assetType = ASSET_TYPE.Stock;

    public Stock(String companyName, double value){
        super(companyName, value);
        stockList.add(this);
        }

    public static ArrayList<Stock> getStockList() {
        return stockList;
    }

    public Stock(String companyName){
        super(companyName,0.0);
        stockList.add(this);
        stockList.add(this);
    }

    @Override
    public String toString(){
        return this.companyName;
    }
}
