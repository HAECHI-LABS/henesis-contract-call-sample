package io.haechi.henesis.example;

import io.haechi.henesis.example.config.TokenSupplier;
import io.haechi.henesis.example.contracts.IERC20;
import io.haechi.henesis.example.dto.ContractCallRequestBody;
import io.haechi.henesis.example.dto.ContractJsonObject;
import io.haechi.henesis.example.dto.HenesisApiContractCallBody;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service
public class ContractCallService {
    private final EnclaveHttpClient enclaveHttpClient;
    private final TokenSupplier<IERC20> tokenSupplier;

    @Qualifier("passphrase")
    private final String passphrase;

    public ContractCallService(EnclaveHttpClient enclaveHttpClient, TokenSupplier<IERC20> tokenSupplier, String passphrase) {
        this.enclaveHttpClient = enclaveHttpClient;
        this.tokenSupplier = tokenSupplier;
        this.passphrase = passphrase;
    }


    /**
     * Client에서 사용할 데이터를 인자로 주어 스마트 컨트랙트 호출을 요청합니다.
     */
    public ContractJsonObject contractCall(Blockchain blockchain, String masterWalletId, ContractCallRequestBody contractCallRequestBody) {
        return this.enclaveHttpClient.sendContractCall(blockchain, masterWalletId, this.buildData(contractCallRequestBody, blockchain));
    }

    /**
     * Henesis API에 사용하기 위한 핵심 데이터를 build하는 함수입니다.
     * transfer 함수는 두개의 인자를 받습니다. 1-ToAddress, 2-보낼 토큰 양(hexString을 BigInteger로 변환해야합니다.)
     * encodeFunctionCall()은 가져온 transfer function을 encoding 해주는 함수입니다.
     */
    public HenesisApiContractCallBody buildData(ContractCallRequestBody contractCallRequestBody, Blockchain blockchain) {
        String data = this.tokenSupplier.supply(blockchain).transfer(contractCallRequestBody.getToAddress(), new BigInteger(contractCallRequestBody.getValue().substring(2), 16)).encodeFunctionCall();
        return HenesisApiContractCallBody.of(
                this.tokenSupplier.supply(blockchain).getContractAddress(),
                data,
                passphrase,
                contractCallRequestBody.getGasLimit(),
                contractCallRequestBody.getGasPrice()
        );
    }
}
