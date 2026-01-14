package org.isep.javaprojectarchusers;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

public class Asset {
    private double value;
    private static ArrayList<Asset> assetList = new ArrayList<>();
    private ASSET_TYPE assetType;
    private String assetName;

    public Asset(){
        this.value = 0.0;
        assetList.add(this);
    }

    public Asset(@JsonProperty("assetName") String assetName, @JsonProperty("assetValue") double value){
        this.assetName = assetName;
        this.value = value;
        assetList.add(this);
    }

    @JsonIgnore
    public String getInfo(){
        return String.valueOf(value);
    }

    public @JsonProperty("assetValue") double getValue() {
        return value;
    }

    public void setValue(@JsonProperty("assetValue") double value) {
        this.value = value;
    }

    public static ArrayList<Asset> getAssetList() {
        return assetList;
    }

    public @JsonProperty("assetType") ASSET_TYPE getAssetType() {
        return assetType;
    }

    public void setAssetType(@JsonProperty("assetType") ASSET_TYPE assetType) {
        this.assetType = assetType;
    }

    public @JsonProperty("assetName") String getAssetName() {
        return assetName;
    }

    public void setAssetName(@JsonProperty("assetName") String assetName) {
        this.assetName = assetName;
    }

    @Override
    public String toString(){
        return this.getInfo();
    }
}
