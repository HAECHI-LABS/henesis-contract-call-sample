package io.haechi.henesis.contract.application.dto;

public interface ContractService {
    void requestMasterWalletContractCall(
            String blockchainSymbol,
            String masterWalletId,
            ContractCallRequestBody contractCallRequestBody
    );

    void requestUserWalletContractCall(
            String blockchainSymbol,
            String masterWalletId,
            String userWalletId,
            ContractCallRequestBody contractCallRequestBody
    );
}
