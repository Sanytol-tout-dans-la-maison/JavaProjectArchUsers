package org.isep.javaprojectarchusers;

import java.util.ArrayList;

public class CryptocurrencyToken extends Asset{
    private String cryptoName;
    private static ArrayList<CryptocurrencyToken> cryptocurrencyTokenList = new ArrayList<>();
    private final ASSET_TYPE assetType = ASSET_TYPE.CryptocurrencyToken;

    @Override
    public String toString(){
        return this.cryptoName;
    }

    public static ArrayList<CryptocurrencyToken> getCryptocurrencyTokenList() {
        return cryptocurrencyTokenList;
    }

    public CryptocurrencyToken(String cryptoName){
        super(cryptoName, 0.0);
        cryptocurrencyTokenList.add(this);
    }
}
