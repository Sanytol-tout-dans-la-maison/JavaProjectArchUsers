package org.isep.javaprojectarchusers.Assets;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Représente un actif financier (Action, Crypto, etc.) avec son historique de prix.
 */
public class Asset extends GeneralAssets {
    private double value;
    private static ArrayList<Asset> assetList = new ArrayList<>();
    private ASSET_TYPE assetType;
    private String assetName;
    private String portfolio;

    // --- AJOUT : Historique pour les graphiques ---
    // On utilise LinkedHashMap pour garantir que les dates restent dans l'ordre
    private Map<LocalDate, Double> priceHistory = new LinkedHashMap<>();

    public Asset(){
        this.value = 0.0;
        assetList.add(this);
    }

    public Asset(@JsonProperty("assetName") String assetName, @JsonProperty("assetType") ASSET_TYPE assetType,@JsonProperty("portfolio") String portfolio){
        super(assetName, assetType);
        this.portfolio = portfolio;
        assetList.add(this);
    }



    @JsonIgnore
    public String getInfo(){
        return assetName + ": " + value;
    }

    public @JsonProperty("assetValue") double getValue() {return value;}

    public void setValue(@JsonProperty("assetValue") double value) {
        this.value = value;
    }

    public static @JsonProperty("assets") ArrayList<Asset> getAssetList() {
        return assetList;
    }

    public static void setAssetList(ArrayList<Asset> assetList) {
        Asset.assetList = assetList;
    }

    public @JsonProperty("assetType") ASSET_TYPE getGeneralAssetType() {
        return assetType;
    }

    public void setAssetType(@JsonProperty("assetType") ASSET_TYPE assetType) {
        this.assetType = assetType;
    }

    public @JsonProperty("assetName") String getAssetName() {
        return assetName;
    }

    public void setGeneralAssetName(@JsonProperty("assetName") String assetName) {
        this.assetName = assetName;
    }

    public String getPortfolio() {
        return portfolio;
    }

    public void setPortfolio(String portfolio){
        this.portfolio = portfolio;
    }

    @Override
    public String toString(){
        return this.getAssetName() + " (" + this.getValue() + ")";
    }

    public double getRefreshedValue(LocalDate date){
        value = super.getPriceHistory().get(date);
        return value;
    }
}