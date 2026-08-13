package com.games.nuvei.enums;

import lombok.Getter;

@Getter
public enum DigitalAssetTypeEnums {
    //The Special Condition Indicator (SCI) values must be included in the “digitalAssetType” parameter, as outlined below:
    //1 - Purchase of Central Bank Digital Currency (CBDC) or Tokenized Deposits
    //2 - Purchase of Stablecoin (Fiat-backed)
    //3 - Purchase of Blockchain Native Token/Coin
    //7 - Purchase of Cryptocurrency

    BLOCKCHAIN_NATIVE_TOKEN("3"),
    CRYPTOCURRENCY("7");

    private final String code;

    DigitalAssetTypeEnums(String code) {
        this.code = code;
    }
}
