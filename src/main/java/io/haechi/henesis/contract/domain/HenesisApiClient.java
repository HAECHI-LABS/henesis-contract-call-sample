package io.haechi.henesis.contract.domain;

import io.haechi.henesis.contract.infra.dto.SmartContractJsonObject;

public interface HenesisApiClient {
    SmartContractJsonObject masterWalletContractCallHenesisApi(String blockchainSymbol, String masterWalletId, HenesisApiContractCallBody henesisApiContractCallBody);

    SmartContractJsonObject userWalletContractCallHenesisApi(String blockchainSymbol, String masterWalletId, String userWalletId, HenesisApiContractCallBody henesisApiContractCallBody);
}
