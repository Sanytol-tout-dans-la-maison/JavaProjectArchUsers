package org.isep.javaprojectarchusers;

import java.util.ArrayList;

public class Asset {
    private double value;
    private static ArrayList<Asset> assetList = new ArrayList<>();

    public Asset(){
        this.value = 0.0;
        assetList.add(this);
    }

    public Asset(double value){
        this.value = value;
        assetList.add(this);
    }

    public String getInfo(){
        return String.valueOf(value);
    }

    public double getValue() {
        return value;
    }

    public static ArrayList<Asset> getAssetList() {
        return assetList;
    }

    @Override
    public String toString(){
        return this.getInfo();
    }
}
