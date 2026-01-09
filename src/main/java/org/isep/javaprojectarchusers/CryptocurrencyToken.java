package org.isep.javaprojectarchusers;

public class CryptocurrencyToken extends Asset{
    private String cryptoName;

    @Override
    public String toString(){
        return this.cryptoName;
    }
}
