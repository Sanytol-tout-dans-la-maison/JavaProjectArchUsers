package org.isep.javaprojectarchusers.Assets;

import java.util.ArrayList;

public class CryptocurrencyToken extends Asset {
    private String cryptoName;
    private static ArrayList<CryptocurrencyToken> cryptocurrencyTokenList = new ArrayList<>();
    private final ASSET_TYPE assetType = ASSET_TYPE.CryptocurrencyToken;
    private String portfolio;

    @Override
    public String toString(){
        return this.cryptoName;
    }

    public static ArrayList<CryptocurrencyToken> getCryptocurrencyTokenList() {
        return cryptocurrencyTokenList;
    }

    public CryptocurrencyToken(String cryptoName, double value, String portfolio){
        super(cryptoName, ASSET_TYPE.CryptocurrencyToken, portfolio);
        cryptocurrencyTokenList.add(this);
    }
}
