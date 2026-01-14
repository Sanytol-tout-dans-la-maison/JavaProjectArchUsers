package org.isep.javaprojectarchusers;

import java.util.ArrayList;

public class Stock extends Asset{
    private String companyName;
    private static ArrayList<Stock> stockList = new ArrayList<>();

    public Stock(String companyName, double value){
        super(value);
        this.companyName = companyName;
        }

    public static ArrayList<Stock> getStockList() {
        return stockList;
    }

    public Stock(String companyName){
        super(0.0);
        this.companyName = "Uknow";
        stockList.add(this);
    }

    @Override
    public String toString(){
        return this.companyName;
    }
}
