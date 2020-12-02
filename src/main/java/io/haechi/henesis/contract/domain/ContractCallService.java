package io.haechi.henesis.contract.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.haechi.henesis.contract.application.dto.ContractCallRequestBody;
import io.haechi.henesis.contract.application.dto.ContractService;
import io.haechi.henesis.contract.infra.dto.SmartContractJsonObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContractCallService implements ContractService {
    private final HenesisApiClient henesisApiClient;
    private final ParameterSetter parameterSetter;

    public void requestMasterWalletContractCall(String blockchainSymbol, String masterWalletId, ContractCallRequestBody contractCallRequestBody) {
        log.info("Master wallet contract call has been requested.");
        SmartContractJsonObject masterContractCallResult = this.henesisApiClient.masterWalletContractCallHenesisApi(
                blockchainSymbol,
                masterWalletId,
                this.parameterSetter.setBodyParams(contractCallRequestBody)
        );
        String msg = String.format("\nTransaction ID: %s\nStatus: %s\n", masterContractCallResult.getId(), masterContractCallResult.getStatus());
        log.info(msg);
    }

    public void requestUserWalletContractCall(String blockchainSymbol, String masterWalletId, String userWalletId, ContractCallRequestBody contractCallRequestBody) {
        log.info("User wallet contract call has been requested.");
        SmartContractJsonObject userContractCallResult = this.henesisApiClient.userWalletContractCallHenesisApi(
                blockchainSymbol,
                masterWalletId,
                userWalletId,
                this.parameterSetter.setBodyParams(contractCallRequestBody)
        );
        String msg = String.format("\nTransaction ID: %s\nStatus: %s\n", userContractCallResult.getId(), userContractCallResult.getStatus());
        log.info(msg);
    }
}
