package io.haechi.henesis.example;

import io.haechi.henesis.example.contracts.IERC20;
import io.haechi.henesis.example.dto.ContractCallRequestBody;
import io.haechi.henesis.example.dto.ContractJsonObject;
import io.haechi.henesis.example.dto.HenesisApiContractCallBody;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@RequiredArgsConstructor
@Service
public class ContractCallService {
    private final EnclaveHttpClient enclaveHttpClient;
    private final IERC20 ierc20;

    @Qualifier("passphrase")
    private final String passphrase;

    /**
     * Client에서 사용할 데이터를 인자로 주어 스마트 컨트랙트 호출을 요청합니다.
     */
    public ContractJsonObject contractCall(String masterWalletId, ContractCallRequestBody contractCallRequestBody) {
        return this.enclaveHttpClient.sendContractCall(masterWalletId, this.buildData(contractCallRequestBody));
    }

    /**
     * Henesis API에 사용하기 위한 핵심 데이터를 build하는 함수입니다.
     * transfer 함수는 두개의 인자를 받습니다. 1-ToAddress, 2-보낼 토큰 양(hexString을 BigInteger로 변환해야합니다.)
     * encodeFunctionCall()은 가져온 transfer function을 encoding 해주는 함수입니다.
     */
    public HenesisApiContractCallBody buildData(ContractCallRequestBody contractCallRequestBody) {
        String data = this.ierc20.transfer(contractCallRequestBody.getToAddress(), new BigInteger(contractCallRequestBody.getValue().substring(2), 16)).encodeFunctionCall();
        return HenesisApiContractCallBody.of(
                this.ierc20.getContractAddress(),
                data,
                passphrase,
                contractCallRequestBody.getGasLimit(),
                contractCallRequestBody.getGasPrice()
        );
    }
}
