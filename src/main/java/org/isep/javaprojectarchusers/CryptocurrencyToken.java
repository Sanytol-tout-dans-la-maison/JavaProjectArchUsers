package org.isep.javaprojectarchusers;

import java.util.ArrayList;

public class CryptocurrencyToken extends Asset{
    private String cryptoName;
    private static ArrayList<CryptocurrencyToken> cryptocurrencyTokenList = new ArrayList<>();

    @Override
    public String toString(){
        return this.cryptoName;
    }

    public static ArrayList<CryptocurrencyToken> getCryptocurrencyTokenList() {
        return cryptocurrencyTokenList;
    }

    public CryptocurrencyToken(String cryptoName){
        super(0.0);
        this.cryptoName = cryptoName;
        cryptocurrencyTokenList.add(this);
    }
}
