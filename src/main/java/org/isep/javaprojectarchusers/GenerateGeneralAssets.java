package org.isep.javaprojectarchusers;

import org.isep.javaprojectarchusers.Assets.ASSET_TYPE;
import org.isep.javaprojectarchusers.Assets.GeneralAssets;

public class GenerateGeneralAssets {
    public static void generate(){
        new GeneralAssets("Bitcoin", ASSET_TYPE.CryptocurrencyToken);
        new GeneralAssets("Ethereum",ASSET_TYPE.CryptocurrencyToken);
        new GeneralAssets("Solana", ASSET_TYPE.CryptocurrencyToken);
        new GeneralAssets("XRP", ASSET_TYPE.CryptocurrencyToken);
        new GeneralAssets("Apple", ASSET_TYPE.Stock);
        new GeneralAssets("Nvidia", ASSET_TYPE.Stock);
        new GeneralAssets("Tesla", ASSET_TYPE.Stock);
        new GeneralAssets("Microsoft", ASSET_TYPE.Stock);
    }
}
