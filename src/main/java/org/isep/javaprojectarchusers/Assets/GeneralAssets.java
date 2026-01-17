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
public class GeneralAssets {
    private double value;
    private static ArrayList<GeneralAssets> generalAssetList = new ArrayList<>();
    private ASSET_TYPE assetType;
    private String assetName;

    // --- AJOUT : Historique pour les graphiques ---
    // On utilise LinkedHashMap pour garantir que les dates restent dans l'ordre
    private Map<LocalDate, Double> priceHistory = new LinkedHashMap<>();

    public GeneralAssets(){
        this.value = 0.0;
        generalAssetList.add(this);
    }

    public GeneralAssets(@JsonProperty("assetName") String assetName, @JsonProperty("assetType") ASSET_TYPE assetType){
        this.assetName = assetName;
        generalAssetList.add(this);
    }

    // =========================================================================
    // GESTION DE L'HISTORIQUE (Requis pour Market et les graphiques)
    // =========================================================================

    /**
     * Ajoute une valeur à l'historique pour une date donnée.
     * Utilisée par Market.simulateMarketHistory().
     */
    public void addHistory(LocalDate date, double price) {
        priceHistory.put(date, price);
    }

    /**
     * @return La map complète de l'historique (Date -> Prix).
     */
    @JsonIgnore // On ne sauvegarde pas tout l'historique en JSON pour le moment
    public Map<LocalDate, Double> getPriceHistory() {
        return priceHistory;
    }

    // =========================================================================
    // GETTERS / SETTERS CLASSIQUES
    // =========================================================================

    @JsonIgnore
    public String getInfo(){
        return assetName + ": " + value;
    }

    public @JsonProperty("assetValue") double getValue() {
        return value;
    }

    public void setValue(@JsonProperty("assetValue") double value) {
        this.value = value;
    }

    public static ArrayList<GeneralAssets> getGeneralAssetList() {
        return generalAssetList;
    }

    public @JsonProperty("assetType") ASSET_TYPE getGeneralAssetType() {
        return assetType;
    }

    public void setAssetType(@JsonProperty("assetType") ASSET_TYPE assetType) {
        this.assetType = assetType;
    }

    public @JsonProperty("assetName") String getGeneralAssetName() {
        return assetName;
    }

    public void setGeneralAssetName(@JsonProperty("assetName") String assetName) {
        this.assetName = assetName;
    }


    @Override
    public String toString(){
        return this.getGeneralAssetName() + " (" + this.getValue() + ")";
    }
}