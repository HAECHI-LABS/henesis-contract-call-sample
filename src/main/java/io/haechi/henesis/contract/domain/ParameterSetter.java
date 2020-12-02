package io.haechi.henesis.contract.domain;

import io.haechi.henesis.contract.application.dto.ContractCallRequestBody;
import io.haechi.henesis.contract.config.HenesisApiProperties;
import io.haechi.henesis.contract.infra.EncodeFunction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ParameterSetter {
    private final HenesisApiProperties henesisApiProperties;
    private final EncodeFunction encodeFunction;

    public HenesisApiContractCallBody setBodyParams(ContractCallRequestBody contractCallRequestBody) {
        HenesisApiContractCallBody henesisApiContractCallBody = new HenesisApiContractCallBody();
        henesisApiContractCallBody.setData(this.encodeFunction.getHexTokenData(contractCallRequestBody));
        henesisApiContractCallBody.setContractAddress(this.henesisApiProperties.getErcTokenAddress());
        henesisApiContractCallBody.setValue("0x0");
        henesisApiContractCallBody.setPassphrase(this.henesisApiProperties.getPassphrase());
        if (contractCallRequestBody.getGasLimit() != null) {
            henesisApiContractCallBody.setGasLimit(contractCallRequestBody.getGasLimit());
        }
        if (contractCallRequestBody.getGasPrice() != null) {
            henesisApiContractCallBody.setGasPrice(contractCallRequestBody.getGasPrice());
        }
        return henesisApiContractCallBody;
    }
}
